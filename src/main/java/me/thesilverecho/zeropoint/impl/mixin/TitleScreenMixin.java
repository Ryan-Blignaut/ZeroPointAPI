package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.music.MusicPlayer;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import me.thesilverecho.zeropoint.api.ui.widgets.ButtonComponent;
import me.thesilverecho.zeropoint.api.ui.widgets.PaneComponent;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.screen.TitleScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.gui.screen.TitleScreen.class)
public abstract class TitleScreenMixin extends Screen
{
	Framebuffer framebuffer;

	private final PaneComponent paneComponent = new PaneComponent();

	protected TitleScreenMixin(Text title)
	{
		super(title);
	}

	@Inject(method = "init", at = @At(value = "HEAD"), cancellable = true)
	public void init(CallbackInfo ci)
	{
		TitleScreen.init(this.client, this, this.width, this.height);
		paneComponent.addComponent(new ButtonComponent(0, 0, 100, 100, ColourHolder.FULL, "test", paneComponent));
		this.addDrawableChild(new TexturedButtonWidget(this.width / 2 - 124, 12, 20, 20, 0, 106, 20, ButtonWidget.WIDGETS_TEXTURE, 256, 256, (button) ->
		{
			Shader.resetShaderHashMap();
		}, new TranslatableText("reset")));
		this.addDrawableChild(new TexturedButtonWidget(this.width / 2 - 104, 12, 20, 20, 0, 106, 20, ButtonWidget.WIDGETS_TEXTURE, 256, 256, (button) ->
		{
			MusicPlayer.INSTANCE.play();
		}, new TranslatableText("w")));

		ci.cancel();

	}

	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	public void renderCustom(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci)
	{
		ci.cancel();
		TitleScreen.render(matrixStack, this.width, this.height, mouseX, mouseY, delta);
		super.render(matrixStack, mouseX, mouseY, delta);
	}

	@Inject(method = "mouseClicked", at = @At(value = "HEAD"), cancellable = true)
	public void mouseClickCustom(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir)
	{
		TitleScreen.mouseClick(mouseX, mouseY, button);
		super.mouseClicked(mouseX, mouseY, button);
	}

}

