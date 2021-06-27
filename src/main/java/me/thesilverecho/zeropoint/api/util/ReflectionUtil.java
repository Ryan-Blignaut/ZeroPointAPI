package me.thesilverecho.zeropoint.api.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Various utils for reflection so that try/catches are not needed everywhere.
 */
public class ReflectionUtil
{
	/**
	 * Invokes a method using reflection, wrapped in try catch.
	 *
	 * @param source the class(source) of the method to be called
	 * @param target the method(target) of the method to be called
	 * @param args   the arguments that will be passed to the method
	 */
	public static void invokeMethodSafe(Object source, Method target, Object... args)
	{
		try
		{
			target.invoke(source, args);
		} catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}




}
