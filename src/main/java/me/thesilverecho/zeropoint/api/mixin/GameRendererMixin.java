package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin
{
	@Shadow @Final private Camera camera;

	@Shadow
	protected abstract double getFov(Camera camera, float tickDelta, boolean changingFov);

	@Shadow
	public abstract Matrix4f getBasicProjectionMatrix(double fov);

	@Shadow
	public abstract void loadProjectionMatrix(Matrix4f projectionMatrix);

	@Shadow
	protected abstract void bobViewWhenHurt(MatrixStack matrices, float tickDelta);

/*	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", shift = AFTER))
	private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci)
	{
		EventManager.call(new RenderWorldEvent.End());
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.BEFORE))
	private void onRender2(float tickDelta, long startTime, boolean tick, CallbackInfo ci)
	{
		EventManager.call(new RenderWorldEvent.End1());
	}*/


	@Shadow @Final private MinecraftClient client;

	@Inject(method = "renderWorld", at = @At("TAIL"/*"RETURN"*/))
	private void onRenderWorldPost(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci)
	{
		EventManager.call(new RenderWorldEvent.Post(matrix, this.client.getFramebuffer()));
//		GL11.glClear(GL11.GL_STENCIL_BITS);
	}

	@Inject(method = "renderWorld", at = @At("HEAD"))
	private void onRenderWorldPre(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci)
	{
		EventManager.call(new RenderWorldEvent.Pre(matrix));
	}

	@Inject(method = "onResized", at = @At("TAIL"))
	private void onRenderWorld(int width, int height, CallbackInfo ci)
	{
//		EventManager.call(new RenderWorldEvent.Resize());
		Framebuffer.resizeAllFBOs();
	}

	@Inject(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = AFTER),
			method = "render"
	)
	private void hookShaderRender(float tickDelta, long nanoTime, boolean renderLevel, CallbackInfo info)
	{
//		EventManager.call(new RenderWorldEvent.Post(null));

	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/render/WorldRenderer.drawEntityOutlinesFramebuffer()V"))
	public void renderForEvent(float float_1, long long_1, boolean boolean_1, CallbackInfo ci)
	{
		EventManager.call(new Render2dEvent.World());
	}


}
