package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{
	@Inject(at = @At("HEAD"), method = "tick")
	private void onStartTick(CallbackInfo info)
	{
		EventManager.call(new TickEvent.StartTickEvent((MinecraftClient) (Object) this));
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void onEndTick(CallbackInfo info)
	{
		EventManager.call(new TickEvent.EndTickEvent((MinecraftClient) (Object) this));
	}
}
