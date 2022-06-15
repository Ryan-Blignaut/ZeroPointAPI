package me.thesilverecho.zeropoint.api.util;

import java.util.Locale;

/**
 * A container that is used to store int colour information to be retrieved later.
 */
public final class APIColour
{
	public final static APIColour WHITE = new APIColour(255, 255, 255, 255);
	private final int red, green, blue;
	private int alpha;

	public APIColour(int red, int green, int blue, int alpha)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public APIColour(int col)
	{
		this((col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF, ((col >> 24) & 0xFF) == 0 ? 255 : (col >> 24) & 0xFF);
	}

	/**
	 * Creates an instance of renderer {@link APIColour} using the string provided.
	 *
	 * @param text the {@link String} to decode.
	 * @return an {@link APIColour} object holding value of the decoded string.
	 */
	public static APIColour decode(String text)
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
		return new APIColour(col);
	}


	public String encode()
	{
		String rgba;
		try
		{
//			rgba = "#" + Integer.toHexString(this.alpha) + Integer.toHexString(this.red) + Integer.toHexString(this.green) + Integer.toHexString(this.blue) ;
//			rgba = rgba.toUpperCase(Locale.ROOT);
			rgba = String.format("0x%02x%02x%02x", /*this.alpha,*/ this.red, this.green, this.blue).toLowerCase(Locale.ROOT);
		} catch (NumberFormatException e)
		{
			ZeroPointApiLogger.error("Cant encode number correctly", e);
			rgba = "0xffffff";
		}
		return rgba;
	}

	public APIColour setAlpha(int alpha)
	{
		this.alpha = alpha;
		return this;
	}

	public int getRed() {return red;}

	public int getGreen() {return green;}

	public int getBlue() {return blue;}

	public int getAlpha() {return alpha;}

	public int getAsInt()
	{
		return red << 16 | green << 8 | blue | alpha << 24;
	}

	public static class ColourQuad
	{
		private APIColour topLeft, topRight, bottomRight, bottomLeft;

		public ColourQuad(APIColour allColours)
		{
			this(allColours, allColours, allColours, allColours);
		}

		public ColourQuad(APIColour.ColourQuad colour)
		{
			this.topLeft = colour.topLeft;
			this.topRight = colour.topRight;
			this.bottomRight = colour.bottomRight;
			this.bottomLeft = colour.bottomLeft;
		}

		public ColourQuad(APIColour topLeft, APIColour topRight, APIColour bottomRight, APIColour bottomLeft)
		{
			this.topLeft = topLeft;
			this.topRight = topRight;
			this.bottomRight = bottomRight;
			this.bottomLeft = bottomLeft;
		}

		public ColourQuad setTopLeft(APIColour topLeft)
		{
			this.topLeft = topLeft;
			return this;
		}

		public ColourQuad setTopRight(APIColour topRight)
		{
			this.topRight = topRight;
			return this;
		}

		public ColourQuad setBottomRight(APIColour bottomRight)
		{
			this.bottomRight = bottomRight;
			return this;
		}

		public ColourQuad setBottomLeft(APIColour bottomLeft)
		{
			this.bottomLeft = bottomLeft;
			return this;
		}


		public APIColour getTopLeft()
		{
			return topLeft;
		}

		public APIColour getTopRight()
		{
			return topRight;
		}

		public APIColour getBottomRight()
		{
			return bottomRight;
		}

		public APIColour getBottomLeft()
		{
			return bottomLeft;
		}


		public void fill(APIColour colour0, APIColour colour1, APIColour colour2, APIColour colour3)
		{
			topLeft = colour0;
			topRight = colour1;
			bottomRight = colour2;
			bottomLeft = colour3;
		}

		public void fill(APIColour colour0, APIColour colour1, APIColour colour2)
		{
			fill(colour0, colour1, colour2, colour2);
		}

		public void fill(APIColour colour0, APIColour colour1)
		{
			fill(colour0, colour1, colour1, colour1);
		}

		public void fill(APIColour colour0)
		{
			fill(colour0, colour0, colour0, colour0);
		}

	}

}
