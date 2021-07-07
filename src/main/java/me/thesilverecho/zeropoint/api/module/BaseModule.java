package me.thesilverecho.zeropoint.api.module;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.util.Keybind;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import net.minecraft.client.MinecraftClient;

import java.util.Objects;

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
//	public BaseModule()
//	{
//		this(null, null);
//	}

	public static  BaseModule newInstance(Boolean active, Integer key)
	{
		return new BaseModule(active, key);
	}

	public BaseModule(Boolean active, Integer key)
	{
		ClientModule annotation = this.getClass().getAnnotation(ClientModule.class);
		this.enabled = Objects.requireNonNullElseGet(active, annotation::active);
		this.name = annotation.name();
		this.description = annotation.description();
		this.shouldDraw = annotation.shouldDraw();
		this.keybind = new Keybind(Objects.requireNonNullElseGet(key, annotation::keyBinding), Keybind.Duration.TOGGLED, clickType ->
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
