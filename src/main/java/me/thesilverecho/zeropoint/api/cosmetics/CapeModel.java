package me.thesilverecho.zeropoint.api.cosmetics;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class CapeModel extends Model
{
	public final ModelPart head;

	public CapeModel(EntityRendererFactory.Context ctx, EntityModelLayer entityModelLayer)
	{
		super(RenderLayer::getEntityTranslucent);
		this.head = ctx.getPart(entityModelLayer).getChild("head");
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha)
	{
		this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
