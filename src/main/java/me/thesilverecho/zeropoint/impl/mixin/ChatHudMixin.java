package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin
{
	@Shadow
	protected abstract boolean isChatHidden();

	@Shadow
	protected abstract void processMessageQueue();

	@Shadow
	public abstract int getVisibleLineCount();

	@Shadow @Final private List<ChatHudLine<OrderedText>> visibleMessages;

	@Shadow
	protected abstract boolean isChatFocused();

	@Shadow
	public abstract double getChatScale();


	@Shadow @Final private Deque<Text> messageQueue;

	@Shadow @Final private MinecraftClient client;

	@Shadow private int scrolledLines;


	@Shadow
	public abstract int getWidth();

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onChatRenderedStart(MatrixStack matrices, int tickDelta, CallbackInfo ci)
	{
		ci.cancel();

		if (!this.isChatHidden())
		{
			this.processMessageQueue();
			int i = this.getVisibleLineCount();
			int j = this.visibleMessages.size();
			if (j > 0)
			{
				boolean bl = this.isChatFocused();

				float f = (float) this.getChatScale();
				int k = MathHelper.ceil((float) ChatHud.getWidth(this.client.options.chatWidth) / f);

				final CustomFont font = APIFonts.REGULAR.getFont();
				final float fontSize = 0.5f;

				double g = 9.0D * (this.client.options.chatLineSpacing + 1.0D);
				double chatBoxHeight = -8.0D * (this.client.options.chatLineSpacing + 1.0D) + 4.0D * this.client.options.chatLineSpacing;
				if (bl)
				{
					matrices.push();
					matrices.translate(0.0D, (double) (-this.client.getWindow().getScaledHeight() + 48), 0.0D);
					RenderUtilV2.roundRect(matrices, 0, (float) 32, this.getWidth(), this.visibleMessages.size() * FontRenderer.getHeight(font, fontSize), 3, APIColour.decode("#2b2b2b"));
					matrices.pop();
				}
				for (int m = 0; m + this.scrolledLines < this.visibleMessages.size() && m < i; ++m)
				{
					ChatHudLine<OrderedText> chatHudLine = this.visibleMessages.get(m + this.scrolledLines);

					if (chatHudLine != null)
					{
						final int timeAlive = tickDelta - chatHudLine.getCreationTick();
						if (timeAlive < 200 || bl)
						{
							double s = (double) (-m) * FontRenderer.getHeight(font, fontSize) * (this.client.options.chatLineSpacing + 1.0D);
							AtomicReference<String> rText = new AtomicReference<>("");
							chatHudLine.getText().accept((index, style, codePoint) ->
							{
								rText.updateAndGet(v -> v + (char) codePoint);
								return true;
							});
//							RenderUtilV2.roundRect(matrices, 0, (float) (s), k, (float) chatBoxHeight, 3, ColourHolder.FULL);
//							RenderUtilV2.roundRect(matrices, 0, (float) (-100), 121, (float) -chatBoxHeight, 3, ColourHolder.FULL);
							FontRenderer.renderText(font, fontSize, matrices, rText.get(), 0, (float) (s + chatBoxHeight));
						}
					}
				}
			/*	matrices.push();
//				RenderUtilV2.roundRect(matrices, 0, -120, 2000, 2000, 3, ColourHolder.decode("#2b2b2b"));

				matrices.pop();

				int chatHudLine;
				if (!this.messageQueue.isEmpty())
				{
//					m = (int) (128.0D * d);
//					chatHudLine = (int) (255.0D * e);
					matrices.push();
					matrices.translate(0.0D, 0.0D, 50.0D);

//					fill(matrices, -2, 0, k + 4, 9, chatHudLine << 24);
					RenderSystem.enableBlend();
					matrices.translate(0.0D, 0.0D, 50.0D);
//					this.client.textRenderer.drawWithShadow(matrices, (Text) (), 0.0F, 1.0F, 16777215 + (m << 24));
					FontRenderer.renderText(font, matrices, new TranslatableText("chat.queue", this.messageQueue.size()).asString(), 0.0F, 1.0F);
					matrices.pop();
//					RenderSystem.disableBlend();
					RenderSystem.enableBlend();

				}

				matrices.pop();*/
			}
			GLWrapper.enableGL2D();
		}

	}

	@Inject(method = "render", at = @At("TAIL"))
	private void onChatRenderedEnd(MatrixStack matrices, int tickDelta, CallbackInfo ci)
	{
//		matrices.pop();
	}

}
