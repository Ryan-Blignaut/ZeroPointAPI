package me.thesilverecho.zeropoint.api.ui.widgets;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.MouseEvent;
import me.thesilverecho.zeropoint.api.event.events.ScreenCharEvent;
import me.thesilverecho.zeropoint.api.event.events.ScreenKeyboardEvent;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class TextBoxComponent extends IntractableComponent
{
	private final float padding;
	private final CustomFont font = APIFonts.REGULAR.getFont();
	private String promptText = "Enter text";
	private String text = "";
	private int caretPos, selectionStart, selectionEnd;
	private final boolean editable = true;

	public TextBoxComponent(float x, float y, float w, float h, Component2D parent, float padding)
	{
		super(x, y, w, h, parent);
		this.padding = padding;
		EventManager.register(this);
	}

	public TextBoxComponent setDefaultText(String defaultText)
	{
		this.promptText = defaultText;
		return this;
	}

	@Override
	public void renderComp(MatrixStack matrices, float mouseX, float mouseY, double delta)
	{
		//If focused render renderer cursor
		if (focus)
		{
			//Render cursor blinker
			final float width = font.getWidth(text.substring(0, caretPos));
//			if (delta >= 0.1)
			RenderUtilV2.rectangle(matrices, x + width, y + padding, x + width + 3, y + padding + font.getHeight(), APIColour.decode("#EDDDBC"));
		}
		//No text show prompt text
		if (text.isEmpty() && !focus)
			FontRenderer.renderText(font, matrices, promptText, false, x + padding, y + padding);
		else
			FontRenderer.renderText(font, matrices, text, false, x + padding, y + padding);


		//Change mouse to text icon
		if (mouseOver)
		{

		}
	}

	@EventListener
	public boolean mouseClicked(MouseEvent event)
	{
		if (event.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
		{
			//Clear text on renderer right click
			if (!text.isEmpty())
			{
				text = "";
				caretPos = 0;
			}
		} else if (event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT)
		{
			caretPos = text.length();
		}

		if (inMouseOver((float) event.x(), (float) event.y()))
			focus = true;
		return super.mouseClicked(event.x(), event.y(), event.button());
	}


	@EventListener
	public boolean charTyped(ScreenCharEvent event)
	{
		if (!focus) return false;
		if (caretPos < 0)
			caretPos = 0;
		else if (caretPos > text.length())
			caretPos = text.length();
		text = text.substring(0, caretPos) + event.chr() + text.substring(caretPos);
		caretPos++;
		return super.charTyped(event.chr(), event.modifiers());
	}

	@EventListener
	public boolean keyPressed(ScreenKeyboardEvent event)
	{
		if (!focus) return false;
		if (event.keyCode() == GLFW.GLFW_KEY_BACKSPACE)
		{
			if (event.modifiers() == GLFW.GLFW_MOD_CONTROL)
			{
				int beginIndex = text.lastIndexOf(" ");
				if (beginIndex == -1)
					beginIndex = 0;
				text = text.substring(beginIndex);
				caretPos = beginIndex;
			} else
			{
				if (caretPos - 1 >= 0)
				{
					text = text.substring(0, caretPos - 1) + text.substring(caretPos);
					caretPos -= 1;
				}
			}
		} else if (event.keyCode() == GLFW.GLFW_KEY_LEFT)
		{
			caretPos -= 1;
		} else if (event.keyCode() == GLFW.GLFW_KEY_RIGHT)
		{
			caretPos += 1;
		}
		if (caretPos < 0)
			caretPos = 0;
		else if (caretPos > text.length())
			caretPos = text.length();
		return super.keyPressed(event.keyCode(), event.scanCode(), event.modifiers());
	}

	public synchronized String getSelectedText()
	{
		return text.substring(selectionStart, selectionEnd);
	}

	private void selectAll()
	{
		final int textStart = 0;
		final int textEnd = text.length();
		select(textStart, textEnd);
	}

	private void select(int textStart, int textEnd)
	{
		if (textStart < 0)
			textStart = 0;
		if (textStart > text.length())
			textStart = text.length();

		if (textEnd > text.length())
			textEnd = text.length();
		if (textEnd < selectionStart)
			textEnd = selectionStart;

		this.selectionStart = textStart;
		this.selectionEnd = textEnd;
	}

	private int getCaretPosition()
	{
		int position;
		position = selectionStart;
		int maxPosition = text.length();
		if (position > maxPosition)
			position = maxPosition;
		return position;
	}


}
