package me.thesilverecho.zeropoint.api.notification;

import me.thesilverecho.zeropoint.api.util.APIColour;

public enum NotificationType
{
	INFO("#03A9F4"), WARNING("#FF9800"), ERROR("#EF5350"), SUCCESS("#4CAF50");
	private final APIColour colour;

	NotificationType(String colour)
	{
		this.colour = APIColour.decode(colour);
	}

	public APIColour getColour()
	{
		return colour;
	}
}
