package me.thesilverecho.zeropoint.api.notification;


import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

public class Notification
{
	private NotificationType type;
	private boolean outlined, blurBackground, hasIcon;
	private String title, text;
	private String icon;

	private float timeInTicks;
	private boolean showing;

	public Notification(NotificationType type, boolean outlined, boolean blurBackground, String title, String text, String icon, float timeInSeconds, boolean showing)
	{
		this.type = type;
		this.outlined = outlined;
		this.blurBackground = blurBackground;
		this.title = title;
		this.text = text;
		this.icon = icon;
		this.timeInTicks = 20 * timeInSeconds;
		this.showing = showing;
	}


	private void setCustomIcon()
	{

	}

	public NotificationType getType()
	{
		return type;
	}

	public String getTitle()
	{
		return title;
	}

	public String getText()
	{
		return text;
	}

	public boolean isShowing()
	{
		return showing;
	}

	public void render(MatrixStack matrixStack)
	{
		final CustomFont font = APIFonts.REGULAR.getFont();
		final Window window = MinecraftClient.getInstance().getWindow();
		float xOffset = 0;
		float yOffset = 25;

		final float fontWidth = FontRenderer.getWidth(font, 0.75f, text);

		float width = Math.min(Math.max(fontWidth, 120), 200);
		float height = FontRenderer.getHeight(font, 0.45f) + FontRenderer.getWrapHeight(font, 0.35f, text.toUpperCase(), width - 28) + 10;
		RenderUtilV2.roundRect(matrixStack, window.getScaledWidth() - xOffset - width, window.getScaledHeight() - yOffset - height, width, height, 3, getType().getColour());
		FontRenderer.renderText(font, 0.45f, matrixStack, title.toUpperCase(), window.getScaledWidth() - xOffset - width + 20, window.getScaledHeight() - yOffset - height + 1);
		final float height1 = FontRenderer.getHeight(font, 0.45f);
		FontRenderer.renderTextWrapped(font, 0.35f, matrixStack, text.toUpperCase(), window.getScaledWidth() - xOffset - width + 20 + 3, window.getScaledHeight() - yOffset - height + height1 + 2, width - 28);

	}

	public void update()
	{
		if (timeInTicks > 0) this.timeInTicks -= 1;
		else showing = false;
	}

	public static class Builder
	{
		private final String title, text;
		private NotificationType type = NotificationType.INFO;
		private boolean outlined = false, blurBackground = false, hasIcon = false;
		private String icon = null;
		private float timeInSeconds = 3f;
		private boolean showing = true;

		public Builder(String title, String text)
		{
			this.title = title;
			this.text = text;
		}

		public static Builder builder(String title, String text)
		{
			return new Builder(title, text);
		}

		public Builder setType(NotificationType type)
		{
			this.type = type;
			return this;
		}

		public Builder setOutlined(boolean outlined)
		{
			this.outlined = outlined;
			return this;
		}

		public Builder setBlurBackground(boolean blurBackground)
		{
			this.blurBackground = blurBackground;
			return this;
		}

		public Builder setHasIcon(boolean hasIcon)
		{
			this.hasIcon = hasIcon;
			return this;
		}

		public Builder setIcon(String icon)
		{
			this.icon = icon;
			return this;
		}

		public Builder setTimeInSeconds(float timeInSeconds)
		{
			this.timeInSeconds = timeInSeconds;
			return this;
		}

		public Builder setShowing(boolean showing)
		{
			this.showing = showing;
			return this;
		}

		public Notification build()
		{
			return new Notification(type, outlined, blurBackground, title, text, icon, timeInSeconds, showing);
		}
	}

}
