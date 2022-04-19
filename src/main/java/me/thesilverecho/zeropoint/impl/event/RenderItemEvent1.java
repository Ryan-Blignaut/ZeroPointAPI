package me.thesilverecho.zeropoint.impl.event;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public record RenderItemEvent1(VertexConsumerProvider vertexConsumerProvider, RenderLayer layer, boolean solid,
                               boolean glint, CallbackInfoReturnable<VertexConsumer> cir) implements BaseEvent
{
}
