package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.render.ScreenEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.impl.mixin.MinecraftClientAccessor;
import net.minecraft.client.util.math.MatrixStack;

@ClientModule(name = "Animate screens on open", active = true)
public class PopScreen extends BaseModule
{
	private float percent;
	private float lastPercent;
	private float percent2;
	private float lastPercent2;

	private boolean close;
	private boolean closed;


	@EventListener
	public void onOpen(ScreenEvent.OPEN_SCREEN event)
	{
		percent = 1.33f;
		lastPercent = 1f;
		percent2 = 1.33f;
		lastPercent2 = 1f;
		this.close = false;
		this.closed = false;
	}

	@EventListener
	public void onClose(ScreenEvent.CLOSE_SCREEN event)
	{
		close = true;
		event.ci().cancel();
	}


	@EventListener
	public void onRender(ScreenEvent.PRE event)
	{
		final MatrixStack matrixStack = event.matrices();
		percent = smoothTrans(this.percent, lastPercent);
		percent2 = smoothTrans(this.percent2, lastPercent2);
		matrixStack.push();

		final int scaledWidth = MC.getWindow().getScaledWidth();
		final int scaledHeight = MC.getWindow().getScaledHeight();
		if (percent > 0.98)
		{
			matrixStack.translate(scaledWidth / 2f, scaledHeight / 2f, 0);
			matrixStack.scale(percent, percent, 0);
			matrixStack.translate(-scaledWidth / 2f, -scaledHeight / 2f, 0);
		} else if (percent2 <= 1)
		{
			matrixStack.translate(scaledWidth / 2f, scaledHeight / 2f, 0);
			matrixStack.scale(percent2, percent2, 0);
			matrixStack.translate(-scaledWidth / 2f, -scaledHeight / 2f, 0);
		}


		if (percent <= 1.5 && close)
		{
			percent = smoothTrans(this.percent, 2);
			percent2 = smoothTrans(this.percent2, 2);
		}

		if (percent >= 1.4 && close)
		{
			percent = 1.5f;
			closed = true;
			MC.setScreen(null);
		}
	}

	public float smoothTrans(double current, double last)
	{
		return (float) (current + (last - current) / (MinecraftClientAccessor.getCurrentFps() / 10));
	}


}
