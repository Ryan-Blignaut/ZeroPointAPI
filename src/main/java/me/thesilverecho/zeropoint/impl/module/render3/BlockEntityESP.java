package me.thesilverecho.zeropoint.impl.module.render3;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.config.selector.BooleanHolder;
import me.thesilverecho.zeropoint.api.config.selector.ColourHolder;
import me.thesilverecho.zeropoint.api.config.selector.FloatSliderHolder;
import me.thesilverecho.zeropoint.api.config.selector.SettingHolder;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.KeyEvent;
import me.thesilverecho.zeropoint.api.event.events.RenderTileEntityEvent;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.render.Mesh;
import me.thesilverecho.zeropoint.api.render.MeshVertexConsumerProvider;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

@ClientModule(name = "BlockEntities ESP", keyBinding = GLFW.GLFW_KEY_RIGHT_ALT)
public class BlockEntityESP extends BaseModule
{

	public static Framebuffer getFramebuffer()
	{
		if (framebuffer == null)
			framebuffer = new Framebuffer();
		return framebuffer;
	}

	private static Framebuffer framebuffer;
	private Mesh mesh;
	private Framebuffer /*framebuffer,*/ outlineHorizontal, outlineVertical, glowFramebuffer;
	private MeshVertexConsumerProvider provider;

	private final SettingHolder<Float> radius = settingHolders.addItem(new FloatSliderHolder("Outline size", "Controls the size of outline", 6f, 1.0f, 20.0f, 1f));

	private final SettingHolder<Float> sigma = settingHolders.addItem(new FloatSliderHolder("Glow Sigma", "Controls the detail of outline", 3.9f, 1.0f, 10.0f, 0.1f));
	private final SettingHolder<Float> exposure = settingHolders.addItem(new FloatSliderHolder("Glow Exposure", "Controls the brightness of the outline", 1.6f, 1.0f, 3.5f, 0.1f));
	private final SettingHolder<APIColour> glowColour = settingHolders.addItem(new ColourHolder("Glow colour", "Controls the colour of the outline", new APIColour(200, 200, 200, 220)));

	private final SettingHolder<Boolean> renderSolidBehindWalls = settingHolders.addItem(new BooleanHolder("Render walls", "Render Solid Behind Walls", true));

//	TODO: Add option to filter block entities and add way to change there colours.

	@EventListener
	public void renderTileStart(RenderTileEntityEvent.startRenderTile event)
	{
		if (framebuffer == null || glowFramebuffer == null)
		{
			outlineHorizontal = new Framebuffer();
			outlineVertical = new Framebuffer();
			glowFramebuffer = new Framebuffer();
			framebuffer = new Framebuffer();
//			mesh = new Mesh(DrawMode.Triangles, Mesh.Attrib.Vec3, Mesh.Attrib.Color);
//			provider = new MeshVertexConsumerProvider(mesh);
		}
//		mesh.begin();
	}

	@EventListener
	public void renderTileEnd(RenderTileEntityEvent.endRenderTile event)
	{
	/*	final Shader shader = APIShaders.RECTANGLE_SHADER.getShader();
		shader.bind();
		if (renderSolidBehindWalls.getValue())
		{
			provider.setColor(renderSolidBehindWallsColour);
			mesh.render();
		}
		RenderSystem.disableDepthTest();
		framebuffer.bind();
		mesh.render();
		framebuffer.unbind();
		shader.unBind();*/

	}

	@EventListener
	public void renderTile(RenderTileEntityEvent.renderTile event)
	{
//		event.ci().cancel();
		final BlockEntity be = event.blockEntity();
		final MatrixStack ms = event.matrices();

//		event.renderer().render(be, event.tickDelta(), ms, CustomVertexConsumerProvider.INSTANCE/*provider*/, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
	}

	private boolean screenShot = false;

	@EventListener(priority = 1)
	public void onKeu(KeyEvent event)
	{
		if (event.key() == GLFW.GLFW_KEY_LEFT_ALT && event.action() == GLFW.GLFW_PRESS)
		{
			screenShot = true;
		}
	}

