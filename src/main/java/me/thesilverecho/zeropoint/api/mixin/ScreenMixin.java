package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.RenderScreenEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin
{
	@Inject(method = "render", at = @At("HEAD"))
	protected void preScreenRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci)
	{
		EventManager.call(new RenderScreenEvent.PRE((Screen) (Object) this, matrices, mouseX, mouseY, delta));
	}

	@Inject(method = "render", at = @At("RETURN"))
	protected void postScreenRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci)
	{
		EventManager.call(new RenderScreenEvent.POST((Screen) (Object) this, matrices, mouseX, mouseY, delta));

	}
}
