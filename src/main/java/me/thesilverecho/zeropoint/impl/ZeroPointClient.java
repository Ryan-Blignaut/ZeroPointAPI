package me.thesilverecho.zeropoint.impl;

import me.thesilverecho.zeropoint.api.config.Config;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import me.thesilverecho.zeropoint.impl.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class ZeroPointClient implements ClientModInitializer
{
	public static final Config DEFAULT_CONFIG = new Config("Zero-point");
	public static final String MOD_ID = "zero-point";
	@ConfigSetting
	private final int version = 1;

	public static final boolean BLUR = true;

	@Override
	public void onInitializeClient()
	{
		DEFAULT_CONFIG.register(this);
		ZeroPointApiLogger.setUp(true, true, true);
		ModuleManager.registerAllModules();
		DEFAULT_CONFIG.save();
//		DiscordPresence.startRPC();

		Registry.register(Registry.SOUND_EVENT, new Identifier(MOD_ID, "module_on"), MODULE_ON);
		Registry.register(Registry.SOUND_EVENT, new Identifier(MOD_ID, "module_off"), MODULE_OFF);

//		MusicPlayer.INSTANCE.togglePause();
//		MusicPlayer.load();

//		ShaderManager.initShaders();
//		FontManager.initFonts();
	}

	public static final SoundEvent MODULE_ON = new SoundEvent(new Identifier(MOD_ID, "module_on"));
	public static final SoundEvent MODULE_OFF = new SoundEvent(new Identifier(MOD_ID, "module_off"));

}
