package me.thesilverecho.zeropoint.api.event;

import me.thesilverecho.zeropoint.api.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The {@code EventManager} class provides a mechanism that allows for dynamic loading of modules.
 */
public class EventManager
{
	private static final HashMap<Class<?>, CopyOnWriteArrayList<MethodData>> EVENTS = new HashMap<>();

	/**
	 * Basic registration of modules
	 * in this implementation only methods with the {@link EventListener} will be registered.
	 *
	 * @param moduleClass the class of the methods that will be registered.
	 */
	public static void register(Class<?> moduleClass)
	{
		for (Method method : moduleClass.getDeclaredMethods())
			if (method.isAnnotationPresent(EventListener.class))
				register(method, moduleClass);
	}

	/**
	 * Removes modules for class.
	 *
	 * @param moduleClass the class of the methods that will be deregistered.
	 */
	public static void deregister(Class<?> moduleClass)
	{
		EVENTS.get(moduleClass).clear();
	}

	/**
	 * Helper method that will allow the method to be registered.
	 *
	 * @param method      to be registered.
	 * @param moduleClass the class where the method can be found.
	 */
	private static void register(Method method, Class<?> moduleClass)
	{
//		Class<?> indexClass = method.getParameterTypes()[0];
		if (!EVENTS.containsKey(moduleClass))
			EVENTS.put(moduleClass, new CopyOnWriteArrayList<>());
		EVENTS.get(moduleClass).add(new MethodData(moduleClass, method, method.getAnnotation(EventListener.class).priority()));
	}

	/**
	 * Invokes all methods from that module type.
	 *
	 * @param event the module type determining what methods will be invoked.
	 */
	public static void call(BaseEvent event)
	{
		EVENTS.get(event.getClass())
		      .stream()
		      .sorted(Comparator.comparingInt(MethodData::priority))
		      .forEach(methodData -> ReflectionUtil.invokeMethodSafe(methodData.source(), methodData.target(), event));
	}
}
