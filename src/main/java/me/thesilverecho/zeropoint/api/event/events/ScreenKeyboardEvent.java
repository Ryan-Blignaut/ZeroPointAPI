package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.util.InputUtil;

public record ScreenKeyboardEvent(int keyCode, int scanCode, int modifiers) implements BaseEvent
{
}
