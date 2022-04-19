package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;

public enum APIFonts
{
	COMIC("comic.ttf"),
	FREE_SANS("free_sans.ttf"),
	ICON("zp-icons.ttf"),
	REGULAR("regular.ttf"),
	UD("ud.ttf"),
	THIN("thin.ttf");

	private final CustomFont font;
	private static final String BASE_FONT_PATH = "fonts/";

	APIFonts(String name)
	{
		this.font = new CustomFont(ZeroPointClient.MOD_ID, BASE_FONT_PATH + name);
	}

	public CustomFont getFont()
	{
		return this.font.get();
	}
}
