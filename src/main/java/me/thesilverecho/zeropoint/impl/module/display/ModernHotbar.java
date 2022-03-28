package me.thesilverecho.zeropoint.impl.module.display;


import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.animation.Animate;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ClientModule(name = "Modern Hotbar", active = true, keyBinding = GLFW.GLFW_KEY_H)
public class ModernHotbar extends BaseModule
{
	private float animationX = 0;

	@ConfigSetting
	private final String hudCol = "#323232";
	@ConfigSetting
	private final String fpsCol = "rainbow";
	private final Animate animate = new Animate();

	@EventListener
	public void render(Render2dEvent.RenderHotbar event)
	{
		event.ci().cancel();
		final int height = event.scaledHeight();
		final int width = event.scaledWidth();
		final MatrixStack matrixStack = event.matrixStack();

		final int codeHeight = 24;
		/*if (ZeroPointClient.BLUR)
		{
			final Framebuffer blurFramebuffer = BlurModule.blurFramebuffer;
			if (blurFramebuffer != null)
				RenderUtilV2.rectangleTexture(matrixStack, 0, 0, width, height, 0, 0, 1, 1, blurFramebuffer.texture.getID(), ColourHolder.decode(hudCol).setAlpha(180));
			blurFramebuffer.clear();
		}*/
		final Window w = MinecraftClient.getInstance().getWindow();
//		RenderUtilV2.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
//		RenderUtilV2.setShaderUniform("Radius", new Vec2f(2, 0));
//		RenderUtilV2.setTextureId(fbo2.texture.getID());
//		RenderUtilV2.quadTexture(event.matrixStack(), 0, height - codeHeight, width, height, 1f / w.getScaledWidth() * 0, 1 - 1f / w.getScaledHeight() * (height - codeHeight), 1f / w.getScaledWidth() * (width), 1 - 1f / w.getScaledHeight() * (height), ColourHolder.decode(hudCol).setAlpha(180));
		ScoreBoardHud.blurFBO.bind();
		RenderUtilV2.rectangle(matrixStack, 0, height - codeHeight, width, codeHeight, 0, ColourHolder.decode(hudCol).setAlpha(180));
		ScoreBoardHud.blurFBO.unbind();
		RenderUtilV2.rectangle(matrixStack, 0, height - codeHeight, width, codeHeight, 0, ColourHolder.decode(hudCol).setAlpha(180));


		PlayerEntity playerEntity = event.player();
		final PlayerInventory inventory = playerEntity.getInventory();
		final ItemStack offHandStack = playerEntity.getOffHandStack();


		final float tempZIndex = RenderUtilV2.getZIndex();
		RenderUtilV2.setZIndex(-90);
		final float halfWidth = width / 2f;
		final float x = halfWidth - 93 + inventory.selectedSlot * 20 + 2;

		this.animationX = (float) RenderUtilV2.getAnimationState(this.animationX, x, /*Math.max(50.0F, Math.abs(this.animationX - x)  **/MinecraftClient.getInstance().getTickDelta() *7);
		RenderUtilV2.roundRect(matrixStack, animationX, height - codeHeight, -x + halfWidth - 71 + inventory.selectedSlot * 20 + 2, codeHeight, 2, new ColourHolder(255, 255, 255, 80));
		if (!offHandStack.isEmpty())
			RenderUtilV2.roundRect(matrixStack, halfWidth - 90 - 29, height - codeHeight, 22, codeHeight, 2, new ColourHolder(255, 255, 255, 80));


		RenderUtilV2.setZIndex(tempZIndex);
		int m = 1;
		final CustomFont font = APIFonts.REGULAR.getFont();
		for (int slot = 0; slot < 9; slot++)
		{
			int wPos = (int) (halfWidth - 90 + slot * 20 + 2);
			int hPos = height - 16 - 3;
			FontRenderer.renderText(font, 0.3f, matrixStack, "${#DE6E1F}" + (slot + 1), wPos + 15, hPos - 5);
			this.renderHotbarItem(event, wPos, hPos, inventory.main.get(slot), m++);
		}

		if (!offHandStack.isEmpty())
		{
			final int wPos = (int) (halfWidth - 90 - 26);
			final int hPos = height - 16 - 3;
			this.renderHotbarItem(event, wPos, hPos, offHandStack, m);
			FontRenderer.renderText(font, 0.3f, matrixStack, "${#DE6E1F}Offhand", wPos, hPos - 5);
		}


		FontRenderer.renderText(font, 0.4f, matrixStack, time, width - 25, height - 11.5f - 7.2f);
		FontRenderer.renderText(font, 0.4f, matrixStack, date, width - 35, height - 11.5f);

		final float size = FontRenderer.renderText(font, 0.4f, matrixStack, fps, 10, height - 11.5f - 7.2f);

		FontRenderer.renderText(font, 0.4f, matrixStack, ping, size + 2, height - 11.5f - 7.2f);

		FontRenderer.renderText(font, 0.4f, matrixStack, pos, 10, height - 10f);
	}


