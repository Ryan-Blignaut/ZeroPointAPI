package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;

public record ScreenKeyboardEvent(int keyCode, int scanCode, int modifiers) implements BaseEvent
{
}
