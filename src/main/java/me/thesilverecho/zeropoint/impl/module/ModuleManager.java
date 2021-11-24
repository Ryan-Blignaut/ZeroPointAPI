package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.config.Config;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.module.SimpleModuleHolder;
import me.thesilverecho.zeropoint.api.util.DynamicClassUtil;
import me.thesilverecho.zeropoint.api.util.ReflectionUtil;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;

import java.util.ArrayList;
import java.util.Optional;

public class ModuleManager
{
	public final Config MODULE_CONFIG = new Config("Zero-point/test");
	@ConfigSetting
	public ArrayList<SimpleModuleHolder> baseModules = new ArrayList<>();

	public static void registerAllModules()
	{
		final ModuleManager instance = new ModuleManager();
		instance.MODULE_CONFIG.register(instance);
		DynamicClassUtil.getClasses("me/thesilverecho/zeropoint/impl/module").forEach(clazzName ->
				ReflectionUtil.getClassFromPath(clazzName, true).ifPresent(clazz ->
				{
					if (clazz.getSuperclass() == BaseModule.class)
					{
						final String name = clazz.getAnnotation(ClientModule.class).name();
						final Optional<SimpleModuleHolder> first = instance.baseModules.stream().filter(module -> module.getName().equals(name)).findFirst();
						final Optional<BaseModule> module = ReflectionUtil.callConstructor(clazz, BaseModule.class);

						if (first.isPresent())
						{
//							ZeroPointApiLogger.error("Method was found and will be overwritten with values from the JSON");
							module.ifPresent(baseModule -> baseModule.setKeybind(first.get().getKey()).setEnabled(first.get().isEnabled()));
						} else
						{
//							ZeroPointApiLogger.error("NO Method was found in JSON CREATE NEW");
							module.ifPresent(baseModule -> instance.baseModules.add(new SimpleModuleHolder(baseModule)));
						}
					}
				}));
		instance.MODULE_CONFIG.save();
	}
}
