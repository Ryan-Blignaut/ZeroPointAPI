package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class RenderScreenEvent
{
	public static record PRE(Screen screen, MatrixStack matrices, int mouseX, int mouseY,
	                         float delta) implements BaseEvent
	{

	}

	public static record POST(Screen screen, MatrixStack matrices, int mouseX, int mouseY,
	                          float delta) implements BaseEvent
	{
	}
}
