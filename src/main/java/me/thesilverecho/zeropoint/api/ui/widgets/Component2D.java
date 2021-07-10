package me.thesilverecho.zeropoint.api.ui.widgets;

import net.minecraft.client.util.math.MatrixStack;

public class Component2D
{
	public boolean visible = true;

	protected float x, y, w, h;

	public float minWidth;

	public Component2D parent;
	public boolean mouseOver;
	protected double mouseOverTimer;
	public boolean focus;

	public Component2D(float x, float y, float w, float h, Component2D parent)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.parent = parent;
	}

	public void calculateSize()
	{
		onCalculateSize();
		if (w < minWidth) w = minWidth;
	}

	protected void onCalculateSize()
	{

	}

	public boolean render(MatrixStack matrixStack, float mouseX, float mouseY, double delta)
	{
		if (!visible) return true;

		if (inMouseOver(mouseX, mouseY))
		{
			mouseOverTimer += delta;
			mouseOver = true;
		} else
		{
			mouseOver = false;
			mouseOverTimer = 0;
		}

		renderComp(matrixStack, mouseX, mouseY, delta);
		return false;
	}


	public void renderComp(MatrixStack matrices, float mouseX, float mouseY, double delta)
	{

	}

	public Component2D getBaseParent()
	{
		return this.parent.getBaseParent();
	}

	public boolean inMouseOver(float mouseX, float mouseY)
	{
		return mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + h;
	}


}
