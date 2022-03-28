package me.thesilverecho.zeropoint.api.uiv2;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;

public class ButtonComponent extends IntractableComponent
{
	private Runnable onClickEvent;

	private LabelComponent text;

	public ButtonComponent(float x, float y, float w, float h, String text, Runnable onClickEvent)
	{
		super(x, y, w, h);
		this.onClickEvent = onClickEvent;
		this.text = new LabelComponent(x, y, w, h, () -> text);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		this.text.x = x;
		this.text.y = y;
		this.text.w = w;
		this.text.h = h;

//		RenderUtilV2.roundRect(matrixStack, x, y, w, h, 2, getBackground());

//		if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h)
//			RenderUtilV2.roundRect(matrixStack, x, h - 3, w, 3, 2, ColourHolder.FULL);
		if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h)
						RenderUtilV2.roundRect(matrixStack, x, h - 3, w, 3, 2, ColourHolder.FULL);

			RenderUtilV2.roundRect(matrixStack, x, y, w, h, 2, getBackground());


		text.render(matrixStack, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		return super.mouseClicked(mouseX, mouseY, button);
	}

	public ButtonComponent setText(LabelComponent text)
	{
		this.text = text;
		return this;
	}
}
