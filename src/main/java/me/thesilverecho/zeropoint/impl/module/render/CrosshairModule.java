package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Custom Crosshair", keyBinding = GLFW.GLFW_KEY_C, active = true)
public class CrosshairModule extends BaseModule
{
	@EventListener
	public void renderEvent(Render2dEvent.RenderCrosshair e)
	{
		e.ci().cancel();
		final MatrixStack matrixStack = e.matrixStack();
		final int scaledHeight = e.scaledHeight() / 2;
		final int scaledWidth = e.scaledWidth() / 2;
		int width = 5;
		int height = 5;
		int s = 1;
		drawXCrosshair(matrixStack, scaledWidth, scaledHeight, 0, ColourHolder.FULL);
	}

	private static void drawXCrosshair(MatrixStack matrix, int screenWidth, int screenHeight, int renderGap, ColourHolder renderColour)
	{

		float i = 5 / 2f;


	}

}
