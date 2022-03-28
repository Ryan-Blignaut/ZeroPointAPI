package me.thesilverecho.zeropoint.api.uiv2;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
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

	private int imageBackground = -1;
	private boolean renderBackground = true;

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		if (renderBackground)
			if (imageBackground != -1)
				RenderUtilV2.rectangleTexture(matrixStack, x, y, w, h, imageBackground, ColourHolder.FULL);
			else
				RenderUtilV2.rectangle(matrixStack, x, y, w, h, getBackground());
		components.forEach(component -> component.render(matrixStack, mouseX, mouseY, delta));

	}

	public void addComponent(Component c)
	{
		components.add(c);
	}

	public Pane setRenderBackground(boolean renderBackground)
	{
		this.renderBackground = renderBackground;
		return this;
	}

	public Pane setImageBackground(int imageBackground)
	{
		this.imageBackground = imageBackground;
		return this;
	}
}
