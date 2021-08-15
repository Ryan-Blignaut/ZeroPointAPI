package me.thesilverecho.zeropoint.impl.module.render;


import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.api.event.events.RenderHotbarEvent;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vector4f;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ClientModule(name = "Modern Hotbar", active = true, keyBinding = GLFW.GLFW_KEY_H)
public class ModernHotbar extends BaseModule
{
	public ModernHotbar(@Nullable Boolean active, @Nullable Integer key)
	{
		super(active, key);
	}

	@EventListener
	public void render(RenderHotbarEvent event)
	{
		event.ci().cancel();
		final MatrixStack matrixStack = event.matrixStack();

		RenderUtil.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		final int height = event.scaledHeight();
		final int width = event.scaledWidth();
		RenderUtil.setPostShaderBind(shader ->
		{
			final int i = 1;
			shader.setArgument("u_Thickness", new Vec2f(i, i));
			shader.setArgument("u_InnerRect", new Vector4f(i, height - 23 + i, width - i, height - i));
		});
		RenderUtil.quad(matrixStack, 0, height - 23, width, height, new ColourHolder(55, 55, 55, 80));


		PlayerEntity playerEntity = event.player();

		final PlayerInventory inventory = playerEntity.getInventory();

		final float tempZIndex = RenderUtil.getZIndex();
		RenderUtil.setZIndex(-90);
		final float halfWidth = width / 2f;
		final float x = halfWidth - 93 + inventory.selectedSlot * 20 + 2;
		RenderUtil.roundRect(matrixStack, x, height - 23, halfWidth - 71 + inventory.selectedSlot * 20 + 2, height, 2, new ColourHolder(255, 255, 255, 80));
		RenderUtil.setZIndex(tempZIndex);
		int m = 1;
		for (int slot = 0; slot < 9; slot++)
		{
			int r = (int) (halfWidth - 90 + slot * 20 + 2);
			int s = height - 16 - 3;
			APIFonts.REGULAR.getFont().setFontScale(0.2f).render(matrixStack, "${#DE6E1F}" + (slot + 1), r + 15, s - 3);
			this.renderHotbarItem(event, r, s, inventory.main.get(slot), m++);
		}

		final DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
		final DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		final LocalDateTime now = LocalDateTime.now();

		PlayerListEntry playerListEntry = MC.getNetworkHandler().getPlayerListEntry(MC.player.getUuid());
		String ping = playerListEntry != null ? Integer.toString(playerListEntry.getLatency()) : "0";

		APIFonts.REGULAR.getFont().setFontScale(0.4f).render(matrixStack, time.format(now), width - 25, height - 11.5f - 7.2f);
		APIFonts.REGULAR.getFont().render(matrixStack, date.format(now), width - 35, height - 11.5f);
		final float size = APIFonts.REGULAR.getFont().render(matrixStack, "FPS: " + MC.options.maxFps, 10, height - 14);
		final Vec3d pos = playerEntity.getPos();
		APIFonts.REGULAR.getFont().render(matrixStack, "Ping: ${#388E3C} " + ping, size + 2, height - 14);
		APIFonts.REGULAR.getFont().render(matrixStack, "X: " + (int) pos.getX() + " Y: " + (int) pos.getY() + " Z: " + (int) pos.getZ(), 10, height - 7);
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
