package me.thesilverecho.zeropoint.impl.module.render3;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderTileEntityEvent;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.*;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

@ClientModule(name = "BlockEntities ESP", active = true, keyBinding = GLFW.GLFW_KEY_RIGHT_ALT)
public class BlockEntityESP extends BaseModule
{
	private Mesh mesh;
	private Framebuffer framebuffer, outlineHorizontal, outlineVertical, glowFramebuffer;
	private MeshVertexConsumerProvider provider;

	@EventListener
	public void renderTileStart(RenderTileEntityEvent.startRenderTile event)
	{
		if (framebuffer == null)
		{
			outlineHorizontal = new Framebuffer();
			outlineVertical = new Framebuffer();
			glowFramebuffer = new Framebuffer();
			framebuffer = new Framebuffer();
			mesh = new Mesh(DrawMode.Triangles, Mesh.Attrib.Vec3, Mesh.Attrib.Color);
			provider = new MeshVertexConsumerProvider(mesh);
		}
		mesh.begin();
	}

	@EventListener
	public void renderTileEnd(RenderTileEntityEvent.endRenderTile event)
	{
		final Shader shader = APIShaders.RECTANGLE_SHADER.getShader();
		RenderSystem.disableDepthTest();
		framebuffer.bind();
		shader.bind();
		mesh.render();
		shader.unBind();
		framebuffer.unbind();

	}

	@EventListener
	public void renderTile(RenderTileEntityEvent.renderTile event)
	{
		final BlockEntity be = event.blockEntity();
		final MatrixStack ms = event.matrices();
		event.renderer().render(be, event.tickDelta(), ms, provider, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
	}


	@EventListener(priority = 1)
	public void renderTest(RenderWorldEvent.Post event)
	{

		if (this.framebuffer == null) return;
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		GL11.glDisable(GL11.GL_DEPTH_TEST);


		final float radius = 6f;
		RenderUtilV2.setShader(APIShaders.OUTLINE0.getShader());
		this.outlineVertical.bind();
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(0, 1));
		RenderUtilV2.setShaderUniform("Radius", radius / 1.5f);
		RenderUtilV2.setShaderUniform("Colour", new Vector4f(1, 1, 1, 1));
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight);
		this.outlineVertical.unbind();

		this.outlineHorizontal.bind();
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(1, 0));
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight);
		this.outlineHorizontal.unbind();

		this.framebuffer.clear();
		this.framebuffer.bind();
		RenderUtilV2.setShader(APIShaders.ADD_EFFECTS.getShader());
		GLWrapper.activateTexture(0, this.outlineHorizontal.texture.getID());
		GLWrapper.activateTexture(1, this.outlineVertical.texture.getID());
		RenderUtilV2.setTextureId(this.outlineHorizontal.texture.getID());
		RenderUtilV2.setShaderUniform("Sampler0", 0);
		RenderUtilV2.setShaderUniform("Sampler1", 1);
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight);
		this.framebuffer.unbind();


		glowFramebuffer.bind();
		RenderUtilV2.setShader(APIShaders.GLOW.getShader());
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", radius);
		RenderUtilV2.setShaderUniform("Colour", new Vector4f(1, 1, 1, 1));
		RenderUtilV2.setShaderUniform("Exposure", (float) (3.5 * 1));
//		TODO: Cache this ->
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(256);
		for (int i = 1; i <= radius; i++) buffer.put(calculateGaussianValue(i, radius / 2));
		buffer.rewind();
//		<-
		RenderUtilV2.setShaderUniform("Weights", buffer);
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight);
		glowFramebuffer.unbind();
		RenderUtilV2.setShader(APIShaders.GLOW.getShader());
		RenderUtilV2.setTextureId(glowFramebuffer.texture.getID());
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(0, 1));
		RenderUtilV2.setShaderUniform("Colour", new Vector4f(0.5f, 1, 1, 1));
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight);

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		RenderSystem.disableBlend();

		this.outlineHorizontal.clear();
		this.outlineVertical.clear();
		this.framebuffer.clear();
		this.glowFramebuffer.clear();

	}

	//	TODO: Move to util class
	public static float calculateGaussianValue(float x, float sigma)
	{
		double PI = 3.141592653;
		double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
		return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
	}

}
