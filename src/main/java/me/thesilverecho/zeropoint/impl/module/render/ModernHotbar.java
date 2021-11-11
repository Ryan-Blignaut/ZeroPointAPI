package me.thesilverecho.zeropoint.impl.module.render;


import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderHotbarEvent;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.GameRenderer;
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
	private String hudCol = "#323232", fpsCol = "";

	@EventListener
	public void render(RenderHotbarEvent event)
	{
		event.ci().cancel();
		final int height = event.scaledHeight();
		final int width = event.scaledWidth();
		final MatrixStack matrixStack = event.matrixStack();

		RenderUtilV2.rectangle(matrixStack, 0, height - 23, width, 23, 0, ColourHolder.decode(hudCol).setAlpha(180));
		PlayerEntity playerEntity = event.player();
		final PlayerInventory inventory = playerEntity.getInventory();


//		RenderUtilV2.quadTexture(matrixStack, 0, 0, 32, 32, new ColourHolder(255, 255, 255, 255));

		final float tempZIndex = RenderUtilV2.getZIndex();
		RenderUtilV2.setZIndex(-90);
		final float halfWidth = width / 2f;
		final float x = halfWidth - 93 + inventory.selectedSlot * 20 + 2;

		this.animationX = (float) RenderUtilV2.getAnimationState(this.animationX, x, Math.max(50.0F, Math.abs(this.animationX - x) * 10.0F));
		RenderUtilV2.roundRect(matrixStack, x, height - 23, -x + halfWidth - 71 + inventory.selectedSlot * 20 + 2, 23, 2, new ColourHolder(255, 255, 255, 80));


		RenderUtilV2.setZIndex(tempZIndex);
		int m = 1;
		for (int slot = 0; slot < 9; slot++)
		{
			int r = (int) (halfWidth - 90 + slot * 20 + 2);
			int s = height - 16 - 3;
			APIFonts.REGULAR.getFont().setFontScale(0.3f).render(matrixStack, "${#DE6E1F}" + (slot + 1), r + 15, s - 3);
			this.renderHotbarItem(event, r, s, inventory.main.get(slot), m++);
		}


		APIFonts.REGULAR.getFont().setFontScale(0.4f).render(matrixStack, time, width - 25, height - 11.5f - 7.2f);
		APIFonts.REGULAR.getFont().render(matrixStack, date, width - 35, height - 11.5f);

		final float size = APIFonts.REGULAR.getFont().render(matrixStack, fps, 10, height - 11.5f - 7.2f);
		APIFonts.REGULAR.getFont().render(matrixStack, ping, size + 2, height - 11.5f - 7.2f);
		APIFonts.REGULAR.getFont().render(matrixStack, pos, 10, height - 10f);
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
			fps = "FPS: " + ((MinecraftClientAccessor) client).getCurrentFps();
			final Vec3d playerPos = player.getPos();
			pos = "X: " + (int) playerPos.getX() + " Y: " + (int) playerPos.getY() + " Z: " + (int) playerPos.getZ();
			final LocalDateTime now = LocalDateTime.now();
			time = DateTimeFormatter.ofPattern("HH:mm").format(now);
			date = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(now);
		}
	}

	private void renderHotbarItem(RenderHotbarEvent event, int x, int y, ItemStack stack, int seed)
	{
		if (!stack.isEmpty())
		{
			MatrixStack matrixStack = RenderSystem.getModelViewStack();
			float f = (float) stack.getCooldown() - event.tickDelta();
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
}
