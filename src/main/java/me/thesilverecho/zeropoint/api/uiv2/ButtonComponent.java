package me.thesilverecho.zeropoint.api.uiv2;

import net.minecraft.client.util.math.MatrixStack;

public class ButtonComponent extends IntractableComponent
{
	private Runnable onClickEvent;

	public ButtonComponent(float x, float y, float w, float h, Runnable onClickEvent)
	{
		super(x, y, w, h);
		this.onClickEvent = onClickEvent;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{

	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
