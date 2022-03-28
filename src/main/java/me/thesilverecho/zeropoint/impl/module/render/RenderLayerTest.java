package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderItemEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Render Layer Test", active = true, keyBinding = GLFW.GLFW_KEY_RIGHT_CONTROL)
public class RenderLayerTest extends BaseModule
{
	@EventListener
	public void renderEvent(RenderItemEvent event)
	{
		/*final VertexConsumerProvider vertexConsumerProvider = event.getVertexConsumerProvider();
		final ItemStack stack = event.getStack();
		final RenderLayer layer = event.getLayer();
		final CallbackInfoReturnable<VertexConsumer> cancelInfo = event.getCancelInfo();
		final boolean glint = event.isGlintEnabled();
		final ModelTransformation.Mode transformation = event.getTransformation();
		if (stack.getItem() instanceof SwordItem)
		{
			final VertexConsumer union = VertexConsumers.union(vertexConsumerProvider.getBuffer(ModRenderLayer.DG), vertexConsumerProvider.getBuffer(layer));
			cancelInfo.setReturnValue(union);
		}*/
//		if (glint && stack.getItem() instanceof PotionItem)
//			if (transformation == ModelTransformation.Mode.GUI)
//				cancelInfo.setReturnValue(VertexConsumers.union(vertexConsumerProvider.getBuffer(ModRenderLayer.POT_OVERLAY), vertexConsumerProvider.getBuffer(layer)));
	}

}
