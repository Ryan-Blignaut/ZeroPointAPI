package me.thesilverecho.zeropoint.api.module;

import me.thesilverecho.zeropoint.api.config.SettingList;
import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.notification.Notification;
import me.thesilverecho.zeropoint.api.notification.NotificationManager;
import me.thesilverecho.zeropoint.api.notification.NotificationType;
import me.thesilverecho.zeropoint.api.render.animations.Animation;
import me.thesilverecho.zeropoint.api.render.animations.impl.DecelerateAnimation;
import me.thesilverecho.zeropoint.api.util.Keybind;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;

import java.util.HashMap;
import java.util.StringJoiner;


public class BaseModule implements IModule
{
	// Instance of MinecraftClient to prevent the need for constantly calling getInstance();
	protected static final MinecraftClient MC = MinecraftClient.getInstance();

	// Instance variables, set by ClientModule annotation
	private final Keybind keybind;
	private boolean enabled;
	private final String name;

	//Used to provide some more info on the module.
	private final String description;
	//Used to determine if the module should be drawn in the Arraylist gui.
	private final boolean shouldDraw;

	protected SettingList settingHolders = new SettingList();

	public static final HashMap<Class<? extends BaseModule>, BaseModule> ENABLE_MODULES2 = new HashMap<>();

	public SettingList getSettingHolders()
	{
		return settingHolders;
	}

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
			if (clickType == Keybind.ClickType.PRESSED) this.toggle();
		});
	}

	public BaseModule setKeybind(int key)
	{
		this.keybind.setKeyCode(key);
		return this;
	}

	public BaseModule setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		return this;
	}

	public void silentRegister()
	{
		register();
	}

	/**
	 * Toggles the module on/off.
	 */
	protected void toggle()
	{
		runToggleActions();
		this.enabled = !enabled;
	}


	/**
	 * Actions to be run if module is enabled or disabled.
	 */
	protected void runToggleActions()
	{
		if (!enabled)
		{
			PositionedSoundInstance onSound = new PositionedSoundInstance(ZeroPointClient.MODULE_ON, SoundCategory.MASTER, 1, 1, MC.player.getX(), MC.player.getY(), MC.player.getZ());
			MC.getSoundManager().play(onSound);
			register();
			onEnable();
		} else
		{
			PositionedSoundInstance offSound = new PositionedSoundInstance(ZeroPointClient.MODULE_OFF, SoundCategory.MASTER, 1, 1, MC.player.getX(), MC.player.getY(), MC.player.getZ());
			onDisable();
			deregister();
			MC.getSoundManager().play(offSound);
		}
	}

	private void register()
	{
		EventManager.register(this);
		ENABLE_MODULES2.putIfAbsent(this.getClass(), this);

	}

	private void deregister()
	{
		final BaseModule remove = ENABLE_MODULES2.remove(this.getClass());
		EventManager.deregister(this);
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
	public void onEnable()
	{
		if (shouldDraw)
			NotificationManager.INSTANCE.addNotification(Notification.Builder.builder("Module Enabled", this.name + " has been enabled.").setType(NotificationType.INFO).setTimeInSeconds(2f).build());
	}

	@Override
	public void onDisable()
	{
		if (shouldDraw)
			NotificationManager.INSTANCE.addNotification(Notification.Builder.builder("Module Disabled", this.name + " has been disabled.").setType(NotificationType.INFO).setTimeInSeconds(2f).build());
	}

	private Animation anime = new DecelerateAnimation(250, 1);


	public Animation getAnimation()
	{
		return anime;
	}


	@Override
	public String toString()
	{
		return new StringJoiner(", ", BaseModule.class.getSimpleName() + "[", "]").add("keybind=" + keybind.getKeyCode()).add("enabled=" + enabled).add("name='" + name + "'").toString();
	}
}