	@EventListener(priority = 1)
	public void renderTest(RenderWorldEvent.Post event)
	{

//		MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().draw(ModRenderLayer.CHARMING);

		if (this.framebuffer == null) return;
		final net.minecraft.client.gl.Framebuffer fb = MinecraftClient.getInstance().getFramebuffer();


/*		if (screenShot)
		{

			final ByteBuffer allocate = BufferUtils.createByteBuffer(fb.textureHeight * fb.textureWidth * 4);
//			framebuffer.texture.bindTexture();
			final int textureId = framebuffer.texture.getID();
			GLWrapper.activateTexture(0, textureId);
			glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, allocate);
			STBImageWrite.stbi_write_png("test" + "a" + ".png", fb.textureHeight, fb.textureWidth, 1, allocate, 0);
			screenShot = false;
		}*/

//		RenderSystem.enableBlend();
//		RenderSystem.defaultBlendFunc();
//		this.framebuffer.bind();
//		MC.getBufferBuilders().getEntityVertexConsumers().draw(ModRenderLayer.CHARMING);
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().draw(ModRenderLayer.CHARMING);

/*		RenderSystem.disableBlend();
//		RenderSystem.defaultBlendFunc();


		GL11.glDisable(GL11.GL_DEPTH_TEST);
		RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", 1f);
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID()*//*fb.getColorAttachment()*//*);
		RenderUtilV2.drawInternal(MC.getWindow().getFramebufferWidth(), MC.getWindow().getFramebufferHeight());
		GL11.glEnable(GL11.GL_DEPTH_TEST);

//		this.framebuffer.clear();
//		this.framebuffer.copyDepthFrom(fb);

		if (true) return;*/

/*		RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", 1f);
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(MC.getWindow().getFramebufferWidth(), MC.getWindow().getFramebufferHeight());
		this.framebuffer.clear();

		if (true) return;*/

		RenderUtilV2.setShader(APIShaders.OUTLINE0.getShader());

		this.outlineVertical.bind();
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(0, 1));
		RenderUtilV2.setShaderUniform("Radius", 112f);
		RenderUtilV2.setShaderUniform("Colour", new Vector4f(1, 1, 1, 1));
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(fb.viewportWidth, fb.viewportHeight);
		this.outlineVertical.unbind();
//		this.framebuffer.clear();


		this.outlineHorizontal.bind();
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(1, 0));
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(fb.viewportWidth, fb.viewportHeight);
		this.outlineHorizontal.unbind();
//		this.framebuffer.clear();
		this.framebuffer.clear();
//		if (true) return;
//		this.framebuffer.bind();
		RenderUtilV2.setShader(APIShaders.ADD_EFFECTS.getShader());
		GLWrapper.activateTexture(0, this.outlineHorizontal.texture.getID());
		GLWrapper.activateTexture(1, this.outlineVertical.texture.getID());
		RenderUtilV2.setTextureId(this.outlineHorizontal.texture.getID());
		RenderUtilV2.setShaderUniform("Sampler0", 0);
		RenderUtilV2.setShaderUniform("Sampler1", 1);
		RenderUtilV2.postProcessRect(fb.viewportWidth, fb.viewportHeight);
//		this.framebuffer.unbind();
		if (true) return;

		glowFramebuffer.bind();
		RenderUtilV2.setShader(APIShaders.GLOW.getShader());
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", radius.getValue());
		RenderUtilV2.setShaderUniform("Colour", new Vector4f(1, 1, 1, 1));
		RenderUtilV2.setShaderUniform("Exposure", exposure.getValue());
//		TODO: Cache this ->
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(256);
		for (int i = 1; i <= radius.getValue(); i++)
			buffer.put(calculateGaussianValue(i, sigma.getValue()/*radius / 2*/));
		buffer.rewind();
//		<-
		RenderUtilV2.setShaderUniform("Weights", buffer);
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(fb.viewportWidth, fb.viewportHeight);
		glowFramebuffer.unbind();
		RenderUtilV2.setShader(APIShaders.GLOW.getShader());
		RenderUtilV2.setTextureId(glowFramebuffer.texture.getID());
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(0, 1));
		RenderUtilV2.setShaderUniform("Colour", glowColour.getValue());
		RenderUtilV2.postProcessRect(fb.viewportWidth, fb.viewportHeight);

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
