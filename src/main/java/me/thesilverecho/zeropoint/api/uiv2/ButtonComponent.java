package me.thesilverecho.zeropoint.api.uiv2;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.util.math.MatrixStack;

public class ButtonComponent extends IntractableComponent
{
	private final Runnable onClickEvent;
	private LabelComponent text;

	public LabelComponent getText()
	{
		return text;
	}

	public ButtonComponent(float x, float y, float w, float h, String text, Runnable onClickEvent)
	{
		super(x, y, w, h);
		this.onClickEvent = onClickEvent;
		this.text = new LabelComponent(x, y, w, h, () -> text);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		RenderUtilV2.roundRect(matrixStack, x, y, w, h, 2, getBackground());
		if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h)
			RenderUtilV2.roundRect(matrixStack, x, y, w, h, 2, new APIColour(255, 255, 255, 95));
		text.render(matrixStack, mouseX, mouseY, delta);
	}

	@Override
	public boolean onClick(double mouseX, double mouseY, int button)
	{
		if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h)
			onClickEvent.run();
		return super.onClick(mouseX, mouseY, button);
	}

	public ButtonComponent setText(LabelComponent text)
	{
		this.text = text;
		return this;
	}

	@Override
	public void repaint()
	{
		this.text.x = x;
		this.text.y = y;
		this.text.w = w;
		this.text.h = h;
	}
}
