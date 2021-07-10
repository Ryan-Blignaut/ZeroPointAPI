/*
package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

public class FontRenderer
{
	private static int fontSize;
	private static ColourHolder baseColour;
	private static CustomFont font;
	private static int height;
	private static int outline;


	public int getHeight()
	{
		return height;
	}

	public float getWidth(String string, float scale)
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
				x += glyph.xAdvance() * scale;
			}

		}

		return x;
	}

	public float render(MatrixStack matrixStack, String string, float x, float y)
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
					baseColour = ColourHolder.decode(substring);
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
*/
