package me.thesilverecho.zeropoint.api.util;

import java.util.ArrayList;
import java.util.function.Consumer;

import static me.thesilverecho.zeropoint.api.util.Keybind.ClickType.*;

public record Keybind(int code, Duration duration, Consumer<ClickType> consumer)
{
	/**
	 * List containing all the keybinds registered.
	 */
	public static final ArrayList<Keybind> REGISTERED_KEYBINDS = new ArrayList<>();

	public Keybind(int code, Duration duration, Consumer<ClickType> consumer)
	{
		this.code = code;
		this.consumer = consumer;
		this.duration = duration;
		REGISTERED_KEYBINDS.add(this);
	}

	public final void onRelease()
	{
		consumer.accept(RELEASED);
		if (duration == Duration.HELD)
			onClick();
	}

	public final void onPress()
	{
		consumer.accept(PRESSED);
		if (duration == Duration.HELD)
			onClick();
	}

	private void onClick()
	{
		consumer.accept(HELD_TICK_CLICK);
	}

	/**
	 * Enum for {@code Duration}, determining if the click will be activated while the key is released/held
	 */
	public enum Duration
	{
		TOGGLED, HELD
	}

	/**
	 * Enum for {@code ClickType}, determining what action will be activated when clicked
	 */
	public enum ClickType
	{
		HELD_TICK_CLICK, PRESSED, RELEASED
	}
}
