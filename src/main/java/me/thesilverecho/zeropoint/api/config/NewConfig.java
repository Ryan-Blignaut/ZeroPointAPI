package me.thesilverecho.zeropoint.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class NewConfig
{
	private final Gson gson;
	private final File configFile;
//	private final JsonObject config;

	public NewConfig(String configName)
	{
//		Gson instance that has pretty printing enabled and will ignore values without the @Expose annotation.
		this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
//		Location of the config file(minecraft/Config/configName.json).
		this.configFile = new File(MinecraftClient.getInstance().runDirectory, "Config" + File.separator + configName + ".json");
	}


	public String serializeModules(ArrayList<Module> modules)
	{
		return gson.toJson(modules);
	}


	public ArrayList<Module> deserializeModules(String json)
	{
		return gson.fromJson(json, (Type) Module[].class);

	}


}
