package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record BlockOutlineEvent(MatrixStack matrixStack, VoxelShape voxelShape, float x,
                                float y, float z,
                                CallbackInfo ci) implements BaseEvent
{
}
