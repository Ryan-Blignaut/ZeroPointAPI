package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.MouseEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin
{
	@Shadow private double x;
	@Shadow private double y;
	@Shadow @Final private MinecraftClient client;

	@Inject(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;", shift = At.Shift.BEFORE), cancellable = true)
	private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci)
	{
		double x = this.x * (double) this.client.getWindow().getScaledWidth() / (double) this.client.getWindow().getWidth();
		double y = this.y * (double) this.client.getWindow().getScaledHeight() / (double) this.client.getWindow().getHeight();
		EventManager.call(new MouseEvent(window, button, action, mods, x, y, ci));
	}

//	@Overwrite
//	private void onMouseScroll(long window, double horizontal, double vertical)
//	{
//	}
}
