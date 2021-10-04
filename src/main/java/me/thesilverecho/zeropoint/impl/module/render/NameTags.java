package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderEntityEvent;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Vec3d;

public class NameTags
{

	@ConfigSetting private int totalNumberOfTags = 50;

	@EventListener
	private void renderCustomNames(RenderEntityEvent event)
	{

//		event.gameRenderer()
		Vec3d cameraPos = event.camera().getPos();
		final Frustum frustum;
		final Frustum eventFrustum = event.frustum();
		if (eventFrustum == null)
		{
			frustum = new Frustum(event.matrices().peek().getModel(), event.matrix4f());
			frustum.setPosition(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
		} else
			frustum = eventFrustum;

//		StreamSupport.stream(event.world().getEntities().spliterator(), false).filter(entity -> entity instanceof LivingEntity && entity != cameraEntity && entity.isAlive() && entity.shouldRender(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ()) && (entity.ignoreCameraFrustum || frustum.isVisible(entity.getBoundingBox()))).map(LivingEntity.class::cast).forEach(entity -> renderHealthBar(entity, matrices, partialTicks, camera, cameraEntity));


	}

}
