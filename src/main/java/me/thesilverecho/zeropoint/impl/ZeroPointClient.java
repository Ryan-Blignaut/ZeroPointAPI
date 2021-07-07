package me.thesilverecho.zeropoint.impl;

import me.thesilverecho.zeropoint.api.config.Config;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.render.font.ClientFonts;
import me.thesilverecho.zeropoint.api.render.shader.ShaderManager;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import me.thesilverecho.zeropoint.impl.module.ModuleManager;
import me.thesilverecho.zeropoint.impl.render.ZeroShaders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class ZeroPointClient implements ClientModInitializer
{
	public static final Config DEFAULT_CONFIG = new Config("Zero-point");
	public static final String MOD_ID = "zero-point";
	@ConfigSetting
	private int version = 1;

	@Override
	public void onInitializeClient()
	{
		DEFAULT_CONFIG.register(this);
		ZeroPointApiLogger.setUp(true, true, true);
		ModuleManager.registerAllModules();
		DEFAULT_CONFIG.save();
		ShaderManager.initShaders(Arrays.stream(ZeroShaders.values()).map(ZeroShaders::getShader).collect(Collectors.toList()));





	}
}
