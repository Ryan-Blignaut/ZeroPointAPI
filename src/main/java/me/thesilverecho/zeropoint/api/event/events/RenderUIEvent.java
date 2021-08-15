package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RenderUIEvent
{
	public record ScoreBoard(MatrixStack matrixStack, ScoreboardObjective scoreboardObjective,
	                         CallbackInfo ci) implements BaseEvent
	{
	}
}
