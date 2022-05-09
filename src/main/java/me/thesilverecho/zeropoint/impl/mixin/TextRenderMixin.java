package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.impl.event.TextRenderEvent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextRenderer.class)
public abstract class TextRenderMixin
{
	@Shadow @Final private static float Z_INDEX;

	@Inject(method = "getWidth(Ljava/lang/String;)I", at = @At(value = "HEAD"), cancellable = true)
	private void customTextWidth(String text, CallbackInfoReturnable<Integer> cir)
	{
		EventManager.call(new TextRenderEvent.WidthStringText(text, cir));
	}

	@Inject(method = "getWidth(Lnet/minecraft/text/StringVisitable;)I", at = @At(value = "HEAD"), cancellable = true)
	private void customTextWidth(StringVisitable text, CallbackInfoReturnable<Integer> cir)
	{
		EventManager.call(new TextRenderEvent.WidthStringText(text.getString(), cir));
	}


	@Inject(method = "getWidth(Lnet/minecraft/text/OrderedText;)I", at = @At(value = "HEAD"), cancellable = true)
	private void customTextWidth(OrderedText text, CallbackInfoReturnable<Integer> cir)
	{
		EventManager.call(new TextRenderEvent.WidthOrderedText(text, cir));
	}

	@Inject(method = "drawLayer(Lnet/minecraft/text/OrderedText;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)F", at = @At(value = "HEAD"), cancellable = true)
	private void drawInternal(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light, CallbackInfoReturnable<Float> cir)
	{
		EventManager.call(new TextRenderEvent.RenderOrderedText(text, x, y, color, shadow, matrix, Z_INDEX, cir));
	}

	@Inject(method = "drawLayer(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)F", at = @At(value = "HEAD"), cancellable = true)
	private void drawInternal(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light, CallbackInfoReturnable<Float> cir)
	{
		EventManager.call(new TextRenderEvent.RenderStringText(text, x, y, color, shadow, matrix, Z_INDEX, cir));
	}


}
