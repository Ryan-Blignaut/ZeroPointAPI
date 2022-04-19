package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderItemEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.layer.ModRenderLayer;
import me.thesilverecho.zeropoint.impl.event.RenderItemEvent1;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@ClientModule(name = "Render Layer Test", keyBinding = GLFW.GLFW_KEY_RIGHT_CONTROL)
public class RenderLayerTest extends BaseModule
{
	@EventListener
	public void renderEvent(RenderItemEvent event)
	{
		final VertexConsumerProvider vertexConsumerProvider = event.getVertexConsumerProvider();
		final ItemStack stack = event.getStack();
		final RenderLayer layer = event.getLayer();
		final CallbackInfoReturnable<VertexConsumer> cancelInfo = event.getCancelInfo();
		/*if (stack.getItem() instanceof SwordItem)
		{
			final VertexConsumer union = VertexConsumers.union(vertexConsumerProvider.getBuffer(layer), vertexConsumerProvider.getBuffer(RenderLayer.getGlint()));


			cancelInfo.setReturnValue(union);
		}*/

		cancelInfo.setReturnValue(VertexConsumers.union(event.getVertexConsumerProvider().getBuffer(ModRenderLayer.BLUR), event.getVertexConsumerProvider().getBuffer(layer)));

//		cancelInfo.setReturnValue(VertexConsumers.union(event.getVertexConsumerProvider().getBuffer(event.isSolid() ? RenderLayer.getDirectGlint() : RenderLayer.getDirectEntityGlint()), event.getVertexConsumerProvider().getBuffer(layer)));


//		if (glint && stack.getItem() instanceof PotionItem)
//			if (transformation == ModelTransformation.Mode.GUI)
//				cancelInfo.setReturnValue(VertexConsumers.union(vertexConsumerProvider.getBuffer(ModRenderLayer.POT_OVERLAY), vertexConsumerProvider.getBuffer(layer)));
	}


	@EventListener
	public void render3dEvent(RenderItemEvent1 event)
	{
//		if (event.stack.getItem() instanceof SwordItem)
		{
			final VertexConsumer union = VertexConsumers.union(event.vertexConsumerProvider().getBuffer(event.solid() ? RenderLayer.getGlint() : RenderLayer.getEntityGlint()), event.vertexConsumerProvider().getBuffer(event.layer()));
//			final VertexConsumer union = VertexConsumers.union(event.vertexConsumerProvider().getBuffer(RenderLayer.getEntityGlint()), event.vertexConsumerProvider().getBuffer(event.layer()));
			event.cir().setReturnValue(union);
		}
	}

}
