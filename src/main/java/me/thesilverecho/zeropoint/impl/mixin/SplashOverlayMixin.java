package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Identifier;
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
//		int scaledWidth = this.client.getWindow().getScaledWidth();
//		int scaledHeight = this.client.getWindow().getScaledHeight();

//		RenderUtil.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
//		RenderUtil.setShaderTexture(new Identifier(ZeroPointClient.MOD_ID, "textures/background.png"));
//		RenderUtil.quadTexture(matrices, 0, 0, scaledWidth, scaledHeight, new ColourHolder(0, 0, 0, 255));
//		final int width = 100;
//		RenderUtil.roundRect(matrices, scaledWidth / 2f - width / 2f, scaledHeight - 50, width, 25, 4, ColourHolder.decode("#263A47"));
//		APIFonts.REGULAR.getFont().render(matrices, "test", 32, 32);


		int scaledWidth = this.client.getWindow().getScaledWidth();
		int scaledHeight = this.client.getWindow().getScaledHeight();
		long l = Util.getMeasuringTimeMs();
		if (this.reloading && this.reloadCompleteTime == -1L)
			this.reloadStartTime = l;

		float f = this.reloadCompleteTime > -1L ? (float) (l - this.reloadCompleteTime) / 1000.0F : -1.0F;
		float g = this.reloadStartTime > -1L ? (float) (l - this.reloadStartTime) / 500.0F : -1.0F;
		float s;
		int m;
		if (f >= 1.0F)
		{
			if (this.client.currentScreen != null)
				this.client.currentScreen.render(matrices, 0, 0, delta);

			m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
//			fill(matrices, 0, 0, i, j, withAlpha(BRAND_ARGB.getAsInt(), m));
			s = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
		} else if (this.reloading)
		{
			if (this.client.currentScreen != null && g < 1.0F)
				this.client.currentScreen.render(matrices, mouseX, mouseY, delta);

			m = MathHelper.ceil(MathHelper.clamp((double) g, 0.15D, 1.0D) * 255.0D);
//			fill(matrices, 0, 0, i, j, withAlpha(BRAND_ARGB.getAsInt(), m));
			s = MathHelper.clamp(g, 0.0F, 1.0F);
		} else
		{
//			m = BRAND_ARGB.getAsInt();
//			float p = (float) (m >> 16 & 255) / 255.0F;
//			float q = (float) (m >> 8 & 255) / 255.0F;
//			float r = (float) (m & 255) / 255.0F;
//			GlStateManager._clearColor(p, q, r, 1.0F);
//			GlStateManager._clear(16384, MinecraftClient.IS_SYSTEM_MAC);
//			s = 1.0F;
		}

		m = (int) ((double) this.client.getWindow().getScaledWidth() * 0.5D);
		int u = (int) ((double) this.client.getWindow().getScaledHeight() * 0.5D);
		double d = Math.min((double) this.client.getWindow().getScaledWidth() * 0.75D, (double) this.client.getWindow().getScaledHeight()) * 0.25D;
		int v = (int) (d * 0.5D);
		double e = d * 4.0D;
		int w = (int) (e * 0.5D);

		RenderUtil.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		RenderUtil.setShaderTexture(new Identifier(ZeroPointClient.MOD_ID, "textures/background.png"));
		RenderUtil.quadTexture(matrices, 0, 0, scaledWidth, scaledHeight, new ColourHolder(0, 0, 0, 255));
		final int width = 100;
		RenderUtil.roundRect(matrices, 32, 32, 64, 64, 3, ColourHolder.decode("#263A47"));
		APIFonts.REGULAR.getFont().getFontLazy().render(matrices, "test", 32, 32);

//		RenderSystem.setShaderTexture(0, LOGO);
//		RenderSystem.enableBlend();
//		RenderSystem.blendEquation(32774);
//		RenderSystem.blendFunc(770, 1);
//		RenderSystem.setShader(GameRenderer::getPositionTexShader);
//		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
//		drawTexture(matrices, m - w, u - v, w, (int) d, -0.0625F, 0.0F, 120, 60, 120, 120);
//		drawTexture(matrices, m, u - v, w, (int) d, 0.0625F, 60.0F, 120, 60, 120, 120);
//		RenderSystem.defaultBlendFunc();
//		RenderSystem.disableBlend();
		int x = (int) ((double) this.client.getWindow().getScaledHeight() * 0.8325D);
		float y = this.reload.getProgress();
		this.progress = MathHelper.clamp(this.progress * 0.95F + y * 0.050000012F, 0.0F, 1.0F);
		if (f < 1.0F)
		{
//			this.renderProgressBar(matrices, i / 2 - w, x - 5, i / 2 + w, x + 5, 1.0F - MathHelper.clamp(f, 0.0F, 1.0F));
		}

		if (f >= 2.0F)
		{
			this.client.setOverlay((Overlay) null);
		}

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
