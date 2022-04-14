package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.math.MatrixStack;

public class RenderWorldEvent
{
	public static record Post(MatrixStack matrix, Framebuffer framebuffer) implements BaseEvent
	{
	}

	public static record Pre(MatrixStack matrix) implements BaseEvent
	{
	}

	public static record RenderEntity(MatrixStack matrix) implements BaseEvent
	{
	}

	public static record RenderV3(MatrixStack matrix) implements BaseEvent
	{
	}

	public static record Resize() implements BaseEvent
	{
	}

	public static class End implements BaseEvent
	{
	}
}
