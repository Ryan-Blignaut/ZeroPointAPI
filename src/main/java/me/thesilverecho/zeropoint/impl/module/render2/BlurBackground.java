package me.thesilverecho.zeropoint.impl.module.render2;

import me.thesilverecho.zeropoint.api.config.selector.BooleanHolder;
import me.thesilverecho.zeropoint.api.config.selector.FloatSliderHolder;
import me.thesilverecho.zeropoint.api.config.selector.SettingHolder;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static me.thesilverecho.zeropoint.impl.module.render3.BlockEntityESP.calculateGaussianValue;

@ClientModule(name = "Blur elements", active = true)
public class BlurBackground extends BaseModule
{

	private static Framebuffer blurMask;
	private Framebuffer pingFbo, pongFbo, bloomFbo;

	public static Framebuffer getBlurMask()
	{
		return blurMask;
	}

	public static Runnable r;

	private final SettingHolder<Float> blurRadius = settingHolders.addItem(new FloatSliderHolder("Blur Radius", "The quality of blur(higher ranges will negatively affect performance)", 10f, 2f, 20f, 1f));
	private final SettingHolder<Boolean> backgroundBlur = settingHolders.addItem(new BooleanHolder("Blur Background", "Blur the background of GUIS", true));

	private final SettingHolder<Boolean> bloom = settingHolders.addItem(new BooleanHolder("Bloom", "Adds outlines around blurred elements", true));
	private final SettingHolder<Float> bloomRadius = settingHolders.addItem(new FloatSliderHolder("Bloom Radius", "The quality of bloom(higher ranges will negatively affect performance)", 10f, 2f, 20f, 1f));
	private final SettingHolder<Float> bloomOffset = settingHolders.addItem(new FloatSliderHolder("Bloom offset", "Space between bloom", 2f, 1f, 7f, 1f));

	private boolean shouldRender;
	private FloatBuffer guassianWeights;


	@EventListener(priority = -2)
	public void renderEvent(RenderWorldEvent.Post event)
	{
		if (blurMask == null || this.pingFbo == null || this.pongFbo == null || this.bloomFbo == null)
		{
			blurMask = new Framebuffer();
			this.pingFbo = new Framebuffer();
			this.pongFbo = new Framebuffer();
			this.bloomFbo = new Framebuffer();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		GL11.glEnable(GL11.GL_BLEND);
//		GL45.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

		final int screenWidth = event.framebuffer().viewportWidth;
		final int screenHeight = event.framebuffer().viewportHeight;

		this.pingFbo.bind();
		RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", blurRadius.getValue());
		RenderUtilV2.setTextureId(event.framebuffer().getColorAttachment());
		RenderUtilV2.postProcessRect(screenWidth, screenHeight);
		this.pingFbo.unbind();

//		Vertical blur

		if (!shouldRender && backgroundBlur.getValue()) this.pongFbo.bind();
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(0, 1));
		RenderUtilV2.setTextureId(this.pingFbo.texture.getID());
		RenderUtilV2.postProcessRect(screenWidth, screenHeight);
		if (shouldRender && backgroundBlur.getValue()) return;
		this.pongFbo.unbind();
		this.pingFbo.clear();
//		Remove parts of the screen that are not blurred using the mask fbo.
		RenderUtilV2.setShader(APIShaders.COMPOSITE.getShader());
		GLWrapper.activateTexture(1, blurMask.texture.getID());
		RenderUtilV2.setShaderUniform("Sampler1", 1);
		RenderUtilV2.setTextureId(this.pongFbo.texture.getID());
		RenderUtilV2.postProcessRect(screenWidth, screenHeight);


//		This should be improved at some stage but the idea is that the bloom will affect the same are of the screen as the blur area.
//		The reason that this is not just using the blur mask is because the blur mask has transparency.
		if (bloom.getValue())
		{
			bloomFbo.bind();
			RenderUtilV2.postProcessRect(screenWidth, screenHeight);
			bloomFbo.unbind();
		}

//		Clear ping and pong frame buffers as they will be used again.
		this.pingFbo.clear();
		this.pongFbo.clear();
//		Bloom
		if (bloom.getValue())
		{
//          Re-enable blending
			GL11.glEnable(GL11.GL_BLEND);

//          Horizontal bloom
			pingFbo.bind();
			RenderUtilV2.setShader(APIShaders.BLOOM.getShader());
			GLWrapper.activateTexture(1, bloomFbo.texture.getID());
			RenderUtilV2.setShaderUniform("Sampler1", 1);
			RenderUtilV2.setTextureId(bloomFbo.texture.getID());
			RenderUtilV2.setShaderUniform("Direction", new Vec2f(bloomOffset.getValue(), 0));
			RenderUtilV2.setShaderUniform("Radius", bloomRadius.getValue());

			if (guassianWeights == null)
			{
				guassianWeights = BufferUtils.createFloatBuffer(256);
				for (int light = 1; light <= bloomRadius.getValue(); light++)
					guassianWeights.put(calculateGaussianValue(light, bloomRadius.getValue()));
				guassianWeights.rewind();
			}
			RenderUtilV2.setShaderUniform("Weights", guassianWeights);
			RenderUtilV2.postProcessRect(screenWidth, screenHeight);
			pingFbo.unbind();

//          Vertical bloom
			GLWrapper.activateTexture(1, bloomFbo.texture.getID());
			RenderUtilV2.setShaderUniform("Sampler1", 1);

			RenderUtilV2.setTextureId(pingFbo.texture.getID());
			RenderUtilV2.setShaderUniform("Direction", new Vec2f(0, bloomOffset.getValue()));
			RenderUtilV2.postProcessRect(screenWidth, screenHeight);

		}

		blurMask.clear();
		pingFbo.clear();
		pongFbo.clear();
		bloomFbo.clear();

	}

	@EventListener
	public void onTick(TickEvent.StartTickEvent event)
	{
		final ClientPlayerEntity player = event.client().player;
		if (player != null) shouldRender = event.client().currentScreen != null;
	}


	public static void renderToBlur(Runnable codeToBlur)
	{
		codeToBlur.run();
		if (!ENABLE_MODULES2.containsKey(BlurBackground.class)) return;
		final BlurBackground blurBackground = (BlurBackground) ENABLE_MODULES2.get(BlurBackground.class);

		blurBackground.blurMask.bind();
		codeToBlur.run();
		blurBackground.blurMask.unbind();

	}

}