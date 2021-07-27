package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.impl.render.ConfigScreen;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Modern Hotbar", active = true, keyBinding = GLFW.GLFW_KEY_RIGHT_SHIFT)
public class MainGuiHud extends BaseModule
{
	public MainGuiHud(@Nullable Boolean active, @Nullable Integer key)
	{
		super(null, null);
	}

	@Override
	protected void toggle()
	{
		MC.setScreen(new ConfigScreen());
	}

	@Override
	protected void runToggleActions()
	{ }
}
