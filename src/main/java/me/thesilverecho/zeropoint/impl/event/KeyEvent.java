package me.thesilverecho.zeropoint.impl.event;

import me.thesilverecho.zeropoint.api.event.BaseEvent;

public record KeyEvent(int key, float action, int modifiers) implements BaseEvent
{
}
