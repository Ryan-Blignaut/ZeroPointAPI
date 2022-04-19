package me.thesilverecho.zeropoint.api.ui.widgets;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.MouseEvent;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class ButtonComponent extends IntractableComponent
{
	private final APIColour APIColour;
	private final String text;
	private boolean resizeFromText;
	private final float padding;
	private CustomFont font = APIFonts.REGULAR.getFont();
	private Runnable clickTask;

	public ButtonComponent(int x, int y, int w, int h, APIColour APIColour, String text, Component2D parent)
	{
		super(x, y, w, h, parent);
		padding = 3;
		this.APIColour = APIColour;
		this.text = text;
		calculateSize();
		EventManager.register(this);
	}

	@Override
	protected void onCalculateSize()
	{
		if (resizeFromText)
		{
			w = font.getWidth(text);
			h = font.getHeight();
		}
		w += padding;
		h += padding;
		x -= padding;
		y -= padding;
	}

	@Override
	public void renderComp(MatrixStack matrices, float mouseX, float mouseY, double delta)
	{
		RenderUtilV2.roundRect(matrices, x, y, x + w, y + w, 3, APIColour);
		FontRenderer.renderText(font, matrices, text, false, x + padding, y + padding);
	}

	public ButtonComponent setResizeFromText(boolean resizeFromText)
	{
		this.resizeFromText = resizeFromText;
		return this;
	}

	public ButtonComponent setFont(CustomFont font)
	{
		this.font = font;
		return this;
	}

	public ButtonComponent setClickTask(Runnable clickTask)
	{
		this.clickTask = clickTask;
		return this;
	}

	@EventListener
	public boolean mouseClicked(MouseEvent event)
	{
		if (event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT)
		{
			if (clickTask != null)
				clickTask.run();
		}
		return super.mouseClicked(event.x(), event.y(), event.button());
	}

}
