package me.thesilverecho.zeropoint.api.cosmetics;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class Cape extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
{

	public ModelPart bipedCloakShoulders;
	public ModelPart miniBipedCloak;
	public ModelPart miniBipedCloakShoulders;
	private int capeTick = 0;
	private int ticks = 0;

	public Cape(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context)
	{
		super(context);
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
