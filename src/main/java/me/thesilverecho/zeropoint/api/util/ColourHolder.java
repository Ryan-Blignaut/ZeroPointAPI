package me.thesilverecho.zeropoint.api.util;

/**
 * A container that is used to store int colour information to be retrieved later.
 */
public record ColourHolder(int red, int green, int blue, int alpha)
{
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


	public static ColourHolder gradientColour(ColourHolder colourOne, ColourHolder colourTwo, GradientDirection direction)
	{
		return null;
	}


	public enum GradientDirection
	{
		LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP
	}
}
