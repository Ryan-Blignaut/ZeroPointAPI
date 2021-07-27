package me.thesilverecho.zeropoint.api.config.selector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that renderer field will have custom rendering.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MenuSelector
{
	/**
	 * the options the user will be able to select from
	 */
	String[] options() default {};
}
