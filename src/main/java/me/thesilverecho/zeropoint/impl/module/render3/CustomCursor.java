package me.thesilverecho.zeropoint.impl.module.render3;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.MouseEvent;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.event.events.render.RenderScreenEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.cursor.APICursors;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@ClientModule(name = "Custom Cursor", active = true)
public class CustomCursor extends BaseModule
{
	private static final CopyOnWriteArrayList<APIParticle> PARTICLES = new CopyOnWriteArrayList<>();
	float timer = 0;

	private me.thesilverecho.zeropoint.api.render.cursor.CustomCursor previousCursor;

	@EventListener
	public void renderEvent(RenderScreenEvent.POST e)
	{
		me.thesilverecho.zeropoint.api.render.cursor.CustomCursor cursor = APICursors.DEFAULT.getCursor();
		final Screen screen = e.screen();
		final int mouseX = e.mouseX();
		final int mouseY = e.mouseY();
		timer += 10 * e.delta();
	/*	if ( timer >= 100)
		{
			final Random r = new Random();

			PARTICLES.add(new APIParticle(20, mouseX, mouseY, +5, new ColourHolder(r.nextInt(255), r.nextInt(255), r.nextInt(255), 255)));
		}*/
		if (screen instanceof GameMenuScreen)
		{
			cursor = APICursors.HAND.getCursor();
		}
		if (screen instanceof HandledScreen)
		{
			final HandledScreen<?> s = (HandledScreen<?>) screen;
			final ItemStack cursorStack = s.getScreenHandler().getCursorStack();

			if (!cursorStack.isEmpty())
				cursor = APICursors.MOVE.getCursor();

		}


		PARTICLES.forEach(apiParticle -> apiParticle.render(e.matrices()));
		if (previousCursor != cursor)
		{
			previousCursor = cursor;
			cursor.activateCursor();
		}
	}

	@EventListener
	public void mouseClickEvent(MouseEvent e)
	{
		if (MinecraftClient.getInstance().currentScreen != null)
		{
			if (e.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT)
			{
				final Random r = new Random();
				for (int i = 0; i < r.nextInt(5) + 1; i++)
				{
					//Spawn particles
//					PARTICLES.add(new APIParticle(r.nextInt(1), (float) e.x() + i * 20, (float) e.y() + i * 20, (float) r.nextInt(10) + 5, new ColourHolder(r.nextInt(255), r.nextInt(255), r.nextInt(255), 255)));
				}
			}
		}
	}

	@EventListener
	public void updateEvent(TickEvent.StartTickEvent e)
	{
		if (MinecraftClient.getInstance().currentScreen != null)
		{
			//Update particles
			PARTICLES.forEach(apiParticle ->
			{
				apiParticle.update();
				if (apiParticle.timeLeft < 0)
					PARTICLES.remove(apiParticle);
			});
		}
	}

	private static final class APIParticle
	{
		private int timeLeft = 1000;
		private float x, y;
		private float size;
		private ColourHolder colour;

		public APIParticle(int timeLeft, float x, float y, float size, ColourHolder colour)
		{
			this.timeLeft = timeLeft;
			this.x = x;
			this.y = y;
			this.size = size;
			this.colour = colour;
		}

		private void update()
		{
			this.timeLeft--;
		}

		private void render(MatrixStack matrixStack)
		{
			RenderUtilV2.roundRect(matrixStack, x, y, size, size, 2, colour);
		}
	}
}
