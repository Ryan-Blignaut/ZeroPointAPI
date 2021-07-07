package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.api.render.ColourHolder;
import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.Texture2D;
import me.thesilverecho.zeropoint.api.render.shader.MaskTextShader;
import me.thesilverecho.zeropoint.api.render.shader.ShaderManager;
import me.thesilverecho.zeropoint.api.util.IOUtils;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.stb.STBTruetype.*;

public class CustomFont
{
	private final int height;
	private final float scale;
	private final float ascent;
	public Texture2D texture;

	private final GlyphInfo[] glyphs;


	public static CustomFont createCustomFont(Identifier identifier)
	{
		AtomicReference<CustomFont> ret = new AtomicReference<>();
		IOUtils.getResourceByID(identifier).ifPresent(stream ->
		{
			ZeroPointApiLogger.error("we have opened the stream");
			final byte[] bytes = IOUtils.readBytes(stream);
			final ByteBuffer buffer = ByteBuffer.allocate(bytes.length).put(bytes);
			buffer.flip();
			ret.set(new CustomFont(buffer, 18));
		});
		return ret.get();
	}

	public CustomFont(ByteBuffer buffer, int height)
	{
		this.height = height;
		final STBTTFontinfo fontInfo = STBTTFontinfo.create();
		stbtt_InitFont(fontInfo, buffer);
		glyphs = new GlyphInfo[256];
		final STBTTPackedchar.Buffer packedChars = STBTTPackedchar.create(glyphs.length);
		final ByteBuffer byteBuffer = BufferUtils.createByteBuffer(2048 * 2048);

		final STBTTPackContext stbttPackContext = STBTTPackContext.create();
		stbtt_PackBegin(stbttPackContext, byteBuffer, 2048, 2048, 0, 1);
		stbtt_PackSetOversampling(stbttPackContext, 2, 2);
		stbtt_PackFontRange(stbttPackContext, buffer, 0, height, 32, packedChars);
		stbtt_PackEnd(stbttPackContext);

		texture = new Texture2D(2048, 2048, byteBuffer, Texture2D.Format.A);

		this.scale = stbtt_ScaleForPixelHeight(fontInfo, height);

		try (MemoryStack stack = MemoryStack.stackPush())
		{
			final IntBuffer ascent = stack.mallocInt(1);
			stbtt_GetFontVMetrics(fontInfo, ascent, null, null);
			this.ascent = ascent.get(0);
		}

		for (int i = 0; i < glyphs.length; i++)
		{
			final STBTTPackedchar packedChar = packedChars.get(i);

			float ipw = 1f / 2048;
			float iph = 1f / 2048;

			glyphs[i] = new GlyphInfo(packedChar.xoff(),
					packedChar.yoff(),
					packedChar.xoff2(),
					packedChar.yoff2(),
					packedChar.x0() * ipw,
					packedChar.y0() * iph,
					packedChar.x1() * ipw,
					packedChar.y1() * iph,
					packedChar.xadvance());
		}
	}

	public float render(MatrixStack matrixStack, String string, float x, float y, float scale)
	{
		y += ascent * this.scale * scale;

		for (int i = 0; i < string.length(); i++)
		{
			int cp = string.charAt(i);
			if (cp < 32 || cp > 256) cp = 32;
			final GlyphInfo glyph = glyphs[cp - 32];
			RenderUtil.setShader(ShaderManager.getShader(MaskTextShader.class));
			RenderUtil.setShaderTexture(texture.getID());
			RenderUtil.quadTexture(matrixStack, x + glyph.x() * scale, y + glyph.y() * scale, x + glyph.w() * scale, y + glyph.h() * scale, glyph.u0(), glyph.v0(), glyph.u1(), glyph.v1(), new ColourHolder(255, 255, 255, 255));
			x += glyph.xAdvance() * scale;
		}
		return x;
	}

}
