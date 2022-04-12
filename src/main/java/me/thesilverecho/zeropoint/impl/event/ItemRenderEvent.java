package me.thesilverecho.zeropoint.impl.event;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record ItemRenderEvent(ItemEntity itemEntity, float yaw, float tickDelta, MatrixStack matrixStack,
                              VertexConsumerProvider vertexConsumerProvider,
                              int light, ItemRenderer itemRenderer, CallbackInfo ci) implements BaseEvent
{
}
