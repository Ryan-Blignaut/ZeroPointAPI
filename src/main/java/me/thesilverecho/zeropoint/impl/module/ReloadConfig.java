package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Reload Shaders", active = true, keyBinding = GLFW.GLFW_KEY_HOME)
public class ReloadConfig extends BaseModule
{
	@Override
	public void onEnable()
	{
		ModuleManager.MODULE_CONFIG.save();
	}
}
