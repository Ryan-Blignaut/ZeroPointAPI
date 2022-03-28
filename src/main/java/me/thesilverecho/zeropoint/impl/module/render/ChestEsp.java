/*
package me.thesilverecho.zeropoint.impl.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderTileEntityEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import me.thesilverecho.zeropoint.impl.module.SimpleBlockRenderer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Chest ESP", active = true, keyBinding = GLFW.GLFW_KEY_J)
public class ChestEsp extends BaseModule
{
	private Framebuffer fbo1;

	@EventListener
	public void renderEvent(RenderTileEntityEvent event)
	{
		final BlockEntity entity = event.blockEntity();
		if (!(entity instanceof ChestBlockEntity))
			return;
		event.ci().cancel();
		if (fbo1 == null) fbo1 = new Framebuffer();
		fbo1.bind();
		final World world = entity.getWorld();
		final BlockPos pos = entity.getPos();
	*/
/*	world.getBlockState(pos).getOutlineShape(world, pos).forEachBox((minX, minY, minZ, maxX, maxY, maxZ) ->
		{

		});*//*

		SimpleBlockRenderer.renderWithBlockEntity(entity, (float) event.tickDelta(), event.vertexConsumers(), event.matrices());
		fbo1.unbind();
	}

	@EventListener
	public void renderBlockOutline(Render2dEvent.Pre event)
	{
		final Window window = MinecraftClient.getInstance().getWindow();
		if (fbo1 == null)
			fbo1 = new Framebuffer();

		RenderUtilV2.setShader(APIShaders.BLUR_RECTANGLE_SHADER.getShader());
		RenderUtilV2.setTextureId(fbo1.texture.getID());
		RenderUtilV2.postProcessRect(32, 32, 0, 0, 1, 1);
		RenderSystem.disableDepthTest();

		fbo1.clear();
	}
}
*/
