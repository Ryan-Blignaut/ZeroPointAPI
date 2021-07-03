package me.thesilverecho.zeropoint.impl.event;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record RenderScoreboardEvent(MatrixStack matrixStack, ScoreboardObjective scoreboardObjective, CallbackInfo ci) implements BaseEvent
{
}
