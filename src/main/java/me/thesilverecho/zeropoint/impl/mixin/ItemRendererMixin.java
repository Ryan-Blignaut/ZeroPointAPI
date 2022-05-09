package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.RenderItemEvent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin
{
	private static final RenderItemEvent RENDER_ITEM_EVENT = new RenderItemEvent();

	@Inject(method = "getDirectItemGlintConsumer", at = @At(value = "HEAD"), cancellable = true)
	private static void render(VertexConsumerProvider vertexConsumerProvider, RenderLayer layer, boolean solid, boolean glint, CallbackInfoReturnable<VertexConsumer> cir)
	{
		EventManager.call(RENDER_ITEM_EVENT.setup(vertexConsumerProvider, layer, solid, glint, cir));
	}


	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "HEAD"), cancellable = true)
	private void render(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci)
	{
		RENDER_ITEM_EVENT.setTransformation(renderMode).setStack(stack);
	}
}
