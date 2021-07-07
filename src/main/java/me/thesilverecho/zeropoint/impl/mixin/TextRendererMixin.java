package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.render.font.ClientFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin
{

	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void x(Function<Identifier, FontStorage> fontStorageAccessor, CallbackInfo ci)
	{
		for (ClientFonts value : ClientFonts.values())
		{
			ZeroPointApiLogger.debug("Loading font: " + value.getLoc());
			value.setFont(CustomFont.createCustomFont(new Identifier(ZeroPointClient.MOD_ID, "shaders/" + value.getLoc())));
		}
	}


}
