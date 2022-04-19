package me.thesilverecho.zeropoint.api.config;

import me.thesilverecho.zeropoint.api.config.selector.SettingHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SettingList
{
	public SettingList()
	{
	}

	private final List<SettingHolder<?>> settings = new ArrayList<>();

	public <T> SettingHolder<T> addItem(SettingHolder<T> setting)
	{
		settings.add(setting);
		return setting;
	}

	public List<SettingHolder<?>> getAllSettings()
	{
		return settings;
	}

	public Optional<SettingHolder<?>> getSetting(String name)
	{
		return settings.stream().filter(settingHolder -> settingHolder.getName().equalsIgnoreCase(name)).findFirst();
	}

}
