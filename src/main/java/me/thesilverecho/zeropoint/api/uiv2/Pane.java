package me.thesilverecho.zeropoint.api.uiv2;

import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class Pane extends Component
{
	//list of components that will be used.
	private final ArrayList<Component> components = new ArrayList<>();

	public Pane(float x, float y, float w, float h)
	{
		super(x, y, w, h);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		components.forEach(component -> component.render(matrixStack, mouseX, mouseY, delta));
	}

	public void addComponent(Component c)
	{
		components.add(c);
	}


}
