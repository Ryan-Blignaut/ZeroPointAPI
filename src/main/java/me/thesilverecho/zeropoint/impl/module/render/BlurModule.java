package me.thesilverecho.zeropoint.impl.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "test blur", active = true, keyBinding = GLFW.GLFW_KEY_I)
public class BlurModule extends BaseModule
{
	private Framebuffer fbo1, fbo2;
	public static Framebuffer fbo3;

	@EventListener
	public void renderEvent(RenderWorldEvent.Post event)
	{
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		final int colorAttachment = framebuffer.getColorAttachment();
		if (fbo1 == null || fbo2 == null)
		{
			fbo1 = new Framebuffer();
			fbo2 = new Framebuffer();

		}

		final int width = framebuffer.textureWidth;
		final int height = framebuffer.textureHeight;

		final Shader shader = APIShaders.BLUR_RECTANGLE_SHADER.getShader();
		RenderUtilV2.setShader(shader);
		fbo1.bind();
//		final Shader shader = APIShaders.BLUR_RECTANGLE_SHADER.getShader();
		RenderUtilV2.setShader(shader);
		RenderUtilV2.setTextureId(colorAttachment);
		Matrix4f matrix4f = Matrix4f.projectionMatrix(width, -height, 1000.0F, 3000.0F);
		RenderSystem.setProjectionMatrix(matrix4f);
		RenderUtilV2.setAfterBindTasks(shader1 ->
		{
			shader.setArgument("InSize", new Vec2f(width, height));
			shader.setArgument("OutSize", new Vec2f(width, height));
			shader.setArgument("Radius", 10f);
			shader.setArgument("BlurDir", new Vec2f(1f, 0f));
		});
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);
		fbo1.unbind();
		RenderUtilV2.setAfterBindTasks(shader1 ->
		{
			shader.setArgument("InSize", new Vec2f(width, height));
			shader.setArgument("OutSize", new Vec2f(width, height));
			shader.setArgument("Radius", 10f);
			shader.setArgument("BlurDir", new Vec2f(0f, 1f));
		});
		RenderUtilV2.setTextureId(fbo1.texture);
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);




	}

}