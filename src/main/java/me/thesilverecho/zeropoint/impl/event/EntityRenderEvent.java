package me.thesilverecho.zeropoint.impl.event;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.entity.Entity;

public record EntityRenderEvent(Entity entity) implements BaseEvent
{

}
