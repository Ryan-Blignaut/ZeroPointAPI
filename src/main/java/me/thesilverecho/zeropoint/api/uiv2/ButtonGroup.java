package me.thesilverecho.zeropoint.api.uiv2;

import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

public class ButtonGroup extends IntractableComponent
{
	private final List<ButtonComponent> buttons;

	public ButtonGroup(List<ButtonComponent> buttons)
	{
		super(0, 0, 0, 0);
		this.buttons = buttons;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		for (ButtonComponent button : buttons) button.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean onClick(double mouseX, double mouseY, int mouseButton)
	{
		for (ButtonComponent button : buttons) button.onClick(mouseX, mouseY, mouseButton);
		return true;
	}

	@Override
	public void onRelease(double mouseX, double mouseY, int mouseButton)
	{
		for (ButtonComponent button : buttons) button.onRelease(mouseX, mouseY, mouseButton);
	}

	@Override
	public void repaint()
	{
		for (ButtonComponent button : buttons) button.repaint();
	}

	/*	private void onHover(int mouseX, int mouseY)
	{
		for (ButtonComponent button : buttons)
		{
			button.onHover(mouseX, mouseY);
		}
	}*/

}
