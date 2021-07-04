package me.thesilverecho.zeropoint.impl.module;

import com.google.gson.GsonBuilder;
import me.thesilverecho.zeropoint.api.config.Config;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.module.ModuleSerialisation;
import me.thesilverecho.zeropoint.api.util.DynamicClassUtil;
import me.thesilverecho.zeropoint.api.util.Pair;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;

public class ModuleManager
{
	public static final ArrayList<BaseModule> ALL_MODULES = new ArrayList<>();
	public final Config MODULE_CONFIG = new Config(new GsonBuilder().setPrettyPrinting().registerTypeHierarchyAdapter(BaseModule.class, new ModuleSerialisation()).create(), "Zero-point/test");
	@ConfigSetting
	public final HashSet<Pair<String, BaseModule>> ENABLED_MODULES = new HashSet<>();

	public static void registerAllModules()
	{

		final ModuleManager instance = new ModuleManager();

	/*	DynamicClassUtil.initClasses("me.thesilverecho.zeropoint.impl", "module", BaseModule.class).forEach(baseModule ->
		{
			ZeroPointApiLogger.debug(baseModule);
			ModuleManager.ALL_MODULES.add(baseModule);
			ZeroPointClient.DEFAULT_CONFIG.register(baseModule);
		});*/
		instance.MODULE_CONFIG.register(instance);
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
		DynamicClassUtil.getClasses("me.thesilverecho.zeropoint.impl", "module", cl).forEach(clazzName ->
		{
			try
			{
				final Class<?> clazz = Class.forName(clazzName, true, cl);

				if (clazz.getSuperclass() == BaseModule.class)
				{
					final String name = clazz.getAnnotation(ClientModule.class).name();
					boolean found = false;
					for (Pair<String, ? extends BaseModule> enabledModule : instance.ENABLED_MODULES)
					{
						ZeroPointApiLogger.debug("this should not be called yet");
						if (enabledModule.key().equals(name))
						{
							found = true;
							break;
						}
					}
					if (!found)
					{
						Constructor<? extends BaseModule> constructor = clazz.asSubclass(BaseModule.class).getConstructor();
						final BaseModule baseModule = constructor.newInstance();
						ZeroPointApiLogger.debug("This is be called -> base module: " + baseModule);
						final Pair<String, BaseModule> pair = new Pair<>(name, baseModule);
						ZeroPointApiLogger.debug("Pair module: " + pair);
						instance.ENABLED_MODULES.add(pair);
					}
				}

			} catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e)
			{
				ZeroPointApiLogger.error("Error registering class: " + clazzName, e);
			}

		});
		instance.MODULE_CONFIG.save();
	}
}
