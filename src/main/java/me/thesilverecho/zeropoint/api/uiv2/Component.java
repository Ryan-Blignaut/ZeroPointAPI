package me.thesilverecho.zeropoint.api.uiv2;

import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Component
{
	private float offsetX, offsetY;

	protected float x, y, w, h;

	private ColourHolder background = ColourHolder.decode("#2b2b2b").setAlpha(20);

	public Component(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}


	public Component setOffsetX(float offsetX)
	{
		this.offsetX = offsetX;
		return this;
	}

	public Component setOffsetY(float offsetY)
	{
		this.offsetY = offsetY;
		return this;
	}

	public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta);

	public ColourHolder getBackground()
	{
		return background;
	}

	public Component setBackground(ColourHolder background)
	{
		this.background = background;
		return this;
	}

}
