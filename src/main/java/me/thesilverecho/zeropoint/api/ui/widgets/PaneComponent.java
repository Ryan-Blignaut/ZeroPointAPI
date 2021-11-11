package me.thesilverecho.zeropoint.api.ui.widgets;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.MouseEvent;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class PaneComponent extends Component2D
{
	private final static ArrayList<IntractableComponent> COMPONENTS = new ArrayList<>();
	private IntractableComponent selected;
	private boolean dragging = false;

	public PaneComponent()
	{
		super(100, 100, 100, 100, null);
	}

	public void addComponent(IntractableComponent component2D)
	{
		COMPONENTS.add(component2D);
	}

	public void setSelected(IntractableComponent selected)
	{
		this.selected = selected;
	}

	@EventListener
	public boolean mouseClicked(MouseEvent event)
	{
		COMPONENTS.forEach(component2D -> component2D.mouseClicked(event.x(), event.y(), event.button()));
		return true;
	}


	@Override
	protected void renderComp(MatrixStack matrices, float mouseX, float mouseY, double delta)
	{

	}
}
