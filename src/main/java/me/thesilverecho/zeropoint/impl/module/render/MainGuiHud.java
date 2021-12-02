package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.impl.render.MusicScreen;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Modern Hotbar", active = true, keyBinding = GLFW.GLFW_KEY_RIGHT_SHIFT)
public class MainGuiHud extends BaseModule
{

	@Override
	protected void toggle()
	{
		MC.setScreen(new MusicScreen());
	}

	@Override
	protected void runToggleActions()
	{}
}
