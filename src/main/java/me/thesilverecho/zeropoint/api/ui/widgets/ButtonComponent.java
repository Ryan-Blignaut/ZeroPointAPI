package me.thesilverecho.zeropoint.api.ui.widgets;

import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;

public class ButtonComponent extends IntractableComponent
{
	private final ColourHolder colourHolder;
	private final String text;
	private boolean resizeFromText;
	private final float padding;
	private CustomFont font = APIFonts.REGULAR.getFont();

	public ButtonComponent(int x, int y, int w, int h, ColourHolder colourHolder, String text, Component2D parent)
	{
		super(x, y, w, h, parent);
		padding = 3;
		this.colourHolder = colourHolder;
		this.text = text;
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
		RenderUtil.roundRect(matrices, x, y, x + w, y + w, 3, colourHolder);
		font.render(matrices, text, x + padding, y + padding);
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


}
