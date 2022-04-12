package me.thesilverecho.zeropoint.impl.module.display.pickup;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.impl.event.PickupEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@ClientModule(name = "Pickup display",
		description = "Shows a list of items picked up.", active = true)
public class PickupHud extends BaseModule
{
	private int maxLines = 3;
	private int renderTime = 0;

	ItemStack test;

	@EventListener
	public void onPickup(PickupEvent event)
	{
		final Entity entity = event.entity();
		if (entity instanceof ItemEntity)
		{
			final ItemStack stack = ((ItemEntity) entity).getStack();
			if (!stack.isEmpty() && !stack.getItem().equals(Items.AIR))
			{

				test = stack.copy();
				System.out.println("Pickup?" + stack);
			}
		}
	}

	@EventListener
	public void render(Render2dEvent.Pre event)
	{

	/*	if (test != null)
		{
			{
				final ItemStack stack = test;
//				ScoreBoardHud.blurFBO.clear();
				final MatrixStack matrixStack = event.matrixStack();
//				MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(test, 0, 32);
				matrixStack.push();
				ScoreBoardHud.blurFBO.bind();
				MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(test, 15, 64);
//				MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(test, 32, 32);
//				itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, x + 1, y + 1);
				FontRenderer.renderText(APIFonts.REGULAR.getFont(), matrixStack, stack.toString(), 32, 32);


				MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(test, 15, 100);
				FontRenderer.renderText(APIFonts.REGULAR.getFont(), matrixStack, stack.toString(), 32, 100);


				matrixStack.pop();

				ScoreBoardHud.blurFBO.unbind();

			}
		}*/
	}

}
