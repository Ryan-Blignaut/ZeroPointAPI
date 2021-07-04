package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.util.DynamicClassUtil;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;

import java.util.ArrayList;

public class ModuleManager
{
	public static final ArrayList<BaseModule> ALL_MODULES = new ArrayList<>();
//	public final Config MODULE_CONFIG = new Config(new GsonBuilder().setPrettyPrinting()/*.registerTypeAdapter(BaseModule.class, (JsonDeserializer<BaseModule>) (json, typeOfT, context) ->
//	{
//		final JsonObject jo = json.getAsJsonObject();
//		return new BaseModule(jo.get("enabled").getAsBoolean(), jo.get("keybind").getAsInt());
//	})*/.create(), "Zero-point/test");
//	@ConfigSetting
//	public HashSet<BaseModule> ENABLED_MODULES = new HashSet<>();

	public static void registerAllModules()
	{

//		final ModuleManager instance = new ModuleManager();
//		instance.MODULE_CONFIG.register(instance);

		DynamicClassUtil.initClasses("me.thesilverecho.zeropoint.impl", "module", BaseModule.class).forEach(baseModule ->
		{
//			if (!instance.ENABLED_MODULES.contains(baseModule))
//				instance.ENABLED_MODULES.add(baseModule);
			ModuleManager.ALL_MODULES.add(baseModule);
			ZeroPointClient.DEFAULT_CONFIG.register(baseModule);
		});
//		instance.MODULE_CONFIG.save();
	}
}
