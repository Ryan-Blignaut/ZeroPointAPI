package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record MouseEvent(long window, int button,int action, int mods, double x, double y, CallbackInfo ci) implements BaseEvent
{
}
