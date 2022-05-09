package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.render.ScreenEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.APIColour;
import me.thesilverecho.zeropoint.impl.mixin.TooltipAccessor;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;

import java.util.concurrent.atomic.AtomicInteger;

@ClientModule(name = "Custom Tooltips", active = false)
public class CustomTooltips extends BaseModule
{
	@EventListener
	public void renderCustomTooltip(ScreenEvent.TooltipEvent event)
	{
		event.ci().cancel();
		extracted(event);


	}

	private void extracted(ScreenEvent.TooltipEvent event)
	{
		final MatrixStack matrices = event.matrices();
		float newX = event.x() + 12;
		int newY = event.y() - 12;

		final CustomFont font = CustomFont.getFontByName("comic");

		float maxWidth = 0;
		int maxHeight = event.components().size() == 1 ? -2 : 0;
		for (TooltipComponent component : event.components())
		{
			if (!(component instanceof OrderedTextTooltipComponent))
				return;
			StringBuilder stringBuilder = new StringBuilder();
			((TooltipAccessor) component).getText().accept((index, style, codePoint) ->
			{
				stringBuilder.append(codePoint);
				return true;
			});
			final float width = font.getWidth(stringBuilder.toString());
			maxHeight += font.getHeight();
			if (width > maxWidth)
				maxWidth = width;
		}

		if (newX + maxWidth > event.width())
			newX -= 28 + maxWidth;

		if (newY + maxHeight + 6 > event.height())
			newY = event.height() - maxHeight - 6;

		RenderUtilV2.roundRect(matrices, newX, newY, maxWidth, maxHeight + 8, 5, new APIColour(0, 120, 102, 180));

		AtomicInteger lineCounter = new AtomicInteger(0);
		final float finalNewX = newX;
		final int finalNewY = newY;
		event.components().stream().filter(OrderedTextTooltipComponent.class::isInstance).forEach(tooltipComponent ->
		{
			final OrderedText text = ((TooltipAccessor) tooltipComponent).getText();
			StringBuilder stringBuilder = new StringBuilder();
			text.accept((index, style, codePoint) ->
			{
				stringBuilder.append((char) codePoint);
				return true;
			});
			FontRenderer.renderText(font, matrices, stringBuilder.toString(), false, finalNewX, finalNewY + lineCounter.getAndIncrement() * font.getHeight());
		});
	}
}
