package me.thesilverecho.zeropoint.impl.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld
{
	@Shadow @Final private ClientWorld.Properties clientWorldProperties;
	@Shadow @Final private MinecraftClient client;

	@Shadow public abstract void setTimeOfDay(long timeOfDay);

	private boolean run;

/*	ALWAYS_DAY(0),
	ALWAYS_NIGHT(15000),
	ALWAYS_MIDNIGHT(18000),
	ALWAYS_SUNSET(13150);*/

	@Inject(at = @At("TAIL"), method = "setTimeOfDay", cancellable = true)
	@Environment(EnvType.CLIENT)
	public void setTimeOfDay(long time, CallbackInfo ci)
	{
//		if (client.getCurrentServerEntry() != null)
		{
//			if (!run)
			{
				this.clientWorldProperties.setTimeOfDay(1200);
				run = true;
			}
//			else ci.cancel();

		}
			/*if (TimeChangerClient.TC_CONFIG.custom_time >= 0 && TimeChangerClient.TC_CONFIG.whitelist.isEmpty())
			{
				this.clientWorldProperties.setTimeOfDay(TimeChangerClient.TC_CONFIG.custom_time);
			} else if (TimeChangerClient.TC_CONFIG.custom_time >= 0 && client.getCurrentServerEntry().address != null)
			{
				if (!TimeChangerClient.TC_CONFIG.blacklist && TimeChangerClient.TC_CONFIG.whitelist.contains(client.getCurrentServerEntry().address))
				{
					this.clientWorldProperties.setTimeOfDay(TimeChangerClient.TC_CONFIG.custom_time);
				} else if (TimeChangerClient.TC_CONFIG.blacklist && !TimeChangerClient.TC_CONFIG.whitelist.contains(client.getCurrentServerEntry().address))
				{
					this.clientWorldProperties.setTimeOfDay(TimeChangerClient.TC_CONFIG.custom_time);
				} else
				{
					ci.cancel();
				}
			} else
			{
				ci.cancel();
			}
		} else
		{
			ci.cancel();
		}*/
	}

}
