package me.thesilverecho.zeropoint.api.notification;

import me.thesilverecho.zeropoint.api.util.ColourHolder;

public enum NotificationType
{
	INFO("#03A9F4"), WARNING("#FF9800"), ERROR("#EF5350"), SUCCESS("#4CAF50");
	private final ColourHolder colour;

	NotificationType(String colour)
	{
		this.colour = ColourHolder.decode(colour);
	}

	public ColourHolder getColour()
	{
		return colour;
	}
}
