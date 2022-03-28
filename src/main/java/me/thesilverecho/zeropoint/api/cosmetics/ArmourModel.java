package me.thesilverecho.zeropoint.api.cosmetics;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class ArmourModel<T extends LivingEntity> extends AnimalModel<T>
{
	private final ModelPart helmet;

	public ArmourModel(ModelPart root)
	{
		this.helmet = root.getChild(EntityModelPartNames.HEAD);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts()
	{
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> getBodyParts()
	{
		return ImmutableList.of();
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{

	}
}
