package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.Texture2D;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.util.ApiIOUtils;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBTruetype.*;

public class CustomFont
{
	private final Identifier identifier;
	private int height;
	private float scale;
	private float fontScale = 1;
	private float ascent;
	public Texture2D texture;
	private GlyphInfo[] glyphs;

	public CustomFont(Identifier identifier)
	{
		this.identifier = identifier;
	}

	public void init(ResourceManager manager)
	{
		ApiIOUtils.getResourceByID(manager, identifier).ifPresent(stream ->
		{
			final byte[] bytes = ApiIOUtils.readBytes(stream);
			final ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length).put(bytes);
			buffer.flip();
			create(buffer, 18);
		});
	}

	public void create(ByteBuffer buffer, int height)
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

	public float render(MatrixStack matrixStack, String string, float x, float y)
	{
		return render(matrixStack, string, new ColourHolder(255, 255, 255, 255), x, y, fontScale, false);
	}

	public float render(MatrixStack matrixStack, String string, float x, float y, float scale)
	{
		return render(matrixStack, string, new ColourHolder(255, 255, 255, 255), x, y, scale, false);
	}

	public float render(MatrixStack matrixStack, String string, float x, float y, float scale, boolean outline)
	{
		return render(matrixStack, string, new ColourHolder(255, 255, 255, 255), x, y, scale, outline);
	}

	public float getHeight()
	{
		return height * fontScale;
	}

	public CustomFont setFontScale(float fontScale)
	{
		this.fontScale = fontScale;
		return this;
	}

	public float getWidth(String string)
	{
		float x = 0;
		for (int i = 0; i < string.length(); i++)
		{
			int character = string.charAt(i);

			if (character < 32 || character > 256) character = 32;
			if (character == 36 && i + 1 < string.length() && string.charAt(i + 1) == 123)
			{
				final int i1 = string.indexOf("${", i);
				final int i2 = string.indexOf("}", i);
				if (i1 != -1 && i2 != -1)
					i += i2 - i1;
			} else
			{
				final GlyphInfo glyph = glyphs[character - 32];
				x += glyph.xAdvance() * fontScale;
			}

		}

		return x;
	}

	public float render(MatrixStack matrixStack, String string, ColourHolder colourHolder, float x, float y, float scale, boolean outline)
	{

		y += ascent * this.scale * scale;

		for (int i = 0; i < string.length(); i++)
		{
			int character = string.charAt(i);

			if (character < 32 || character > 256) character = 32;
			if (character == 36 && i + 1 < string.length() && string.charAt(i + 1) == 123)
			{
				final int i1 = string.indexOf("${", i);
				final int i2 = string.indexOf("}", i);
				if (i1 != -1 && i2 != -1)
				{
					final String substring = string.substring(i1 + 2, i2);
					colourHolder = ColourHolder.decode(substring);
					i += i2 - i1;
				}
			} else
			{
				final GlyphInfo glyph = glyphs[character - 32];
				RenderUtil.setShader(outline ? APIShaders.SHADE_MASK_SHADER.getShader() : APIShaders.MASK_SHADER.getShader());
				RenderUtil.setShaderTexture(texture.getID());
				RenderUtil.setPostShaderBind(shader -> shader.setArgument("InSize", new Vec2f(glyph.u1(), glyph.v1())));
				RenderUtil.quadTexture(matrixStack, x + glyph.x() * scale, y + glyph.y() * scale, x + glyph.w() * scale, y + glyph.h() * scale, glyph.u0(), glyph.v0(), glyph.u1(), glyph.v1(), colourHolder);
				x += glyph.xAdvance() * scale;
			}

		}
		return x;
	}


}
