package me.thesilverecho.zeropoint.api.event.events.render;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public class RenderScreenEvent
{
	public static record PRE(Screen screen, MatrixStack matrices, int mouseX, int mouseY,
	                         float delta) implements BaseEvent
	{

	}

	public static record POST(Screen screen, MatrixStack matrices, int mouseX, int mouseY,
	                          float delta) implements BaseEvent
	{
	}

	public static record TooltipEvent(MatrixStack matrices, List<TooltipComponent> components, int x, int y, int width,
	                                  int height, CallbackInfo ci) implements BaseEvent
	{
		private static ItemStack stack;

		public ItemStack getStack()
		{
			return stack;
		}

		public static void setStack(ItemStack stack)
		{
			TooltipEvent.stack = stack;
		}
	}

}
