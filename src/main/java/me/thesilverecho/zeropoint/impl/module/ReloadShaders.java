package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Reload Shaders",  keyBinding = GLFW.GLFW_KEY_HOME)
public class ReloadShaders extends BaseModule
{
	@Override
	protected void toggle()
	{
		Shader.resetShaderHashMap();
	}
}
