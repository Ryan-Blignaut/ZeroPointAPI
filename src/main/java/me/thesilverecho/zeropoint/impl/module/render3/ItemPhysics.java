package me.thesilverecho.zeropoint.impl.module.render3;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.impl.event.ItemRenderEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

@ClientModule(name = "Item Physics", active = false)
public class ItemPhysics extends BaseModule
{

	private static final Random RANDOM = new Random();

	private long lastTime;

	@EventListener
	public void onTick(TickEvent.EndTickEvent event)
	{
		lastTime = Util.getMeasuringTimeNano();
	}

	@EventListener
	private void render(ItemRenderEvent event)
	{
		event.ci().cancel();
		final MatrixStack matrixStack = event.matrixStack();
		matrixStack.push();
		final MinecraftClient instance = MinecraftClient.getInstance();
		final ItemEntity itemEntity = event.itemEntity();
		final ItemStack stack = itemEntity.getStack();
		RANDOM.setSeed(stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage());
		final BakedModel model = event.itemRenderer().getModel(stack, itemEntity.world, null, itemEntity.getId());
		final boolean hasDepth = model.hasDepth();
		final int renderedAmount = getRenderedAmount(stack);


		float rotateAmount = (Util.getMeasuringTimeNano() - lastTime) / 400_000_000f;
		if (instance.isPaused())
			rotateAmount = 0;


		matrixStack.translate(0, .02f, 0);
		matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
		matrixStack.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(itemEntity.getYaw()));

		final boolean applyEffects = itemEntity.getItemAge() != 0 && (hasDepth || instance.getEntityRenderDispatcher().gameOptions != null);
		if (itemEntity.isSubmergedInWater())
			rotateAmount /= 2 * 10;


		if (applyEffects)
		{
			if (hasDepth)
			{
				if (!itemEntity.isOnGround())
				{
					rotateAmount *= 2;
					itemEntity.setPitch(itemEntity.getPitch() + rotateAmount);
				}
			} else if (!Double.isNaN(itemEntity.getX()) && !Double.isNaN(itemEntity.getY()) && !Double.isNaN(itemEntity.getZ()) && itemEntity.world != null)
			{
				if (itemEntity.isOnGround())
				{
					itemEntity.setPitch(0);
				} else
				{
					rotateAmount *= 2;
					itemEntity.setPitch(itemEntity.getPitch() + rotateAmount);
				}
			}

			if (hasDepth)
				matrixStack.translate(0, -0.2, -0.08);

			else
				matrixStack.translate(0, 0, -0.04);

			double height = 0.2;
			if (hasDepth)
				matrixStack.translate(0, height, 0);
			matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(itemEntity.getPitch()));
			if (hasDepth)
				matrixStack.translate(0, -height, 0);
		}

		if (!hasDepth)
		{
			float x = -0.0f * (renderedAmount - 1) * 0.5f;
			float y = -0.0f * (renderedAmount - 1) * 0.5f;
			float z = -0.09375f * (renderedAmount - 1) * 0.5f;

			matrixStack.translate(x, y, z);
		}


		for (int itemCount = 0; itemCount < renderedAmount; ++itemCount)
		{
			matrixStack.push();
			if (itemCount > 0)
			{
				if (hasDepth)
				{

					float x = (RANDOM.nextFloat() * 2.0f - 1.0f) * 0.15f;
					float y = (RANDOM.nextFloat() * 2.0f - 1.0f) * 0.15f;
					float z = (RANDOM.nextFloat() * 2.0f - 1.0f) * 0.15f;
					matrixStack.translate(x, y, z);
				}
			}
			event.itemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, false, matrixStack, event.vertexConsumerProvider(), event.light(), OverlayTexture.DEFAULT_UV, model);
			matrixStack.pop();
			if (!hasDepth)
				matrixStack.translate(0.0, 0.0, 0.05375f);
		}

		matrixStack.pop();
	}

	private static int getRenderedAmount(ItemStack stack)
	{
		return stack.getCount() > 48 ? 5 : stack.getCount() > 32 ? 4 : stack.getCount() > 16 ? 3 : stack.getCount() > 1 ? 2 : 1;
	}


}
