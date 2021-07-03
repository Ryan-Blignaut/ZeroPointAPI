package me.thesilverecho.zeropoint.impl.event;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record RenderHotbarEvent(MatrixStack matrixStack, float tickDelta, PlayerEntity player,
                                int scaledWidth, int scaledHeight, ItemRenderer itemRenderer,
                                CallbackInfo ci) implements BaseEvent
{
}
