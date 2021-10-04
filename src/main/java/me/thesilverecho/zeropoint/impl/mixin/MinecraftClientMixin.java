package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.InputStream;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setIcon(Ljava/io/InputStream;Ljava/io/InputStream;)V"))
	public void setAlternativeWindowIcon(Window window, InputStream inputStream1, InputStream inputStream2)
	{
		final InputStream x32 = MinecraftClientMixin.class.getResourceAsStream("/assets/zero-point/icons/zp_logo_icon_32.png");
		final InputStream x16 = MinecraftClientMixin.class.getResourceAsStream("/assets/zero-point/icons/zp_logo_icon_16.png");

		window.setIcon(x16, x32);
	}
}
