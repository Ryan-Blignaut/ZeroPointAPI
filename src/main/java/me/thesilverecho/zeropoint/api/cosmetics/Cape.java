/*
package me.thesilverecho.zeropoint.api.cosmetics;

import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class Cape extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
{
	public Cape(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context)
	{
		super(context);

		ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("cloak", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, Dilation.NONE, 1.0F, 0.5F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

//		this.cape = new ModelPart.Cuboid(22, 23, 0, 0);
//		this.leftWingOuter = new ModelPart.Cuboid()t(this, 0, 18);
//		this.leftWingOuter.mirror = true;
//		this.leftWingOuter.setPivot(0.5F, 0.0F, 0.0F);

	}


	public ModelPart rightBaseStem;
	public ModelPart leftBaseStem;
	public ModelPart rightOuterStem;
	public ModelPart rightWingInner;
	public ModelPart rightWingOuter;
	public ModelPart leftOuterStem;
	public ModelPart leftWingInner;
	public ModelPart leftWingOuter;

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{

	}
}
*/
