package me.thesilverecho.zeropoint.impl.module.render3;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.MouseEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.impl.event.HandItemRenderEvent;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Adds fake blocking animations", active = false)
public class FakeItemBlocking extends BaseModule
{

	private boolean blocking = false;

	@EventListener
	public void renderEvent(HandItemRenderEvent event)
	{
		event.ci().cancel();
		final MatrixStack matrixStack = event.matrices();
		if (blocking)
		{


			/*matrixStack.translate(0.56F, -0.52F, -0.71999997F);
			matrixStack.translate(0.0F, 0.2 * -0.6F, 0.0F);
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(45));
			float f1 = MathHelper.sin(MathHelper.sqrt(0.2f) * (float) Math.PI);
			float f = MathHelper.sin(0.2f * 0.2f * (float) Math.PI);
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f * -20.0F));
			matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(f1 * -20.0F));
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(f1 * -80.0F));

			matrixStack.scale(0.4F, 0.4F, 0.4F);*/

			matrixStack.translate(-0.5F, 0.4F, -0.2F);
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(32));
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-70));
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(40));

//			matrixStack.multiply(new Quaternion( 0, 1, 0,32));
//			matrixStack.multiply(new Quaternion( 1, 0, 0,-70));
//			matrixStack.multiply(new Quaternion( 0, 1, 0,40));
		}
		event.itemRenderer().renderItem(event.entity(), event.stack(), event.renderMode(), event.leftHanded(), matrixStack, event.vertexConsumers(), event.entity().world, event.light(), OverlayTexture.DEFAULT_UV, event.entity().getId() + event.renderMode().ordinal());

	}

	@EventListener
	public void onKeyPressEvent(MouseEvent event)
	{
		if (event.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
		{
			if (event.action() == GLFW.GLFW_PRESS) blocking = true;
			else if (event.action() == GLFW.GLFW_RELEASE) blocking = false;
		}
	}


}
