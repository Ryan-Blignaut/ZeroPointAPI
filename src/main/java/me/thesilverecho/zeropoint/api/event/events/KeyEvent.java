package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;

public record KeyEvent(int key, float action, int modifiers) implements BaseEvent
{
}
