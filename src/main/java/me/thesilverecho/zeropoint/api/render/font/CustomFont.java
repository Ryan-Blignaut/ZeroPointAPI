package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.api.render.texture.Texture2D;
import me.thesilverecho.zeropoint.api.util.ApiIOUtils;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.stb.STBTruetype.*;

public class CustomFont
{
	//	Map containing all fonts.
	private static final HashMap<String, CustomFont> ALL_FONTS = new HashMap<>();

	private boolean loaded;
	private final String name;
	private final Identifier customFontID;

	private int height;
	private float scale, ascent;

	public Texture2D texture;
	private GlyphInfo[] glyphs;


	public CustomFont(String nameSpace, String path)
	{
		this.customFontID = new Identifier(nameSpace, path);
		final int lastSlash = path.lastIndexOf("/");
		final int extensionStart = path.lastIndexOf(".");
		this.name = lastSlash == -1 ? path : path.substring(lastSlash + 1, extensionStart);
	}

	public static CustomFont getFontByName(String name)
	{
		final CustomFont customFont = ALL_FONTS.get(name);
		if (customFont == null)
			return APIFonts.REGULAR.getFont();
		return customFont.get();
	}

	public CustomFont get()
	{
		ALL_FONTS.putIfAbsent(this.name, this);
		if (!this.loaded)
			loadFont();
		return this;
	}


	private void loadFont()
	{
		ApiIOUtils.getResourceFromClientPack(this.customFontID).ifPresent(this::loadFont);
		this.loaded = true;
	}

	/**
	 * Loads font from input stream.
	 *
	 * @param inputStream where to load font from.
	 */
	public void loadFont(InputStream inputStream)
	{
		this.create(ApiIOUtils.readBytesToBuffer(inputStream), 18);
	}

	public String getName()
	{
		return name;
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

		this.texture = new Texture2D(2048, 2048, byteBuffer, Texture2D.Format.A);

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

	public GlyphInfo[] getGlyphs()
	{
		return glyphs;
	}

	public float getHeight()
	{
		return height;
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
				x += glyph.xAdvance();
			}

		}

		return x;
	}


	public GlyphInfo getGlyph(int location)
	{
		return this.glyphs[location];
	}

	public float getAscent()
	{
		return this.ascent;
	}

	public Texture2D getTexture()
	{
		return this.texture;
	}

	public float getScale()
	{
		return this.scale;
	}
}
