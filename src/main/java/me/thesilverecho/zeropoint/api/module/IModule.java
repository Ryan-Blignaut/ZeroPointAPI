package me.thesilverecho.zeropoint.api.module;

public interface IModule
{

	/**
	 * Action to be run when enabled
	 */
	void onEnable();

	/**
	 * Action to be run when disabled
	 */
	void onDisable();

}
