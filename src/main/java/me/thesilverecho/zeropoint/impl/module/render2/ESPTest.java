/*
package me.thesilverecho.zeropoint.impl.module.render2;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderTileEntityEvent;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.notification.Notification;
import me.thesilverecho.zeropoint.api.notification.NotificationManager;
import me.thesilverecho.zeropoint.api.notification.NotificationType;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "ESP", active = false, keyBinding = GLFW.GLFW_KEY_L)
public class ESPTest extends BaseModule
{
	private static Framebuffer fbo1;


*/
/*	@EventListener
	public void renderEnder(RenderWorldEvent.End event)
	{
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		final int width = framebuffer.textureWidth;
		final int height = framebuffer.textureHeight;
	}*//*


	@EventListener
	public void render(RenderWorldEvent.End1 event)
	{
	*/
/*	if (fbo1 == null)
		{
			ZeroPointApiLogger.error("test");
			return;
		}

		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		final int width = framebuffer.textureWidth;
		final int height = framebuffer.textureHeight;

		final Shader shader = APIShaders.RECTANGLE_SHADER.getShader();
		shader.setShaderUniform("Radius", new Vec2f(1, 1));

		final BufferBuilder builder = RenderSystem.renderThreadTesselator().getBuffer();

		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		final ColourHolder f = ColourHolder.FULL;
		final MatrixStack matrixStack = new MatrixStack();
		matrixStack.push();
		matrixStack.loadIdentity();
		final Matrix4f matrix4f = Matrix4f.projectionMatrix(0f, width, 0f, height, -1f, 1000);
		builder.vertex(matrix4f, 0, 0, 1).color(f.red(), f.green(), f.blue(), f.alpha()).next();
		builder.vertex(matrix4f, 0, 1000, 1).color(f.red(), f.green(), f.blue(), f.alpha()).next();
		builder.vertex(matrix4f, 1000, 1000, 1).color(f.red(), f.green(), f.blue(), f.alpha()).next();
		builder.vertex(matrix4f, 1000, 0, 1).color(f.red(), f.green(), f.blue(), f.alpha()).next();
		builder.end();

		shader.bind();

		matrixStack.multiplyPositionMatrix(matrix4f);
		BufferRenderer.postDraw(builder);
		shader.unBind();
		final int textureFromLocation = RenderUtilV2.getTextureFromLocation(new Identifier(ZeroPointClient.MOD_ID, "textures/zero-point_background-1080p.png"));
		RenderUtilV2.rectangleTexture(matrixStack, 0, 0, 100, 100, textureFromLocation, ColourHolder.decode("#2b2b2b"));
		matrixStack.pop();


		fbo1.clear();*//*

	}

	@EventListener
	public void render(RenderWorldEvent.End event)
	{
		if (fbo1 == null)
		{
			ZeroPointApiLogger.error("test");
			return;
		}
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		final int width = framebuffer.textureWidth;
		final int height = framebuffer.textureHeight;

		final Shader shader = APIShaders.RECTANGLE_SHADER.getShader();
		shader.setShaderUniform("radius", 1f);

		final BufferBuilder builder = RenderSystem.renderThreadTesselator().getBuffer();

		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		final ColourHolder f = ColourHolder.FULL;
		final Matrix4f matrix4f = Matrix4f.projectionMatrix(0f, width, 0f, height, -1f, 1000);
		builder.vertex(matrix4f, 0, 0, 1).color(f.red(), f.green(), f.blue(), f.alpha()).next();
		builder.vertex(matrix4f, 0, height, 1).color(f.red(), f.green(), f.blue(), f.alpha()).next();
		builder.vertex(matrix4f, width, height, 1).color(f.red(), f.green(), f.blue(), f.alpha()).next();
		builder.vertex(matrix4f, width, 0, 1).color(f.red(), f.green(), f.blue(), f.alpha()).next();
		builder.end();

		shader.bind();
		BufferRenderer.postDraw(builder);
		shader.unBind();

		fbo1.clear();
	}

	@EventListener
	public void render(Render2dEvent.Pre event)
	{
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		final int width = framebuffer.textureWidth;
		final int height = framebuffer.textureHeight;
		if (fbo1 == null)
		{
			ZeroPointApiLogger.error("error no fbo (this would be the first time)");
			return;
		}
		RenderUtilV2.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		RenderUtilV2.setTextureId(fbo1.texture.getID());
		RenderUtilV2.setShaderUniform("Radius", new Vec2f(1, 0));
		RenderUtilV2.quadTexture(event.matrixStack(), 0, 0, width, height, ColourHolder.FULL);
//		fbo1.clear();
		final int textureFromLocation = RenderUtilV2.getTextureFromLocation(new Identifier(ZeroPointClient.MOD_ID, "textures/zero-point_background-1080p.png"));
		RenderUtilV2.rectangleTexture(event.matrixStack(), 0, 0, 100, 100, fbo1.texture.getID(), ColourHolder.decode("#2b2b2b"));
//		RenderUtilV2.quadTexture(event.matrixStack(), 0, 0, width, height, ColourHolder.FULL);
		fbo1.clear();
	}

	@EventListener
	public void renderTileToFBO(RenderTileEntityEvent event)
	{
		//Get the tile.
		final BlockEntity entity = event.blockEntity();
		//If chest good to go.
		if (!(entity instanceof ChestBlockEntity))
			return;
		event.ci().cancel();

		//Null check on the fbo.
		if (fbo1 == null) fbo1 = new Framebuffer();

		//Render tile to the fbo.
		fbo1.bind();
		event.renderer().render(entity, event.tickDelta(), event.matrices(), event.vertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV);
		fbo1.unbind();

	}

	@Override
	public void onEnable()
	{
		final Notification n = Notification.Builder.builder("Enabled ESP TEST Module", "").setType(NotificationType.INFO).setTimeInSeconds(1).build();
		NotificationManager.INSTANCE.addNotification(n);
		super.onEnable();
	}

	@Override
	public void onDisable()
	{
		final Notification n = Notification.Builder.builder("Disabled ESP TEST Module", "").setType(NotificationType.SUCCESS).setTimeInSeconds(1).build();
		NotificationManager.INSTANCE.addNotification(n);
		super.onEnable();
	}

}*/
