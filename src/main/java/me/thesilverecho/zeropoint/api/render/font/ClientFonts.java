package me.thesilverecho.zeropoint.api.render.font;

public enum ClientFonts
{
	COMIC("comic.ttf"), THIN("thin.ttf"), FREE_SANS("free_sans.ttf"), REGULAR("regular.ttf");
	private final String loc;
	private CustomFont font;

	ClientFonts(String loc)
	{
		this.loc = loc;
	}

	public void setFont(CustomFont font)
	{
		this.font = font;
	}

	public String getLoc()
	{
		return loc;
	}

	public CustomFont getFont()
	{
		return font;
	}
}
