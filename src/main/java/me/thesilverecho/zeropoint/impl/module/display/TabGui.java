package me.thesilverecho.zeropoint.impl.module.display;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "TabGui", description = "Displays the tab gui", keyBinding = GLFW.GLFW_KEY_TAB)
public class TabGui extends BaseModule
{
	@EventListener
	public void render(Render2dEvent.Pre event)
	{


	}


}
