package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderScreenEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.cursor.APICursors;
import me.thesilverecho.zeropoint.api.render.cursor.CustomCursor;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;

@ClientModule(name = "Custom Cursor", active = true)
public class CursorCustom extends BaseModule
{


	@EventListener
	public void renderEvent(RenderScreenEvent.POST e)
	{
		CustomCursor cursor = APICursors.DEFAULT.getCursor();
		final Screen screen = e.screen();
		final int mouseX = e.mouseX();
		final int mouseY = e.mouseY();
		if (screen instanceof GameMenuScreen)
		{
			cursor = APICursors.HAND.getCursor();
		}
		cursor.activateCursor();
	}
}
