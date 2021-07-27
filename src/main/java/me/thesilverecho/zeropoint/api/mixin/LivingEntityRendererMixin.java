package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
{

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void renderHead(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci)
	{
//		glEnable(GL_POLYGON_OFFSET_FILL);
//		glPolygonOffset(1.0f, -1100000.0f);
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void renderTail(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci)
	{
//		glPolygonOffset(1.0f, 1100000.0f);
//		glDisable(GL_POLYGON_OFFSET_FILL);
	}


	/*@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;"))
	private RenderLayer getRenderLayer(LivingEntityRenderer<T, M> livingEntityRenderer, T livingEntity, boolean showBody, boolean translucent, boolean showOutline)
	{
		return RenderLayer.getItemEntityTranslucentCull(new Identifier(ZeroPointClient.MOD_ID, "textures/bg.png"));
	}*/

}
