package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.impl.module.ScreenRender;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen
{

	protected TitleScreenMixin(Text title)
	{
		super(title);


	}

	@Inject(method = "init", at = @At(value = "HEAD"), cancellable = true)
	public void init(CallbackInfo ci)
	{
		if (this.client == null)
			return;

//		try
//		{
//			final GLFWImage malloc = GLFWImage.malloc();
//			final ByteBuffer pixels = Texture2D.readTextBuf(ApiIOUtils.readBytesToBuffer(client.getResourceManager().getResource(new Identifier(ZeroPointClient.MOD_ID, "textures/test.png")).getInputStream()));
//			final ByteBuffer flip = pixels.flip();
//			malloc.set(128, 128, flip);
//			final long l = GLFW.glfwCreateCursor(malloc, 0, 0);
//			GLFW.glfwSetCursor(client.getWindow().getHandle(), l);
//
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}

//		ci.cancel();
	}


	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	public void renderCustom(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci)
	{
		ci.cancel();
		ScreenRender.render(matrixStack, this.width, this.height, mouseX, mouseY, delta);

	}

	@Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
	public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir)
	{
	}


}

