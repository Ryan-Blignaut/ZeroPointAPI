package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import java.awt.*;
import java.util.Arrays;

public class FontRenderer
{
	public static float getHeight(CustomFont font, float size)
	{
		return font.getHeight() * size;
	}

	public static float getHeight(CustomFont font, float wrapWidth, float size)
	{
		return font.getHeight() * size;
	}

	public static float getWrapHeight(CustomFont font, float size, String string, float wrapWidth)
	{
		float x = 0;
		float y = 0;
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
				x += font.getGlyph(character - 32).xAdvance() * size;
				if (x >= (0 + wrapWidth))
				{
					x = 0;
					y += getHeight(font, size);
				}
			}
		}
		return y;
	}


	public static float getWidth(CustomFont font, float size, String string)
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
				x += font.getGlyph(character - 32).xAdvance() * size;

		}
		return x;
	}

	public static float renderText(CustomFont font, MatrixStack matrixStack, String text, float x, float y)
	{
		return renderText(font, 1, matrixStack, text, false, x, y);
	}

	public static float renderText(CustomFont font, Matrix4f matrix4f, String text, float x, float y)
	{
		return renderText(font, 0.5f, matrix4f, text, false, x, y);
	}

	public static float renderText(CustomFont font, float size, Matrix4f matrix4f, String text, boolean background, float x, float y)
	{
		return renderText(font, size, text, ColourHolder.FULL, background, matrix4f, x, y);
	}

	public static float renderText(CustomFont font, float size, String text, ColourHolder baseColour, boolean background, Matrix4f matrix4f, float x, float y)
	{
		if (text == null) text = "Error text String is empty.";
		y += font.getAscent() * font.getScale() * size;
//      Loop for each letter of the text.
		final ColourHolder[] colours = new ColourHolder[4];
		Arrays.fill(colours, baseColour);
		for (int index = 0; index < text.length(); index++)
		{
//          Get the integer representation each letter of the text at given index.
			int character = text.charAt(index);
//			If the character is out of bounds replace it with default.
			if (character < 32 || character > 256) character = 32;
//			Look for custom formatting from the string ${format type}.
			if (character == '$' && index + 1 < text.length() && text.charAt(index + 1) == '{')
				index = applyFormatting(character, index, text, baseColour, colours, x, y);
			else
			{
//				if (background)
//					renderChar(font, size, character, new ColourHolder(43, 43, 43, 255), matrix4f, x + 0.5f, y + 0.5f);
				x += renderChar(font, size, character, colours, matrix4f, x, y);
			}
		}
//      Reset size and colours;
//		System.out.println(x);
		return x;
	}

	public static float renderText(CustomFont font, float size, CharSequence text, ColourHolder baseColour, boolean background, Matrix4f matrix4f, float x, float y)
	{
		if (text == null) text = "Error text String is empty.";
		y += font.getAscent() * font.getScale() * size;
//      Loop for each letter of the text.
		final ColourHolder[] colours = new ColourHolder[4];
		Arrays.fill(colours, baseColour);
		for (int index = 0; index < text.length(); index++)
		{
//          Get the integer representation each letter of the text at given index.
			int character = text.charAt(index);
//			If the character is out of bounds replace it with default.
			if (character < 32 || character > 256) character = 32;
//			Look for custom formatting from the string ${format type}.
		/*	if (character == '$' && index + 1 < text.length() && text.charAt(index + 1) == '{')
				index = applyFormatting(character, index, text, baseColour, colours, x, y);*/
//			else
			{
//				if (background)
//					renderChar(font, size, character, new ColourHolder(43, 43, 43, 255), matrix4f, x + 0.5f, y + 0.5f);
				x += renderChar(font, size, character, colours, matrix4f, x, y);
			}
		}
//      Reset size and colours;
//		System.out.println(x);
		return x;
	}

	private static float renderChar(CustomFont font, float size, int characterLoc, ColourHolder[] vertexColours, Matrix4f matrixStack, float x, float y)
	{
		final GlyphInfo glyph = font.getGlyph(characterLoc - 32);
		RenderUtilV2.setShader(APIShaders.FONT_MASK_TEXTURE.getShader());
		RenderUtilV2.setTextureId(font.getTexture().getID());
		RenderUtilV2.setQuadColourHolder(vertexColours[0]);

		RenderUtilV2.quadTexture(matrixStack,
				x + glyph.x() * size,
				y + glyph.y() * size,
				x + glyph.w() * size,
				y + glyph.h() * size,
				glyph.u0(),
				glyph.v0(),
				glyph.u1(),
				glyph.v1(),
				vertexColours[0],
				vertexColours[1],
				vertexColours[2],
				vertexColours[3]);
		return glyph.xAdvance() * size;

	}


	public static float renderText(CustomFont font, float size, MatrixStack matrixStack, String text, float x, float y)
	{
		return renderText(font, size, matrixStack, text, false, x, y);
	}

	public static float renderText(CustomFont font, MatrixStack matrixStack, String text, boolean background, float x, float y)
	{
		return renderText(font, 1, text, ColourHolder.FULL, background, matrixStack, x, y);
	}

	public static float renderText(CustomFont font, float size, MatrixStack matrixStack, String text, boolean background, float x, float y)
	{
		return renderText(font, size, text, ColourHolder.FULL, background, matrixStack, x, y);
	}


	public static float renderTextWrapped(CustomFont font, float size, String text, ColourHolder baseColour, boolean background, MatrixStack matrixStack, float x, float y, float wrapWidth)
	{
		float originalX = x;
		y += font.getAscent() * font.getScale() * size;
//      Loop for each letter of the text.
		final ColourHolder[] colours = new ColourHolder[4];
		Arrays.fill(colours, baseColour);
		for (int index = 0; index < text.length(); index++)
		{
//          Get the integer representation each letter of the text at given index.
			int character = text.charAt(index);
//			If the character is out of bounds replace it with default.
			if (character < 32 || character > 256) character = 32;
//			Look for custom formatting from the string ${format type}.
			if (character == '$' && index + 1 < text.length() && text.charAt(index + 1) == '{')
				index = applyFormatting(character, index, text, baseColour, colours, x, y);
			else
			{
				if (background)
					renderChar(font, size, character, new ColourHolder(43, 43, 43, 255), matrixStack, x + 0.5f, y + 0.5f);
				x += renderChar(font, size, character, colours, matrixStack, x, y);
				if (x >= (originalX + wrapWidth))
				{
					x = originalX;
					y += getHeight(font, size);
				}
			}
		}
		return x;
	}


	public static float renderText(CustomFont font, float size, String text, ColourHolder baseColour, boolean background, MatrixStack matrixStack, float x, float y)
	{
		if (text == null) text = "Error text String is empty.";
		y += font.getAscent() * font.getScale() * size;
//      Loop for each letter of the text.
		final ColourHolder[] colours = new ColourHolder[4];
		Arrays.fill(colours, baseColour);
		for (int index = 0; index < text.length(); index++)
		{
//          Get the integer representation each letter of the text at given index.
			int character = text.charAt(index);
//			If the character is out of bounds replace it with default.
			if (character < 32 || character > 256) character = 32;
//			Look for custom formatting from the string ${format type}.
			if (character == '$' && index + 1 < text.length() && text.charAt(index + 1) == '{')
				index = applyFormatting(character, index, text, baseColour, colours, x, y);
			else
			{
				if (background)
					renderChar(font, size, character, new ColourHolder(43, 43, 43, 255), matrixStack, x + 0.5f, y + 0.5f);
				x += renderChar(font, size, character, colours, matrixStack, x, y);
			}
		}
//      Reset size and colours;
//		System.out.println(x);
		return x;
	}


	private static int applyFormatting(int character, int position, String text, ColourHolder baseColour, ColourHolder[] colours, float x, float y)
	{
		if (character == '$' && position + 1 < text.length() && text.charAt(position + 1) == '{')
		{
			final int startOfFormatString = text.indexOf("${", position);
			final int endOfFormatString = text.indexOf("}", position);
//				Checks if the custom format string has a start and end.
			if (startOfFormatString != -1 && endOfFormatString != -1)
			{
//					Pulls out the string where containing the format options.
				final String substring = text.substring(startOfFormatString + 2, endOfFormatString);
				if (substring.contains("rainbow"))
				{
					final float delay = 5000;
					final long timePos = System.currentTimeMillis() - (int) (x * 10 - y * 10);
					final int colour = Color.HSBtoRGB((timePos % (long) delay) / delay, 0.8f, 0.8f);
					Arrays.fill(colours, new ColourHolder(colour));
				} else
					applyColour(baseColour, colours, substring);
				position += endOfFormatString - startOfFormatString;
			}
		}
		return position;
	}

	private static int countFormatting(int character, int position, String text)
	{
		if (character == '$' && position + 1 < text.length() && text.charAt(position + 1) == '{')
		{
			final int startOfFormatString = text.indexOf("${", position);
			final int endOfFormatString = text.indexOf("}", position);
//			Checks if the custom format string has a start and end.
			if (startOfFormatString != -1 && endOfFormatString != -1)
			{
//				Pulls out the string where containing the format options.
				final String substring = text.substring(startOfFormatString + 2, endOfFormatString);
//				Split custom options if possible.
				position += endOfFormatString - startOfFormatString;
			}
		}
		return position;
	}


	private static void applyColour(ColourHolder baseColour, ColourHolder[] colours, String substring)
	{
//		Split custom options if possible.
		final String[] split = substring.split(",");
//		Apply colour to all
		if (split.length > 1)
		{
//			When populating the vertex colour array it is essential to make sure that the size of the array is not exceeded.
			for (int currentColourIndex = 0; currentColourIndex < colours.length; currentColourIndex++)
//  			This ensures that if less than 4 colours are passed in. The default colour is used for the other vertices.
				if (currentColourIndex < split.length)
					colours[currentColourIndex] = ColourHolder.decode(split[currentColourIndex]);
				else
					colours[currentColourIndex] = baseColour;
		} else
		{
			Arrays.fill(colours, ColourHolder.decode(substring));
		}
	}

	private static float renderChar(CustomFont font, float size, int characterLoc, ColourHolder colour, MatrixStack matrixStack, float x, float y)
	{
		final ColourHolder[] colourHolders = new ColourHolder[4];
		Arrays.fill(colourHolders, colour);
		return renderChar(font, size, characterLoc, colourHolders, matrixStack, x, y);
	}

	private static float renderChar(CustomFont font, float size, int characterLoc, ColourHolder[] vertexColours, MatrixStack matrixStack, float x, float y)
	{
		final GlyphInfo glyph = font.getGlyph(characterLoc - 32);
		RenderUtilV2.setShader(APIShaders.FONT_MASK_TEXTURE.getShader());
		RenderUtilV2.setTextureId(font.getTexture().getID());
		RenderUtilV2.setQuadColourHolder(vertexColours[0]);

		RenderUtilV2.quadTexture(matrixStack,
				x + glyph.x() * size,
				y + glyph.y() * size,
				x + glyph.w() * size,
				y + glyph.h() * size,
				glyph.u0(),
				glyph.v0(),
				glyph.u1(),
				glyph.v1(),
				vertexColours[0],
				vertexColours[1],
				vertexColours[2],
				vertexColours[3]);
		return glyph.xAdvance() * size;

	}

	public static void renderTextWrapped(CustomFont font, float v, MatrixStack matrixStack, String text, float v1, float v2, float width)
	{
		renderTextWrapped(font, v, text, ColourHolder.FULL, false, matrixStack, v1, v2, width);
	}
}
