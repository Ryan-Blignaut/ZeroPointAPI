package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin
{
	@Inject(method = "render", at = @At("HEAD"))
	private void onChatRenderedStart(MatrixStack matrices, int tickDelta, CallbackInfo ci)
	{
//		matrices.push();
//		matrices.translate(0, 0.1 , 0);

	}

	@Inject(method = "render", at = @At("TAIL"))
	private void onChatRenderedEnd(MatrixStack matrices, int tickDelta, CallbackInfo ci)
	{
//		matrices.pop();
	}

}
