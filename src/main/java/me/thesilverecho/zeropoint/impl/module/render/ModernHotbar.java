package me.thesilverecho.zeropoint.impl.module.render;


import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.impl.event.RenderHotbarEvent;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ClientModule(name = "Modern Hotbar", active = true, keyBinding = GLFW.GLFW_KEY_F)
public class ModernHotbar extends BaseModule
{

	public ModernHotbar(Boolean active, Integer key)
	{
		super(active, key);
	}

	@EventListener
	public void render(RenderHotbarEvent event)
	{
		event.ci().cancel();
		final MatrixStack matrixStack = event.matrixStack();

		PlayerEntity playerEntity = event.player();
		int m = 1;
		if (playerEntity != null)
		{
			for (int q = 0; q < 9; ++q)
			{
				int r = event.scaledWidth() / 2 - 90 + q * 20 + 2;
				int s = event.scaledHeight() - 16 - 3;
				this.renderHotbarItem(event, r, s, playerEntity.getInventory().main.get(q), m++);
			}
		}
		final DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
		final DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		APIFonts.REGULAR.getFont().render(matrixStack, time.format(now), event.scaledWidth() - 25, event.scaledHeight() - 14, 0.4f);
		APIFonts.REGULAR.getFont().render(matrixStack, date.format(now), event.scaledWidth() - 35, event.scaledHeight() - 7, 0.4f);
		final float size = APIFonts.REGULAR.getFont().render(matrixStack, "FPS: ", 10, event.scaledHeight() - 14, 0.4f);
		if (playerEntity != null)
		{
			final Vec3d pos = playerEntity.getPos();
			APIFonts.REGULAR.getFont().render(matrixStack, "Ping: 20", size + 2, event.scaledHeight() - 14, 0.4f);
			APIFonts.REGULAR.getFont().render(matrixStack, "X: " + (int) pos.getX() + " Y: " + (int) pos.getY() + " Z: " + (int) pos.getZ(), 10, event.scaledHeight() - 7, 0.4f);
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
