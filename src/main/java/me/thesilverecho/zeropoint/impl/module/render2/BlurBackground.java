package me.thesilverecho.zeropoint.impl.module.render2;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.notification.Notification;
import me.thesilverecho.zeropoint.api.notification.NotificationManager;
import me.thesilverecho.zeropoint.api.notification.NotificationType;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Blur background", active = true, keyBinding = GLFW.GLFW_KEY_BACKSLASH)
public class BlurBackground extends BaseModule
{
	private static Framebuffer fbo1, fbo2;
	private boolean shouldRender;

	@EventListener
	public void renderEvent(RenderWorldEvent.Post event)
	{


		if (!shouldRender) return;
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		final int colorAttachment = framebuffer.getColorAttachment();
		if (fbo1 == null || fbo2 == null)
		{
			fbo1 = new Framebuffer();
			fbo2 = new Framebuffer();
		}

		final int width = framebuffer.textureWidth;
		final int height = framebuffer.textureHeight;

		fbo1.bind();
		final Shader shader = APIShaders.GAUSSIAN_BLUR_SHADER.getShader();
		RenderUtilV2.setShader(shader);
		RenderUtilV2.setTextureId(colorAttachment);
		shader.setShaderUniform("Radius", 10f);
		shader.setShaderUniform("BlurDir", new Vec2f(0f, 1f));
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);
		fbo1.unbind();
		//Blur shader is still bound so no need to change shaders.
		//Set the texture to the fbo texture.
		RenderUtilV2.setTextureId(fbo1.texture);
		//Set the after bounds of the shader again to change the direction.
		shader.setShaderUniform("Radius", 10f);
		shader.setShaderUniform("BlurDir", new Vec2f(1f, 0f));
		//Render the final product to the screen.
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);
	}

	@Override
	public void onEnable()
	{
		final Notification n = Notification.Builder.builder("test", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer tempus auctor sapien at cursus. Sed ut dignissim enim. Sed pulvinar nibh ac eros auctor, quis porttitor tortor ultrices. Morbi lacinia turpis ac augue interdum lobortis. Suspendisse potenti.").setType(NotificationType.INFO).setTimeInSeconds(1).build();
		NotificationManager.INSTANCE.addNotification(n);
		super.onEnable();
	}

	@EventListener
	public void onTick(TickEvent.StartTickEvent event)
	{
		final ClientPlayerEntity player = event.client().player;
		if (player != null)
			shouldRender = event.client().currentScreen != null;
	}

	public static Framebuffer getFrameBuffer()
	{
		return fbo1;
	}

}