package me.thesilverecho.zeropoint.api.render.cursor;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.util.Identifier;

public enum APICursors
{
	DEFAULT("cursor.png"),
	HAND("c.png"),
	TEXT("cursor.png");


	APICursors(String loc)
	{
		this.cursor = new CustomCursor(new Identifier(ZeroPointClient.MOD_ID, BASE_CURSOR_PATH + loc));
	}

	private final CustomCursor cursor;
	private static final String BASE_CURSOR_PATH = "cursors/";

	public CustomCursor getCursor()
	{
		return cursor;
	}
}
