package me.thesilverecho.zeropoint.api.ui.widgets;

import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class TextBoxComponent extends IntractableComponent
{
	private final float padding;
	private final CustomFont font = APIFonts.REGULAR.getFont();
	private String promptText = "Enter text";
	private String text = "";
	private int caretPos, selectionStart, selectionEnd;
	private boolean editable = true;

	public TextBoxComponent(float x, float y, float w, float h, Component2D parent, float padding)
	{
		super(x, y, w, h, parent);
		this.padding = padding;
	}

	public TextBoxComponent setDefaultText(String defaultText)
	{
		this.promptText = defaultText;
		return this;
	}

	@Override
	public void renderComp(MatrixStack matrices, float mouseX, float mouseY, double delta)
	{
		//If focused render a cursor
		if (focus)
		{
			//Render cursor blinker
			final float width = font.getWidth(text.substring(0, caretPos));
//			if (delta >= 0.1)
			RenderUtil.rect(matrices, x + width, y + padding, x + width + 3, y + padding + font.getHeight(), ColourHolder.decode("#EDDDBC"));
		}
		//No text show prompt text
		if (text.isEmpty() && !focus)
			font.render(matrices, promptText, x + padding, y + padding);
		else
			font.render(matrices, text, x + padding, y + padding);

		//Change mouse to text icon
		if (mouseOver)
		{

		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
		{
			//Clear text on a right click
			if (!text.isEmpty())
			{
				text = "";
				caretPos = 0;
			}
		} else if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
		{
			caretPos = text.length();
		}

		if (inMouseOver((float) mouseX, (float) mouseY))
			focus = true;
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean charTyped(char chr, int modifiers)
	{
		if (!focus) return false;
		if (caretPos < 0)
			caretPos = 0;
		else if (caretPos > text.length())
			caretPos = text.length();
		text = text.substring(0, caretPos) + chr + text.substring(caretPos);
		caretPos++;
		return super.charTyped(chr, modifiers);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (!focus) return false;
		if (keyCode == GLFW.GLFW_KEY_BACKSPACE)
		{
			if (modifiers == GLFW.GLFW_MOD_CONTROL)
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
		} else if (keyCode == GLFW.GLFW_KEY_LEFT)
		{
			caretPos -= 1;
		} else if (keyCode == GLFW.GLFW_KEY_RIGHT)
		{
			caretPos += 1;
		}
		if (caretPos < 0)
			caretPos = 0;
		else if (caretPos > text.length())
			caretPos = text.length();
		return super.keyPressed(keyCode, scanCode, modifiers);
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
