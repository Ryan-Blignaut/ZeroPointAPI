package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class FontRenderer2
{
	public static float getHeight(CustomFont font, float size)
	{
		return font.getHeight() * size;
	}


	public static float getWidth(CustomFont font, String string, float size)
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
				if (i1 != -1 && i2 != -1) i += i2 - i1;
			} else x += font.getGlyph(character - 32).xAdvance() * size;

		}
		return x;
	}

	public static float renderText(MatrixStack matrixStack, float x, float y, CustomFont font, float size, String text, APIColour colour)
	{
		return renderText(matrixStack, x, y, font, size, text, colour, false);
	}

	public static float renderText(MatrixStack matrixStack, float x, float y, CustomFont font, float size, String text, APIColour colour, boolean background)
	{
		return renderText(matrixStack, x, y, font, size, text, new APIColour.ColourQuad(colour), background);
	}

	public static float renderText(MatrixStack matrixStack, float x, float y, CustomFont font, float size, String text, APIColour.ColourQuad baseColour, boolean background)
	{
		if (text == null) text = "Error text String is empty.";
		y += font.getAscent() * font.getScale()*size;
//      Loop for each letter of the text.
		final APIColour.ColourQuad colours = new APIColour.ColourQuad(baseColour);
//		Arrays.fill(colours, baseColour);
		for (int index = 0; index < text.length(); index++)
		{
//          Get the integer representation each letter of the text at given index.
			int character = text.charAt(index);
//			If the character is out of bounds replace it with default.
			if (character < 32 || character > 256) character = 32;
//			Look for custom formatting from the string ${format type}.
			if (character == '$' && index + 1 < text.length() && text.charAt(index + 1) == '{')
				index = applyFormatting(character, index, text, baseColour.getTopLeft(), colours, x, y);
			else
			{
				if (background)
					renderChar(font, size, character, new APIColour.ColourQuad(new APIColour(43, 43, 43, 255)), matrixStack, x + 0.5f, y + 0.5f);
				x += renderChar(font, size, character, baseColour, matrixStack, x, y);
			}
		}
//      Reset size and colours;
//		System.out.println(x);
		return x;
	}


	private static int applyFormatting(int character, int position, String text, APIColour baseColour, APIColour.ColourQuad colours, float x, float y)
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
					colours.fill(new APIColour(colour));
				} else applyColour(baseColour, colours, substring);
				position += endOfFormatString - startOfFormatString;
			}
		}
		return position;
	}


	private static void applyColour(APIColour baseColour, APIColour.ColourQuad colours, String substring)
	{
//		Split custom options if possible.
		final String[] split = substring.split(",");
//		Apply colour to all
		if (split.length == 1)
			colours.fill(APIColour.decode(split[0]));
		else if (split.length == 2)
			colours.fill(APIColour.decode(split[0]), APIColour.decode(split[1]));
		else if (split.length == 3)
			colours.fill(APIColour.decode(split[0]), APIColour.decode(split[1]), APIColour.decode(split[2]));
		else if (split.length == 4)
			colours.fill(APIColour.decode(split[0]), APIColour.decode(split[1]), APIColour.decode(split[2]), APIColour.decode(split[3]));

	}


	private static float renderChar(CustomFont font, float size, int characterLoc, APIColour.ColourQuad vertexColours, MatrixStack matrixStack, float x, float y)
	{
		final GlyphInfo glyph = font.getGlyph(characterLoc - 32);
		RenderUtilV2.setShader(APIShaders.FONT_MASK_TEXTURE.getShader());
		RenderUtilV2.setTextureId(font.getTexture().getID());
		RenderUtilV2.setQuadColourHolder(vertexColours);

		final double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
		RenderUtilV2.quadTexture(matrixStack.peek().getPositionMatrix(), x + glyph.x() * size, y + glyph.y() * size, x + glyph.w() * size, y + glyph.h() * size, glyph.u0(), glyph.v0(), glyph.u1(), glyph.v1());
		return glyph.xAdvance() * size;
	}

}
