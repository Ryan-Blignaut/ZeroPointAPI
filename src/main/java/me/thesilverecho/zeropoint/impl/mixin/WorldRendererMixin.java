package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin
{
	@Shadow @Final private MinecraftClient client;

	/*@Inject(method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I", at = @At("HEAD"))
	private static void getLightmapCoordinatesModifySkyLight(BlockRenderView world, BlockState state, BlockPos pos, CallbackInfoReturnable<Integer> cir)
	{
		cir.setReturnValue(16);
	}
*/
	@Inject(method = "render", at = @At("HEAD"))
	private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci)
	{

	}

	@Inject(method = "render", at = @At("TAIL"))
	private void onRenderEnd(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci)
	{
//		EventManager.call(new RenderWorldEvent.Post(null, this.client.getFramebuffer()));
	}
/*	@Inject(method = "renderEntity", at = @At(value = "LOAD",target = "Lnet/minecraft/world"))
	private void onRenderEnd(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci)
	{
	}*/

	@Inject(method = "render", at = @At(value = "CONSTANT", args = "stringValue=entities", ordinal = 0)
	)
	private void firePreRenderEntities(MatrixStack matrix, float tickDelta, long time, boolean renderBlockOutline, Camera camera, GameRenderer renderer, LightmapTextureManager lmTexManager, Matrix4f matrix4f, CallbackInfo ci)
	{
//		EntitiesPreRenderCallback.EVENT.invoker().beforeEntitiesRender(camera, frustum, tickDelta);
	}

	@Inject(
			method = "render",
			slice = @Slice(from = @At(value = "FIELD:LAST", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/render/WorldRenderer;transparencyShader:Lnet/minecraft/client/gl/ShaderEffect;")),
			at = {
					@At(value = "INVOKE", target = "Lnet/minecraft/client/gl/ShaderEffect;render(F)V"),
					@At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V", ordinal = 1, shift = At.Shift.AFTER)
			}
	)
	private void hookPostWorldRender(MatrixStack matrices, float tickDelta, long nanoTime, boolean renderBlockOutline, Camera camera, GameRenderer renderer, LightmapTextureManager lmTexManager, Matrix4f matrix4f, CallbackInfo ci)
	{
//		PostWorldRenderCallbackV2.EVENT.invoker().onWorldRendered(matrices, camera, tickDelta, nanoTime);
//		EventManager.call(new RenderWorldEvent.Post(null, this.client.getFramebuffer()));

	}


}
