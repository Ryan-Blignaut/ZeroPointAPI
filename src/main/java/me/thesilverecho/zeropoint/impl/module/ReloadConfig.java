package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Reload Shaders", keyBinding = GLFW.GLFW_KEY_END)
public class ReloadConfig extends BaseModule
{
	@Override
	public void onEnable()
	{
		ModuleManager.moduleConfig.register(ModuleManager.INSTANCE);
	}
}
