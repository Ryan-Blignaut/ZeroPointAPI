package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.impl.event.PickupEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin
{
	@Shadow private ClientWorld world;

	@Inject(method = "onItemPickupAnimation", at = @At("HEAD"))
	public void onItemPickupAnimationHook(ItemPickupAnimationS2CPacket packet, CallbackInfo ci)
	{
		final Entity entity = this.world.getEntityById(packet.getEntityId());
		EventManager.call(new PickupEvent(entity));
	}

}

