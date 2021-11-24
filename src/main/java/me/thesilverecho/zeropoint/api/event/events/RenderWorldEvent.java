package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.util.math.MatrixStack;

public class RenderWorldEvent
{
	public static record Post(MatrixStack matrix) implements BaseEvent
	{
	}
}
