package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.impl.event.ItemRenderEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRenderMixin
{
	@Shadow @Final private ItemRenderer itemRenderer;

	@Inject(method = "render*", at = @At(value = "HEAD"), cancellable = true)
	private void doRender(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci)
	{
		EventManager.call(new ItemRenderEvent(itemEntity, f, g, matrixStack, vertexConsumerProvider, light, this.itemRenderer, ci));
	}
}
