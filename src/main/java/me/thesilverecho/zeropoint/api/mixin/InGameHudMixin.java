package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
	@Shadow
	protected abstract PlayerEntity getCameraPlayer();

	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;
	@Shadow @Final private ItemRenderer itemRenderer;

	@Inject(method = "renderCrosshair", at = @At(value = "HEAD"), cancellable = true)
	private void customCrossHair(MatrixStack matrixStack, CallbackInfo ci)
	{
		EventManager.call(new RenderCrosshairEvent(matrixStack, ci));
	}

	@Inject(method = "renderScoreboardSidebar", at = @At(value = "HEAD"), cancellable = true)
	private void renderScoreboardSidebar(MatrixStack matrixStack, ScoreboardObjective scoreboardObjective, CallbackInfo ci)
	{
		EventManager.call(new RenderUIEvent.ScoreBoard(matrixStack, scoreboardObjective, ci));

		EventManager.call(new RenderScoreboardEvent(matrixStack, scoreboardObjective, ci));
	}

	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	private void onDraw1(MatrixStack matrixStack, float tickDelta, CallbackInfo ci)
	{
		if (MinecraftClient.getInstance().options.debugEnabled)
			return;
		EventManager.call(new Render2dEvent(matrixStack, tickDelta, scaledWidth, scaledHeight, ci));
	}

	@Inject(method = "renderHotbar", at = @At(value = "HEAD"), cancellable = true)
	private void renderHotbar(float tickDelta, MatrixStack matrixStack, CallbackInfo ci)
	{
		EventManager.call(new RenderHotbarEvent(matrixStack, tickDelta, this.getCameraPlayer(), scaledWidth, scaledHeight, itemRenderer, ci));
	}

}
