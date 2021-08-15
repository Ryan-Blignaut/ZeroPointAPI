package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderItemEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.layer.ModRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SwordItem;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@ClientModule(name = "Render Layer Test", active = true, keyBinding = GLFW.GLFW_KEY_RIGHT_CONTROL)
public class RenderLayerTest extends BaseModule
{
	public RenderLayerTest(@Nullable Boolean active, @Nullable Integer key)
	{
		super(active, key);
	}

	@EventListener
	public void renderEvent(RenderItemEvent event)
	{
		final VertexConsumerProvider vertexConsumerProvider = event.getVertexConsumerProvider();
		final ItemStack stack = event.getStack();
		final RenderLayer layer = event.getLayer();
		final CallbackInfoReturnable<VertexConsumer> cancelInfo = event.getCancelInfo();
		final boolean glint = event.isGlintEnabled();
		final ModelTransformation.Mode transformation = event.getTransformation();
		if (stack.getItem() instanceof SwordItem)
		{
			final VertexConsumer union = VertexConsumers.union(vertexConsumerProvider.getBuffer(ModRenderLayer.POT_OVERLAY2), vertexConsumerProvider.getBuffer(layer));
			cancelInfo.setReturnValue(union);
		}
		if (glint && stack.getItem() instanceof PotionItem)
			if (transformation == ModelTransformation.Mode.GUI)
				cancelInfo.setReturnValue(VertexConsumers.union(vertexConsumerProvider.getBuffer(ModRenderLayer.POT_OVERLAY), vertexConsumerProvider.getBuffer(layer)));
	}

}
