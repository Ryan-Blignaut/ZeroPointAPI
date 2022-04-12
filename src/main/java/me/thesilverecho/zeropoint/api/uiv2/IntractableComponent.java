package me.thesilverecho.zeropoint.api.uiv2;

public abstract class IntractableComponent extends Component
{
	public IntractableComponent(float x, float y, float w, float h)
	{
		super(x, y, w, h);
	}

	public boolean charTyped(char chr, int modifiers)
	{
		return false;
	}

	public boolean onClick(double mouseX, double mouseY, int button)
	{
		return false;
	}

	public void onRelease(double mouseX, double mouseY, int button)
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
