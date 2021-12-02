package me.thesilverecho.zeropoint.api.uiv2;

public class VerticalPane extends Pane
{
	private float current = 0;

	public VerticalPane(float x, float y, float w, float h)
	{
		super(x, y, w, h);
	}

	float vPadding = 10;
	float hPadding = 10;

	@Override
	public void addComponent(Component c)
	{
		c.x = hPadding + x;
		c.y = y + current;
		current += c.h + 1;
		super.addComponent(c);
	}
}
