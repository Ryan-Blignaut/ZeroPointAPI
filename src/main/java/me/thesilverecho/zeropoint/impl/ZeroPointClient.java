package me.thesilverecho.zeropoint.impl;

import me.thesilverecho.zeropoint.api.config.Config;
import me.thesilverecho.zeropoint.impl.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ZeroPointClient implements ClientModInitializer
{

	public static final Config DEFAULT_CONFIG = new Config("Zero-point");

	@Override
	public void onInitializeClient()
	{
		ModuleManager.registerAllModules();
	}
}
