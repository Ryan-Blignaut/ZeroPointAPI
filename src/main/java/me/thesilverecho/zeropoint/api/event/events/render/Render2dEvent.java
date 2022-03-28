package me.thesilverecho.zeropoint.api.event.events.render;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class Render2dEvent
{

	public static record RenderCrosshair(MatrixStack matrixStack, int scaledWidth, int scaledHeight,
	                                     CallbackInfo ci) implements BaseEvent
	{
	}

	public static record RenderHotbar(MatrixStack matrixStack, float tickDelta, PlayerEntity player, int scaledWidth,
	                                  int scaledHeight, ItemRenderer itemRenderer, CallbackInfo ci) implements BaseEvent
	{
	}

	public static record ScoreBoard(MatrixStack matrixStack, ScoreboardObjective scoreboardObjective,
	                                CallbackInfo ci) implements BaseEvent
	{
	}

	public static record Pre(MatrixStack matrixStack, float tickDelta, float scaledWidth, float scaledHeight,
	                         CallbackInfo ci) implements BaseEvent
	{
	}

	public static record Post(MatrixStack matrixStack, float tickDelta, float scaledWidth, float scaledHeight,
	                          CallbackInfo ci) implements BaseEvent
	{
	}


	public static record World() implements BaseEvent
	{
	}

}
