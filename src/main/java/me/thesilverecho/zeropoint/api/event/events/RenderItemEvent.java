package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public final class RenderItemEvent implements BaseEvent
{
	private VertexConsumerProvider vertexConsumerProvider;
	private RenderLayer layer;
	private boolean solid, glint;
	private CallbackInfoReturnable<VertexConsumer> cir;

	private ItemStack stack;
	private ModelTransformation.Mode transformation;

	public RenderItemEvent setup(VertexConsumerProvider vertexConsumerProvider, RenderLayer layer, boolean solid, boolean glint, CallbackInfoReturnable<VertexConsumer> cir)
	{
		this.vertexConsumerProvider = vertexConsumerProvider;
		this.layer = layer;
		this.solid = solid;
		this.glint = glint;
		this.cir = cir;
		return this;
	}

	public ItemStack getStack()
	{
		return stack;
	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
	}

	public ModelTransformation.Mode getTransformation()
	{
		return transformation;
	}

	public RenderItemEvent setTransformation(ModelTransformation.Mode transformation)
	{
		this.transformation = transformation;
		return this;
	}

	public VertexConsumerProvider getVertexConsumerProvider()
	{
		return vertexConsumerProvider;
	}

	public RenderLayer getLayer()
	{
		return layer;
	}

	public boolean isSolid()
	{
		return solid;
	}

	public boolean isGlintEnabled()
	{
		return glint;
	}

	public CallbackInfoReturnable<VertexConsumer> getCancelInfo()
	{
		return cir;
	}
}
