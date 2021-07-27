package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;

public record ScreenCharEvent(char chr, int modifiers) implements BaseEvent
{
}
