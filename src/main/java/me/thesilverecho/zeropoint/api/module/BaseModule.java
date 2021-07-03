package me.thesilverecho.zeropoint.api.module;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.util.Keybind;
import net.minecraft.client.MinecraftClient;

public class BaseModule implements IModule
{
	// Instance of MinecraftClient to prevent the need for constantly calling getInstance();
	protected static final MinecraftClient MC = MinecraftClient.getInstance();

	// Instance variables, set by ClientModule annotation
	private final String name, description;
	private final Keybind keybind;
	private final boolean shouldDraw;
	private boolean enabled;


	/**
	 * Constructor to set instance variables from {@link ClientModule} annotation, registers a toggle keybind and calls toggleOptions so that enabled can take effect.
	 */
	public BaseModule()
	{
		final ClientModule annotation = getClass().getAnnotation(ClientModule.class);
		this.enabled = annotation.active();
		this.name = annotation.name();
		this.shouldDraw = annotation.shouldDraw();
		this.description = annotation.description();
		this.keybind = new Keybind(annotation.keyBinding(), Keybind.Duration.TOGGLED, clickType ->
		{
			if (clickType == Keybind.ClickType.PRESSED)
				this.toggle();
		});
		this.runToggleActions();
	}


	/**
	 * Toggles the module on/off.
	 */
	private void toggle()
	{
		this.enabled = !enabled;
		runToggleActions();
	}

	/**
	 * Actions to be run if module is enabled or disabled.
	 */
	private void runToggleActions()
	{
		if (enabled)
		{
			onEnable();
			EventManager.register(this);
		} else
		{
			EventManager.deregister(this);
			onDisable();
		}
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
