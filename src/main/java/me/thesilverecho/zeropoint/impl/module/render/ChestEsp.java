package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderTileEntityEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Enabled modules", active = true, keyBinding = GLFW.GLFW_KEY_I)
public class ChestEsp extends BaseModule
{
	@EventListener
	public void renderEvent(RenderTileEntityEvent event)
	{
		final BlockEntity entity = event.blockEntity();
		if (!(entity instanceof ChestBlockEntity))
			return;
		event.renderer().render(entity, event.tickDelta(), event.matrices(), event.vertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV);
		event.ci().cancel();
	}

}
