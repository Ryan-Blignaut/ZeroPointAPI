package me.thesilverecho.zeropoint.api.event;

import me.thesilverecho.zeropoint.api.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The {@code EventManager} class provides renderer mechanism that allows for dynamic loading of modules.
 */
public class EventManager
{
	private static final HashMap<Class<?>, CopyOnWriteArrayList<MethodData>> EVENTS = new HashMap<>();

	private static final ArrayList<Class<?>> REGISTERED_EVENTS = new ArrayList<>();

	/**
	 * Basic registration of modules
	 * in this implementation only methods with the {@link EventListener} will be registered.
	 *
	 * @param moduleClass the class instance of the methods that will be registered.
	 */
	public static void register(Object moduleClass)
	{
		if (REGISTERED_EVENTS.contains(moduleClass.getClass()))
			return;
		REGISTERED_EVENTS.add(moduleClass.getClass());
		for (Method method : moduleClass.getClass().getDeclaredMethods())
			if (method.isAnnotationPresent(EventListener.class))
				register(method, moduleClass);
	}


	/**
	 * Basic de-registration of modules
	 * in this implementation only methods with the {@link EventListener} will be de-registered.
	 *
	 * @param moduleClass the class instance of the methods that will be de-registered.
	 */
	public static void deregister(Object moduleClass)
	{
		if (REGISTERED_EVENTS.contains(moduleClass.getClass()))
			return;
		REGISTERED_EVENTS.remove(moduleClass.getClass());

		for (Method method : moduleClass.getClass().getDeclaredMethods())
			if (method.isAnnotationPresent(EventListener.class))
				deregister(method, moduleClass);
	}


	/**
	 * Removes modules for class.
	 *
	 * @param moduleClass the class of the methods that will be deregistered.
	 */
	public static void deregister(Method method, Object moduleClass)
	{
		Class<?> indexClass = method.getParameterTypes()[0];
		if (!EVENTS.containsKey(indexClass))
			return;
		EVENTS.get(indexClass).remove(new MethodData(moduleClass, method, method.getAnnotation(EventListener.class).priority()));
	}

	/**
	 * Helper method that will allow the method to be registered.
	 *
	 * @param method      to be registered.
	 * @param moduleClass the class instance where the method can be found.
	 */
	private static void register(Method method, Object moduleClass)
	{
		Class<?> indexClass = method.getParameterTypes()[0];
		if (!EVENTS.containsKey(indexClass))
			EVENTS.put(indexClass, new CopyOnWriteArrayList<>());
		EVENTS.get(indexClass).add(new MethodData(moduleClass, method, method.getAnnotation(EventListener.class).priority()));
	}

	/**
	 * Invokes all methods from that module type.
	 *
	 * @param event the module type determining what methods will be invoked.
	 */
	public static void call(BaseEvent event)
	{
		if (EVENTS.containsKey(event.getClass()))
		{

			EVENTS.get(event.getClass())
			      .stream()
			      .sorted(Comparator.comparingInt(MethodData::priority))
			      .forEach(methodData ->
					      ReflectionUtil.invokeMethodSafe(methodData.source(), methodData.target(), event));
		}
	}
}
