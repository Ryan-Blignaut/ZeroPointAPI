package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin
{
	@Shadow @Final private MinecraftClient client;

	@Shadow @Final private boolean reloading;

	@Shadow private long reloadCompleteTime;

	@Shadow private long reloadStartTime;

	@Shadow @Final private ResourceReload reload;

	@Shadow private float progress;

	@Shadow @Final private Consumer<Optional<Throwable>> exceptionHandler;

	@Inject(method = "init", at = @At(value = "HEAD"), cancellable = true)
	private static void init(CallbackInfo ci)
	{
	}

	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci)
	{
		ci.cancel();
		int scaledWidth = this.client.getWindow().getScaledWidth();
		int scaledHeight = this.client.getWindow().getScaledHeight();
		long l = Util.getMeasuringTimeMs();
		if (this.reloading && this.reloadStartTime == -1L)
			this.reloadStartTime = l;

		float f = this.reloadCompleteTime > -1L ? (float) (l - this.reloadCompleteTime) / 1000.0F : -1.0F;
		float g = this.reloadStartTime > -1L ? (float) (l - this.reloadStartTime) / 500.0F : -1.0F;
		if (this.client.currentScreen != null)
			this.client.currentScreen.render(matrices, 0, 0, delta);
		RenderUtilV2.rectangle(matrices, 0, 0, scaledWidth, scaledHeight, new ColourHolder(50, 50, 50, 255));
		if (this.reloading)
		{
			if (this.client.currentScreen != null && g < 1.0F)
				this.client.currentScreen.render(matrices, mouseX, mouseY, delta);
		} else
			RenderUtilV2.rectangle(matrices, 0, 0, scaledWidth, scaledHeight, new ColourHolder(50, 50, 50, 255));

		double d = Math.min((double) this.client.getWindow().getScaledWidth() * 0.75D, this.client.getWindow().getScaledHeight()) * 0.25D;
		double e = d * 4.0D;
		int w = (int) (e * 0.5D);
		int x = (int) ((double) this.client.getWindow().getScaledHeight() * 0.8325D);
		final float minX = scaledWidth / 2f - w;

		RenderUtilV2.roundRect(matrices, minX, x - 5, w * 2, 10, 3, new ColourHolder(255, 255, 255, 255));
		int i = MathHelper.ceil((w * 2 - 2) * this.progress);
		int height = 7;
		RenderUtilV2.roundRect(matrices, minX + 1, x - height / 2f, i, height, 3, new ColourHolder(0, 75, 85, 255));

		this.progress = MathHelper.clamp(this.progress * 0.95F + this.reload.getProgress() * 0.050000012F, 0.0F, 1.0F);

		if (f >= 2.0F)
			this.client.setOverlay(null);

		if (this.reloadCompleteTime == -1L && this.reload.isComplete() && (!this.reloading || g >= 2.0F))
		{
			try
			{
				this.reload.throwException();
				this.exceptionHandler.accept(Optional.empty());
			} catch (Throwable var23)
			{
				this.exceptionHandler.accept(Optional.of(var23));
			}

			this.reloadCompleteTime = Util.getMeasuringTimeMs();
			if (this.client.currentScreen != null)
			{
				this.client.currentScreen.init(this.client, this.client.getWindow().getScaledWidth(), this.client.getWindow().getScaledHeight());
			}
		}
	}
}
