package me.thesilverecho.zeropoint.api.uiv2;

public class VerticalPane extends Pane
{

	private boolean grow = true;

	public VerticalPane()
	{
		this(0, 0, 0, 0);
		this.grow = true;
	}

	public VerticalPane(float x, float y, float w, float h)
	{
		super(x, y, w, h);
	}

	float vPadding = 10;
	float hPadding = 10;

	@Override
	public void addComponent(Component c)
	{
		super.addComponent(c);
		repaint();
	}

	@Override
	public void repaint()
	{
		float previousHeight = 0;
		if (grow) this.h = 0;

		for (Component c : this.components)
		{
			c.x = hPadding + x;
			c.y = y + previousHeight;
			previousHeight = c.h + c.y;
			if (grow) this.h += previousHeight;
			c.repaint();
		}
	}
}
