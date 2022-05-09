package me.thesilverecho.zeropoint.impl.module;

import com.google.gson.GsonBuilder;
import me.thesilverecho.zeropoint.api.config.Config;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.module.ModuleTypeFactoryAdapter;
import me.thesilverecho.zeropoint.api.util.DynamicClassUtil;
import me.thesilverecho.zeropoint.api.util.ReflectionUtil;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Optional;

public class ModuleManager
{

	public static final ModuleManager INSTANCE = new ModuleManager();
	public static Config moduleConfig;
	@ConfigSetting public final HashMap<String, BaseModule> baseModules = new HashMap<>();
	public final HashMap<String, BaseModule> copyOfModules = new HashMap<>();

	public static void registerAllModules()
	{

		DynamicClassUtil.getClasses("me/thesilverecho/zeropoint/impl/module").forEach(clazzName -> ReflectionUtil.getClassFromPath(clazzName, true).ifPresent(clazz ->
		{

			if (clazz.getAnnotation(ClientModule.class) != null)
				if (clazz.getSuperclass() == BaseModule.class)
				{
					final String name = clazz.getAnnotation(ClientModule.class).name();
					Optional<? extends BaseModule> module = callClassConstructor(clazz);
					module.ifPresentOrElse(baseModule ->
					{
						ZeroPointApiLogger.error("Registered module: " + name);
//						baseModule.loadSettings();
						INSTANCE.baseModules.put(name, baseModule);
						INSTANCE.copyOfModules.put(name, baseModule);
					}, () -> ZeroPointApiLogger.error("Failed to load module " + name));
				}
		}));

		final Object clone = INSTANCE.baseModules.clone();


		if (moduleConfig == null)
			moduleConfig = new Config(new GsonBuilder().registerTypeAdapterFactory(new ModuleTypeFactoryAdapter(INSTANCE.baseModules)).setPrettyPrinting().create(), "Zero-point/modules");

		moduleConfig.register(INSTANCE);
//		TODO: Fix this, literally just hacked it together here, I think a custom type adapter is needed for map;
		INSTANCE.copyOfModules.putAll(INSTANCE.baseModules);
		INSTANCE.baseModules.putAll(INSTANCE.copyOfModules);
		moduleConfig.save();
		INSTANCE.baseModules.forEach((s, baseModule) ->
		{
			if (baseModule.isEnabled()) baseModule.silentRegister();
		});
	}

	@NotNull
	private static Optional<? extends BaseModule> callClassConstructor(Class<?> clazz)
	{
		Optional<? extends BaseModule> module = Optional.empty();
		try
		{
			final Constructor<?> constructor = clazz.getConstructor();
			final Object o = constructor.newInstance();
			module = Optional.of((BaseModule) o);
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return module;
	}


}
