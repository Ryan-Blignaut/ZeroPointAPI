package me.thesilverecho.zeropoint.impl.render;

import net.minecraft.client.render.VertexConsumer;

public class CustomVertexConsumer implements VertexConsumer
{

	private final VertexConsumer defaultVertexConsumer;
	private int color;

	public CustomVertexConsumer setColor(int color)
	{
		this.color = color;
		return this;
	}

	public CustomVertexConsumer(VertexConsumer defaultVertexConsumer) {this.defaultVertexConsumer = defaultVertexConsumer;}

	@Override
	public VertexConsumer vertex(double x, double y, double z)
	{
		defaultVertexConsumer.vertex(x, y, z);
		return this;
	}

	@Override
	public VertexConsumer color(int red, int green, int blue, int alpha)
	{
		int r = this.color >> 16 & 255;
		int g = this.color >> 8 & 255;
		int b = this.color & 255;
		defaultVertexConsumer.color(r, g, b, alpha);
		return this;
	}

	@Override
	public VertexConsumer texture(float u, float v)
	{
		defaultVertexConsumer.texture(u, v);
		return this;
	}

	@Override
	public VertexConsumer overlay(int u, int v)
	{
		defaultVertexConsumer.overlay(u, v);
		return this;
	}

	@Override
	public VertexConsumer light(int u, int v)
	{
		defaultVertexConsumer.light(u, v);
		return this;
	}

	@Override
	public VertexConsumer normal(float x, float y, float z)
	{
		defaultVertexConsumer.normal(x, y, z);
		return null;
	}

	@Override
	public void next()
	{
		defaultVertexConsumer.next();
	}

	@Override
	public void fixedColor(int red, int green, int blue, int alpha)
	{
		defaultVertexConsumer.fixedColor(red, green, blue, alpha);
	}

	@Override
	public void unfixColor()
	{
		defaultVertexConsumer.unfixColor();
	}
}
