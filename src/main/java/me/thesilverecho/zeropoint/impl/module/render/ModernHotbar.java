package me.thesilverecho.zeropoint.impl.module.render;


import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.impl.event.RenderHotbarEvent;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Modern Hotbar", active = true, keyBinding = GLFW.GLFW_KEY_F)
public class ModernHotbar extends BaseModule
{

//	@ConfigSetting private int test = 1;
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
