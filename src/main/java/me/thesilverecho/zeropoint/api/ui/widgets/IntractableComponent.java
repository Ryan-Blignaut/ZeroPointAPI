package me.thesilverecho.zeropoint.api.ui.widgets;

public class IntractableComponent extends Component2D
{
	public IntractableComponent(float x, float y, float w, float h, Component2D parent)
	{
		super(x, y, w, h, parent);
	}

	public boolean charTyped(char chr, int modifiers)
	{
		return false;
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		return false;
	}

	public void mouseReleased(double mouseX, double mouseY, int button)
	{
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		return false;
	}

	public void keyReleased(int keyCode, int scanCode, int modifiers)
	{
	}

	public void mouseMoved(double mouseX, double mouseY)
	{
	}
}
