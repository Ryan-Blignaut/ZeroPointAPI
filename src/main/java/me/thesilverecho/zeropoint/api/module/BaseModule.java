package me.thesilverecho.zeropoint.api.module;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.util.Keybind;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
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
	public static final HashMap<String, BaseModule> ENABLE_MODULES = new HashMap<>();

	/**
	 * Constructor to set instance variables from {@link ClientModule} annotation, registers a toggle keybind and calls toggleOptions so that enabled can take effect.
	 *
	 * @param active if the current module is active, use null to get from annotation.
	 * @param key    current keybind for the, use null to get from annotation.
	 */
	public BaseModule(@Nullable Boolean active, @Nullable Integer key)
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
		if (!enabled)
		{
			onEnable();
			EventManager.register(this);
			ENABLE_MODULES.put(this.getName(), this);
		} else
		{
			ENABLE_MODULES.remove(this.getName());
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
