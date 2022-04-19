package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.impl.render.CustomVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin
{
	@Shadow
	public abstract <T extends Entity> EntityRenderer<? super T> getRenderer(T entity);

	@Inject(method = "render", at = @At(value = "HEAD", ordinal = 0), cancellable = true)
	private <E extends Entity> void injectRenderEvent(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci)
	{
		ci.cancel();
		final EntityRenderer<? super E> renderer = this.getRenderer(entity);
		Vec3d vec3d = renderer.getPositionOffset(entity, tickDelta);
		double d = x + vec3d.getX();
		double e = y + vec3d.getY();
		double f = z + vec3d.getZ();
		matrices.push();

		matrices.translate(d, e, f);
		renderer.render(entity, yaw, tickDelta, matrices, CustomVertexConsumerProvider.INSTANCE, light);

		matrices.pop();

	}
}
