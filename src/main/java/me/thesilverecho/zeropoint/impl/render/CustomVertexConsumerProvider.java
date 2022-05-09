package me.thesilverecho.zeropoint.impl.render;

import me.thesilverecho.zeropoint.impl.render.layering.ZeroLayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;

public class CustomVertexConsumerProvider implements VertexConsumerProvider
{
	public static final CustomVertexConsumerProvider INSTANCE = new CustomVertexConsumerProvider();
	private final VertexConsumerProvider.Immediate plainDrawer = VertexConsumerProvider.immediate(new BufferBuilder(256));
//	public VertexConsumerProvider.Immediate parent;


	@Override
	public VertexConsumer getBuffer(RenderLayer layer)
	{
		final Immediate entityVertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		return VertexConsumers.union(entityVertexConsumers.getBuffer(ZeroLayers.BLOCK_ENTITY_LAYER), entityVertexConsumers.getBuffer(layer));//VertexConsumers.union(entityVertexConsumers.getBuffer(/*ModRenderLayer.CHARMING*/RenderLayer.getGlint()), entityVertexConsumers.getBuffer(layer));//VertexConsumers.union(new OutlineVertexConsumer(entityVertexConsumers.getBuffer(ModRenderLayer.CHARMING), 1, 1, 1, 1), entityVertexConsumers.getBuffer(layer));
	}

	public void draw()
	{
		this.plainDrawer.draw();
	}


	static class OutlineVertexConsumer extends FixedColorVertexConsumer
	{
		private final VertexConsumer delegate;
		private double x;
		private double y;
		private double z;
		private float u;
		private float v;

		OutlineVertexConsumer(VertexConsumer delegate, int red, int green, int blue, int alpha)
		{
			this.delegate = delegate;
			super.fixedColor(red, green, blue, alpha);
		}

		@Override
		public void fixedColor(int red, int green, int blue, int alpha)
		{
		}

		@Override
		public void unfixColor()
		{
		}

		@Override
		public VertexConsumer vertex(double x, double y, double z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}

		@Override
		public VertexConsumer color(int red, int green, int blue, int alpha)
		{
			return this;
		}

		@Override
		public VertexConsumer texture(float u, float v)
		{
			this.u = u;
			this.v = v;
			return this;
		}

		@Override
		public VertexConsumer overlay(int u, int v)
		{
			return this;
		}

		@Override
		public VertexConsumer light(int u, int v)
		{
			return this;
		}

		@Override
		public VertexConsumer normal(float x, float y, float z)
		{
			return this;
		}

		@Override
		public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ)
		{
			this.delegate.vertex(x, y, z).color(this.fixedRed, this.fixedGreen, this.fixedBlue, this.fixedAlpha).texture(u, v).next();
		}

		@Override
		public void next()
		{
			this.delegate.vertex(this.x, this.y, this.z).color(this.fixedRed, this.fixedGreen, this.fixedBlue, this.fixedAlpha).texture(this.u, this.v).next();
		}
	}

}
