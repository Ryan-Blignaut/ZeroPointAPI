package me.thesilverecho.zeropoint.api.ui.widgets;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;

public class ColourSelector extends Component2D
{
	public ColourSelector(float x, float y, float w, float h, Component2D parent)
	{
		super(x, y, w, h, parent);
	}

	private float mousePos = 0;

	@Override
	public void renderComp(MatrixStack matrices, float mouseX, float mouseY, double delta)
	{
		if (mouseOver)
			mousePos = mouseY;
		RenderUtilV2.colourPicker(matrices, x, y, w, h, mousePos, ColourHolder.FULL);
	}
}
