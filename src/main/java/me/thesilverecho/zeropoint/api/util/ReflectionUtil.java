package me.thesilverecho.zeropoint.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Various utils for reflection so that try/catches are not needed everywhere.
 */
public class ReflectionUtil
{
	/**
	 * Safely invokes a method using reflection.
	 *
	 * @param source the class(source) of the method to be called
	 * @param target the method(target) of the method to be called
	 * @param args   the arguments that will be passed to the method
	 */
	public static void invokeMethodSafe(Object source, Method target, Object... args)
	{
		try
		{
			final boolean previousAccess = target.canAccess(source);
			target.setAccessible(true);
			target.invoke(source, args);
			target.setAccessible(previousAccess);
		} catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Safely gets the value of a field or null if unable.
	 *
	 * @param field the field that's value will be retrieved
	 * @param source the instance of the class of the field
	 * @return the fields value or null
	 */
	public static Object getObjValueSafe(Field field, Object source)
	{
		try
		{
			boolean previousAccess = field.canAccess(source);
			field.setAccessible(true);
			final Object value = field.get(source);
			field.setAccessible(previousAccess);
			return value;
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Safely sets the value of a field.
	 *
	 * @param field  the field that's value will be modified
	 * @param source the instance of the class of the field
	 * @param value the value that will be set
	 */
	public static void setObjValueSafe(Field field, Object source, Object value)
	{
		try
		{
			boolean previousAccess = field.canAccess(source);
			field.setAccessible(true);
			field.set(source, value);
			field.setAccessible(previousAccess);
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}


}
