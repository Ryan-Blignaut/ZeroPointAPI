package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record Render2dEvent(MatrixStack matrixStack, float tickDelta, float scaledWidth, float scaledHeight,
                            CallbackInfo ci) implements BaseEvent
{
}
