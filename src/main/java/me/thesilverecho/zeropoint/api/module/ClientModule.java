package me.thesilverecho.zeropoint.api.module;

import org.lwjgl.glfw.GLFW;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClientModule
{
	/**
	 * @return name of the module
	 */
	String name();

	/**
	 * @return description of the module
	 */
	String description() default "No description provided";

	/**
	 * @return if the module should be drawn in the array list
	 */
	boolean shouldDraw() default true;

	/**
	 * @return if a notification must be sent when the module is toggled
	 */
	boolean showToggleMsg() default false;

	/**
	 * @return if the module is active by default
	 */
	boolean active() default false;

	/**
	 * @return the keybinding for the module
	 */
	int keyBinding() default GLFW.GLFW_KEY_UNKNOWN;
}
