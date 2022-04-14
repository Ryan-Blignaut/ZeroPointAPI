package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.config.Config;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.util.DynamicClassUtil;
import me.thesilverecho.zeropoint.api.util.ReflectionUtil;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;

import java.util.HashMap;
import java.util.Optional;

public class ModuleManager
{
	@ConfigSetting
	private final HashMap<String, BaseModule> baseModules = new HashMap<>();

	public static final Config MODULE_CONFIG = new Config("Zero-point/modules");
	public static final ModuleManager INSTANCE = new ModuleManager();

	public static void registerAllModules()
	{
		MODULE_CONFIG.register(INSTANCE);

		DynamicClassUtil.getClasses("me/thesilverecho/zeropoint/impl/module").forEach(clazzName ->
				ReflectionUtil.getClassFromPath(clazzName, true).ifPresent(clazz ->
				{
					if (clazz.getSuperclass() == BaseModule.class)
					{
						final String name = clazz.getAnnotation(ClientModule.class).name();
						final Optional<BaseModule> module = ReflectionUtil.callConstructor(clazz, BaseModule.class);
						module.ifPresentOrElse(baseModule ->
						{
//							If the module is not already in the list, add it.
							if (!INSTANCE.baseModules.containsKey(name))
							{
								ZeroPointApiLogger.debug("Module with name " + name + " does not exist, creating.");
								INSTANCE.baseModules.put(name, baseModule);
							}
						}, () -> ZeroPointApiLogger.error("Failed to load module " + name));
					}
				}));


		MODULE_CONFIG.save();
	}


}
