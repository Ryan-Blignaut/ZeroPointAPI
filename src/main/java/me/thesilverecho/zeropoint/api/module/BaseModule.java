package me.thesilverecho.zeropoint.api.module;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.util.Keybind;
import net.minecraft.client.MinecraftClient;

import java.util.HashMap;

public class BaseModule implements IModule
{
	// Instance of MinecraftClient to prevent the need for constantly calling getInstance();
	protected static final MinecraftClient MC = MinecraftClient.getInstance();

	// Instance variables, set by ClientModule annotation
	private final String name, description;
	private final boolean shouldDraw;
	private Keybind keybind;
	private boolean enabled;
	public static final HashMap<String, BaseModule> ENABLE_MODULES = new HashMap<>();

	/**
	 * Constructor to set instance variables from {@link ClientModule} annotation, registers renderer toggle keybind and calls toggleOptions so that enabled can take effect.
	 */
	public BaseModule()
	{
		final ClientModule annotation = this.getClass().getAnnotation(ClientModule.class);
		this.name = annotation.name();
		this.description = annotation.description();
		this.shouldDraw = annotation.shouldDraw();
		this.enabled = annotation.active();
		this.keybind = new Keybind(annotation.keyBinding(), Keybind.Duration.TOGGLED, clickType ->
		{
			if (clickType == Keybind.ClickType.PRESSED)
				this.toggle();
		});
		this.setEnabled(this.enabled);
	}

	public BaseModule setKeybind(int key)
	{
		this.keybind.setKeyCode(key);
		return this;
	}

	public BaseModule setEnabled(boolean enabled)
	{
//		TODO:fix this up a bit so that the register function is only called once, instead of when the obj is created and when the enabled var is set
		deregister();
		this.enabled = !enabled;
		runToggleActions();
		return this;
	}

	/**
	 * Toggles the module on/off.
	 */
	protected void toggle()
	{
		this.enabled = !enabled;
		runToggleActions();
	}

	/**
	 * Actions to be run if module is enabled or disabled.
	 */
	protected void runToggleActions()
	{
		if (!enabled) register();
		else deregister();
	}

	private void register()
	{
		onEnable();
		EventManager.register(this);
		ENABLE_MODULES.put(this.getName(), this);
	}

	private void deregister()
	{
		ENABLE_MODULES.remove(this.getName());
		EventManager.deregister(this);
		onDisable();
	}


	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public Keybind getKeybind()
	{
		return keybind;
	}

	public boolean shouldDraw()
	{
		return shouldDraw;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
}
