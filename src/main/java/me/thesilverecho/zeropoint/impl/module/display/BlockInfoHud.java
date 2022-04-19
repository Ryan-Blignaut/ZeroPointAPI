package me.thesilverecho.zeropoint.impl.module.display;

import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;

@ClientModule(name = "Block info", description = "Shows the current looking blocks info")
public class BlockInfoHud extends BaseModule
{
	@ConfigSetting private String blockInfo = "Looking at: %s";

	@Override
	public void onEnable()
	{
		System.out.println(blockInfo);
		super.onEnable();
	}
}
