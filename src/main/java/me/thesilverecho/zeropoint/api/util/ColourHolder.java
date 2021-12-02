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

	public static class Quad
	{
		private ColourHolder TopLeft, TopRight, BottomRight, BottomLeft;

		public Quad(ColourHolder TopLeft, ColourHolder TopRight, ColourHolder BottomRight,
				ColourHolder BottomLeft)
		{
			this.TopLeft = TopLeft;
			this.TopRight = TopRight;
			this.BottomRight = BottomRight;
			this.BottomLeft = BottomLeft;
		}

		public Quad setTopLeft(ColourHolder topLeft)
		{
			TopLeft = topLeft;
			return this;
		}

		public Quad setTopRight(ColourHolder topRight)
		{
			TopRight = topRight;
			return this;
		}

		public Quad setBottomRight(ColourHolder bottomRight)
		{
			BottomRight = bottomRight;
			return this;
		}

		public Quad setBottomLeft(ColourHolder bottomLeft)
		{
			BottomLeft = bottomLeft;
			return this;
		}

		public Quad(ColourHolder allColours)
		{
			this(allColours, allColours, allColours, allColours);
		}

		public ColourHolder getTopLeft()
		{
			return TopLeft;
		}

		public ColourHolder getTopRight()
		{
			return TopRight;
		}

		public ColourHolder getBottomRight()
		{
			return BottomRight;
		}

		public ColourHolder getBottomLeft()
		{
			return BottomLeft;
		}
	}

}
