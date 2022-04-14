package me.thesilverecho.zeropoint.impl.event;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record HandItemRenderEvent(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode,
                                  boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                  int light, ItemRenderer itemRenderer, CallbackInfo ci) implements BaseEvent
{
}
