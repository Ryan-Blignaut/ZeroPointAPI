package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderTileEntityEvent;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

@ClientModule(name = "Enabled modules", active = true, keyBinding = GLFW.GLFW_KEY_M)
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
		glEnable(GL_DEPTH_TEST);
		event.renderer().render(entity, event.tickDelta(), event.matrices(), event.vertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV);
		fbo1.unbind();
	}


	@EventListener
	public void renderEvent(RenderWorldEvent.Pre event)
	{


	/*	final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		final int colorAttachment = framebuffer.getColorAttachment();
		if (fbo1 == null)
		{
			fbo1 = new Framebuffer();
		}*/

	/*	final int width = framebuffer.textureWidth;
		final int height = framebuffer.textureHeight;

		final Shader shader = APIShaders.BLUR_RECTANGLE_SHADER.getShader();
		RenderUtilV2.setShader(shader);
		RenderUtilV2.setShader(shader);
		RenderUtilV2.setTextureId(fbo1.texture);
		Matrix4f matrix4f = Matrix4f.projectionMatrix(width, -height, 1000.0F, 3000.0F);
		RenderSystem.setProjectionMatrix(matrix4f);
		RenderUtilV2.setAfterBindTasks(shader1 ->
		{
			shader.setArgument("InSize", new Vec2f(width, height));
			shader.setArgument("OutSize", new Vec2f(width, height));
			shader.setArgument("Radius", 100f);
			shader.setArgument("BlurDir", new Vec2f(1f, 0f));
		});
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);*/
	}

}