	private String ping = "", fps = "", pos = "", date = "", time = "";

	@EventListener
	public void onTick(TickEvent.StartTickEvent event)
	{
		final MinecraftClient client = event.client();
		final ClientPlayerEntity player = client.player;
		if (player != null)
		{
			if (client.getNetworkHandler() != null)
			{
				final PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
				ping = "Ping: ${#388E3C} " + (playerListEntry != null ? Integer.toString(playerListEntry.getLatency()) : "0");
			}
			fps = "FPS: ${" + fpsCol + "}" + ((MinecraftClientAccessor) client).getCurrentFps();
			final Vec3d playerPos = player.getPos();
			pos = "X: " + (int) playerPos.getX() + " Y: " + (int) playerPos.getY() + " Z: " + (int) playerPos.getZ();
			final LocalDateTime now = LocalDateTime.now();
			time = DateTimeFormatter.ofPattern("HH:mm").format(now);
			date = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(now);
		}
	}

	private void renderHotbarItem(Render2dEvent.RenderHotbar event, int x, int y, ItemStack stack, int seed)
	{
		if (!stack.isEmpty())
		{
			MatrixStack matrixStack = RenderSystem.getModelViewStack();
			float f = (float) stack.getBobbingAnimationTime() - event.tickDelta();
			if (f > 0.0F)
			{
				float g = 1.0F + f / 5.0F;
				matrixStack.push();
				matrixStack.translate(x + 8, y + 12, 0.0D);
				matrixStack.scale(1.0F / g, (g + 1.0F) / 2.0F, 1.0F);
				matrixStack.translate(-(x + 8), -(y + 12), 0.0D);
				RenderSystem.applyModelViewMatrix();
			}

			event.itemRenderer().renderInGuiWithOverrides(event.player(), stack, x, y, seed);
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			if (f > 0.0F)
			{
				matrixStack.pop();
				RenderSystem.applyModelViewMatrix();
			}

			event.itemRenderer().renderGuiItemOverlay(MC.textRenderer, stack, x, y);
		}
	}

//	private Framebuffer fbo, fbo2;

/*	@EventListener
	public void render(RenderWorldEvent.Post event)
	{
		if (fbo == null)
		{
			fbo = new Framebuffer();
			fbo2 = new Framebuffer();
		}
		fbo.clear();
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();

		final int textureWidth = framebuffer.textureWidth;
		final int textureHeight = framebuffer.textureHeight;

		GL45.glDisable(GL_DEPTH_TEST);
		fbo.bind();
		RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
		RenderUtilV2.setShaderUniform("TextureSize", new Vec2f(textureWidth, textureHeight));
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", 10f);
		final int colorAttachment = framebuffer.getColorAttachment();
		RenderUtilV2.setTextureId(colorAttachment);
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);
		fbo.unbind();

		fbo2.bind();
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(0, 1));
		RenderUtilV2.setTextureId(fbo.texture.getID());
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);
		fbo2.unbind();

		GL45.glEnable(GL_DEPTH_TEST);
	}*/

}
