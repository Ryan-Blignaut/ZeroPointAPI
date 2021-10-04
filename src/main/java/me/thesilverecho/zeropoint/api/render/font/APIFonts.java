package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.util.Identifier;

public enum APIFonts
{
	COMIC("comic.ttf"),
	FREE_SANS("free_sans.ttf"),
	ICON("zp-icons.ttf"),
	REGULAR("regular.ttf"),
	THIN("thin.ttf");

	private final CustomFont font;
	private static final String BASE_FONT_PATH = "fonts/";

	APIFonts(String loc)
	{
		this.font = new CustomFont(new Identifier(ZeroPointClient.MOD_ID, BASE_FONT_PATH + loc));
	}

	public CustomFont getFont()
	{
		return this.font.getFontLazy();
	}
}
