package me.thesilverecho.zeropoint.api.util;

import java.util.Objects;

/**
 * A container that is used to store int colour information to be retrieved later.
 */
public final class ColourHolder
{
	public final static ColourHolder FULL = new ColourHolder(255, 255, 255, 255);
	private int red, green, blue, alpha;

	public ColourHolder(int red, int green, int blue, int alpha)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	/**
	 * Creates an instance of renderer {@link ColourHolder} using the string provided.
	 *
	 * @param text the {@link String} to decode.
	 * @return an {@link ColourHolder} object holding value of the decoded string.
	 */
	public static ColourHolder decode(String text)
	{
		int col;
		try
		{
			col = Integer.decode(text);
		} catch (NumberFormatException e)
		{
			ZeroPointApiLogger.error("Cant decode number correctly", e);
			col = -1;
		}
		int alpha = (col >> 24) & 0xFF;
		return new ColourHolder((col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF, alpha == 0 ? 255 : alpha);
	}

	public ColourHolder setAlpha(int alpha)
	{
		this.alpha = alpha;
		return this;
	}

	public static ColourHolder gradientColour(ColourHolder colourOne, ColourHolder colourTwo, GradientDirection direction)
	{
		return null;
	}

	public int red() {return red;}

	public int green() {return green;}

	public int blue() {return blue;}

	public int alpha() {return alpha;}


	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ColourHolder) obj;
		return this.red == that.red &&
				this.green == that.green &&
				this.blue == that.blue &&
				this.alpha == that.alpha;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(red, green, blue, alpha);
	}

	@Override
	public String toString()
	{
		return "ColourHolder[" +
				"red=" + red + ", " +
				"green=" + green + ", " +
				"blue=" + blue + ", " +
				"alpha=" + alpha + ']';
	}


	public enum GradientDirection
	{
		LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP
	}
}
