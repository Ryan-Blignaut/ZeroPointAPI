package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Inject(method = "renderWorld", at = @At("TAIL"))
	private void onRenderWorldPost(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci)
	{
		EventManager.call(new RenderWorldEvent.Post(matrix));
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
}
