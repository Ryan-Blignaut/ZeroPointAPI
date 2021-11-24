package me.thesilverecho.zeropoint.api.util;

/**
 * A container that is used to store int colour information to be retrieved later.
 */
public final class ColourHolder
{
	public final static ColourHolder FULL = new ColourHolder(255, 255, 255, 255);
	private final int red;
	private final int green;
	private final int blue;
	private int alpha;

	public ColourHolder(int red, int green, int blue, int alpha)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public ColourHolder(int col)
	{
		this((col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF, ((col >> 24) & 0xFF) == 0 ? 255 : (col >> 24) & 0xFF);
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
		return new ColourHolder(col);
	}

	public ColourHolder setAlpha(int alpha)
	{
		this.alpha = alpha;
		return this;
	}

	public int red() {return red;}

	public int green() {return green;}

	public int blue() {return blue;}

	public int alpha() {return alpha;}

}
