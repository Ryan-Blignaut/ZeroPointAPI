package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record RenderEntityEvent(ClientWorld world, MatrixStack matrices, float tickDelta, long limitTime,
                                boolean renderBlockOutline,
                                Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager,
                                Matrix4f matrix4f, Frustum frustum, CallbackInfo ci) implements BaseEvent
{
}
