package me.thesilverecho.zeropoint.api.config.selector;

import me.thesilverecho.zeropoint.api.render.font.CustomFont;

public class FontHolder extends SettingHolder<CustomFont>
{

	public FontHolder(String name, String description, CustomFont value)
	{
		super(name, description, value);
	}

	@Override
	public String serialize()
	{
		return getValue().getName();
	}

	@Override
	public CustomFont deserialize(String value)
	{
		return CustomFont.getFontByName(value);
	}

	@Override
	protected boolean isValid(CustomFont value)
	{
		return true;
	}
}
