package me.thesilverecho.zeropoint.impl.event;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class TextRenderEvent
{
	public record RenderStringText(String text, float x, float y, int color, boolean shadow, Matrix4f matrix,
	                               float zIndex, CallbackInfoReturnable<Float> cir) implements BaseEvent
	{
	}

	public record RenderOrderedText(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix,
	                                float zIndex, CallbackInfoReturnable<Float> cir) implements BaseEvent
	{

	}


	public record WidthStringText(String text, CallbackInfoReturnable<Integer> cir) implements BaseEvent
	{
	}

	public record WidthOrderedText(OrderedText text, CallbackInfoReturnable<Integer> cir) implements BaseEvent
	{
	}
}
