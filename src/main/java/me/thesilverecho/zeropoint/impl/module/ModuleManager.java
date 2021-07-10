package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.config.Config;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.module.SimpleModuleHolder;
import me.thesilverecho.zeropoint.api.util.DynamicClassUtil;
import me.thesilverecho.zeropoint.api.util.ReflectionUtil;

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
		DynamicClassUtil.getClasses("me.thesilverecho.zeropoint.impl", "module").forEach(clazzName ->
				ReflectionUtil.getClassFromPath(clazzName, true).ifPresent(clazz ->
				{
					if (clazz.getSuperclass() == BaseModule.class)
					{
						final String name = clazz.getAnnotation(ClientModule.class).name();
						final Optional<SimpleModuleHolder> first = instance.baseModules.stream().filter(module -> module.getName().equals(name)).findFirst();
						if (first.isPresent())
							ReflectionUtil.callConstructor(clazz, BaseModule.class, first.get().isEnabled(), first.get().getKey());
						else
							ReflectionUtil.callConstructor(clazz, BaseModule.class, null, null).ifPresent(baseModule ->
									instance.baseModules.add(new SimpleModuleHolder(baseModule)));
					}
				}));
		instance.MODULE_CONFIG.save();
	}
}
