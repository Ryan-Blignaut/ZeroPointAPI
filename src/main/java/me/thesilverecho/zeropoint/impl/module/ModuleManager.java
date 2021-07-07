package me.thesilverecho.zeropoint.impl.module;

import com.google.gson.GsonBuilder;
import me.thesilverecho.zeropoint.api.config.Config;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.module.CollectionModuleSerialisation;
import me.thesilverecho.zeropoint.api.util.DynamicClassUtil;
import me.thesilverecho.zeropoint.api.util.ReflectionUtil;

import java.util.ArrayList;

public class ModuleManager
{
	public static final ArrayList<BaseModule> ALL_MODULES = new ArrayList<>();
	public final Config MODULE_CONFIG = new Config(new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(ArrayList.class, new CollectionModuleSerialisation())
			.create(), "Zero-point/test");
	@ConfigSetting
	public ArrayList<BaseModule> ENABLED_MODULES = new ArrayList<>();
	;

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
						final boolean b = instance.ENABLED_MODULES.stream().anyMatch(module -> module.getName().equals(name));
						if (!b)
							ReflectionUtil.callConstructor(clazz, BaseModule.class, null, null).ifPresent(instance.ENABLED_MODULES::add);
					}
				}));
		instance.MODULE_CONFIG.save();
	}
}
