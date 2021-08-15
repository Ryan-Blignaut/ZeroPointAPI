package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface WorldRendererMixin
{
	@Accessor
	Framebuffer getEntityOutlinesFramebuffer();

//	@ModifyVariable(method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I", at = @At(value = "STORE", ordinal = 0))
//	private static int getLightmapCoordinatesModifySkyLight(int sky)
//	{
//		return Math.max(15, sky);
//	}

}
