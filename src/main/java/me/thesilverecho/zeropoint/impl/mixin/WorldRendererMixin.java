package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin
{

//	@ModifyVariable(method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I", at = @At(value = "STORE", ordinal = 0))
//	private static int getLightmapCoordinatesModifySkyLight(int sky)
//	{
//		return Math.max(15, sky);
//	}

}
