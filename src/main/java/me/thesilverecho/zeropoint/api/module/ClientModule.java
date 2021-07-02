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
	String name();

	String description() default "No description provided";

	boolean shouldDraw() default true;

	boolean showToggleMsg() default false;

	boolean active() default false;

	int keyBinding() default GLFW.GLFW_KEY_UNKNOWN;
}
