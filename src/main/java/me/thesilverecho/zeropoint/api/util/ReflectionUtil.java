package me.thesilverecho.zeropoint.api.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Various utils for reflection so that try/catches are not needed everywhere.
 */
public class ReflectionUtil
{
	/**
	 * Safely invokes renderer method using reflection.
	 *
	 * @param source the class(source) instance of the method to be called
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
			ZeroPointApiLogger.error("Error invoking method: " + target.getName(), e);
		}
	}

	/**
	 * Safely gets the value of renderer field or null if unable.
	 *
	 * @param field  the field that's value will be retrieved
	 * @param source the instance of the class of the field
	 * @return the fields value or null
	 */
	public static Object getObjValueSafe(Field field, Object source)
	{
		try
		{
			final boolean previousAccess = field.canAccess(source);
			field.setAccessible(true);
			final Object value = field.get(source);
			field.setAccessible(previousAccess);
			return value;
		} catch (IllegalAccessException e)
		{
			ZeroPointApiLogger.error("Error getting field: " + field.getName(), e);
		}
		return null;
	}

	/**
	 * Safely sets the value of renderer field.
	 *
	 * @param field  the field that's value will be modified
	 * @param source the instance of the class of the field
	 * @param value  the value that will be set
	 */
	public static void setObjValueSafe(Field field, Object source, Object value)
	{
		try
		{
			final boolean previousAccess = field.canAccess(source);
			field.setAccessible(true);
			field.set(source, value);
			field.setAccessible(previousAccess);
		} catch (IllegalAccessException e)
		{
			ZeroPointApiLogger.error("Error setting field: " + field.getName(), e);
		}
	}

	public static <T> Optional<T> callConstructor(Class<?> clazz, Class<T> type, Object... params)
	{
		try
		{
			final int length = params.length;
			Class<?>[] parameterTypes = new Class[length];
			// Not the best implantation but I want to use nulls with this method, thus getting the class of the params is not possible.
			for (Constructor<?> constructor : clazz.asSubclass(type).getConstructors())
				if (constructor.getParameterCount() == length)
				{
					System.arraycopy(constructor.getParameterTypes(), 0, parameterTypes, 0, length);
					break;
				}
			Constructor<? extends T> constructor = clazz.asSubclass(type).getConstructor(parameterTypes);
			return Optional.of(constructor.newInstance(params));

		} catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e)
		{
			ZeroPointApiLogger.error("Error calling constructor", e);
		}
		return Optional.empty();
	}

	public static Optional<Class<?>> getClassFromPath(String path, boolean load)
	{
		try
		{
			return Optional.of(Class.forName(path, load, Thread.currentThread().getContextClassLoader()));
		} catch (ClassNotFoundException e)
		{
			ZeroPointApiLogger.error("Error getting class from path: " + path, e);
		}
		return Optional.empty();
	}

}
