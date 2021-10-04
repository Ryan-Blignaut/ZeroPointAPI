package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.MinecraftClient;

public class TickEvent
{
	public record StartTickEvent(MinecraftClient client) implements BaseEvent
	{
	}

	public record EndTickEvent(MinecraftClient minecraftClient) implements BaseEvent
	{
	}
}
