package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.BlockOutlineEvent;
import me.thesilverecho.zeropoint.api.event.events.RenderEntityEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
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
	/*	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/Frustum;DDD)Z"))
		private <E extends Entity> boolean shouldRenderRedirect(EntityRenderDispatcher entityRenderDispatcher, E entity, Frustum frustum, double x, double y, double z)
		{
			return true || entityRenderDispatcher.shouldRender(entity, frustum, x, y, z);
		}*/
	@Shadow
	@Nullable
	private Frustum capturedFrustum;

	@Shadow private ClientWorld world;


	@Shadow @Final private MinecraftClient client;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 0))
	private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci)
	{
		EventManager.call(new RenderEntityEvent(world, matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, lightmapTextureManager, matrix4f, this.capturedFrustum, ci));
	}


/*	@Inject(method = "drawShapeOutline", at = @At("HEAD"), cancellable = true)
	private static void drawBlockOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, float r, float g, float b, float a, CallbackInfo ci)
	{
		EventManager.call(new BlockOutlineEvent(matrixStack, voxelShape, x, y, z, ci));
	}*/

	@Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
	private void drawBlockOutline1(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci)
	{
		EventManager.call(new BlockOutlineEvent(matrices, blockState.getOutlineShape(this.world, blockPos, ShapeContext.of(entity)), (float) (blockPos.getX() - d), (float) (blockPos.getY() - e), (float) (blockPos.getZ() - f), ci));
	}

	@Inject(
			method = "render",
			at = @At(value = "CONSTANT", args = "stringValue=translucent", ordinal = 1)
	)
	private void firePostRenderEntities(MatrixStack matrix, float tickDelta, long time, boolean renderBlockOutline, Camera camera, GameRenderer renderer, LightmapTextureManager lmTexManager, Matrix4f matrix4f, CallbackInfo ci)
	{
//		BlockEntityESP.getFramebuffer().clear();
//		BlockEntityESP.getFramebuffer().copyDepthFrom(this.client.getFramebuffer());
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
//		EventManager.call(new RenderWorldEvent.Post(matrices, this.client.getFramebuffer()));
	}

}
