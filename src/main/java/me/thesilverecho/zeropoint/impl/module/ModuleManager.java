package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.util.DynamicClassUtil;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;

import java.util.ArrayList;

public class ModuleManager
{
	private static final ArrayList<BaseModule> ALL_MODULES = new ArrayList<>();

	public static void registerAllModules()
	{
		DynamicClassUtil.initClasses("", "", BaseModule.class).forEach(baseModule ->
		{
			ALL_MODULES.add(baseModule);
			ZeroPointClient.DEFAULT_CONFIG.register(baseModule);
		});
	}


}
