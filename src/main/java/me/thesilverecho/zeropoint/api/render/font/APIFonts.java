package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.util.Identifier;

public enum APIFonts
{
	COMIC("comic.ttf"), THIN("thin.ttf"), FREE_SANS("free_sans.ttf"), REGULAR("regular.ttf");
	private final CustomFont font;
	private static final String BASE_FONT_PATH = "fonts/";

	APIFonts(String loc)
	{
		font = new CustomFont(new Identifier(ZeroPointClient.MOD_ID, BASE_FONT_PATH + loc));
	}

	public CustomFont getFont()
	{
		return font;
	}
}
