package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.texture.Texture2D;
import me.thesilverecho.zeropoint.api.util.ApiIOUtils;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
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
	private final ColourHolder[] vertexColours = new ColourHolder[4];
	private float fontScale;

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

/*	public float render(MatrixStack matrixStack, String string, float x, float y)
	{
		return render(matrixStack.peek().getModel(), string, new ColourHolder(255, 255, 255, 255), x, y, fontScale, false);
	}

	public float render(MatrixStack matrixStack, String string, float x, float y, float scale)
	{
		return render(matrixStack.peek().getModel(), string, new ColourHolder(255, 255, 255, 255), x, y, scale, false);
	}

	public float renderOutline(MatrixStack matrixStack, String string, float x, float y)
	{
		return render(matrixStack.peek().getModel(), string, new ColourHolder(255, 255, 255, 255), x, y, fontScale, true);
	}

	public float renderOutline(MatrixStack matrixStack, String string, float x, float y, float scale)
	{
		return render(matrixStack.peek().getModel(), string, new ColourHolder(255, 255, 255, 255), x, y, scale, true);
	}

	public float render(MatrixStack matrixStack, String string, float x, float y, float scale, boolean outline)
	{
		return render(matrixStack.peek().getModel(), string, new ColourHolder(255, 255, 255, 255), x, y, scale, outline);
	}*/

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
				x += glyph.xAdvance();
			}

		}

		return x;
	}

	/*public float render(Matrix4f matrixStack, String string, ColourHolder defaultColour, float x, float y, float scale, boolean outline)
	{
//		Fills the vertex colours with the default colour every time a new string is rendered.
		Arrays.fill(vertexColours, defaultColour);
		y += ascent * this.scale * scale;
		for (int i = 0; i < string.length(); i++)
		{
//			Get the character from the string.
			int character = string.charAt(i);
//			If the character is out of bounds replace it with default.
			if (character < 32 || character > 256) character = 32;
//			Look for custom formatting from the string ${format type}.
//			\$\{(.*?)}
			if (character == '$' && i + 1 < string.length() && string.charAt(i + 1) == '{')
			{
				final int startOfFormatString = string.indexOf("${", i);
				final int endOfFormatString = string.indexOf("}", i);
//				Checks if the custom format string has a start and end.
				if (startOfFormatString != -1 && endOfFormatString != -1)
				{
//					Pulls out the string where containing the format options.
					final String substring = string.substring(startOfFormatString + 2, endOfFormatString);
//					Split custom options if possible.
					final String[] split = substring.split(",");
//					Apply colour to all
					if (split.length > 1)
					{
//						When populating the vertex colour array it is essential to make sure that the size of the array is not exceeded.
						for (int currentColourIndex = 0; currentColourIndex < vertexColours.length; currentColourIndex++)
//							This ensures that if less than 4 colours are passed in. The default colour is used for the other vertices.
							if (currentColourIndex < split.length)
								vertexColours[currentColourIndex] = ColourHolder.decode(split[currentColourIndex]);
							else
								vertexColours[currentColourIndex] = defaultColour;
					} else
					{
						Arrays.fill(vertexColours, ColourHolder.decode(substring));
//						defaultColour = ColourHolder.decode(substring);
					}
					i += endOfFormatString - startOfFormatString;
				}
			} else
			{
//				Resets any formatting after a space.
				if (character == ' ')
					Arrays.fill(vertexColours, defaultColour);


				x = getX(matrixStack, defaultColour, x, y, scale, glyphs[character - 32]);
			}

		}
		return x;
	}*/

	public float getX(Matrix4f matrixStack, ColourHolder defaultColour, float x, float y, float scale, GlyphInfo glyph1)
	{
		final GlyphInfo glyph = glyph1;
		RenderUtilV2.setShader(APIShaders.FONT_MASK_TEXTURE.getShader());
		RenderUtilV2.setTextureId(texture.getID());

		RenderUtilV2.quadTexture(matrixStack,
				x + glyph.x() * scale,
				y + glyph.y() * scale,
				x + glyph.w() * scale,
				y + glyph.h() * scale,
				glyph.u0(),
				glyph.v0(),
				glyph.u1(),
				glyph.v1(),
				vertexColours[0] == null ? defaultColour : vertexColours[0],
				vertexColours[1] == null ? defaultColour : vertexColours[1],
				vertexColours[2] == null ? defaultColour : vertexColours[2],
				vertexColours[3] == null ? defaultColour : vertexColours[3]);
		x += glyph.xAdvance() * scale;
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
