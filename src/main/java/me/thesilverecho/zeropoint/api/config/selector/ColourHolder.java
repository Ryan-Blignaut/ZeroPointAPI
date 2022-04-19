package me.thesilverecho.zeropoint.api.config.selector;

import me.thesilverecho.zeropoint.api.util.APIColour;

public class ColourHolder extends SettingHolder<APIColour>
{

	public ColourHolder(String name, String description, APIColour value)
	{
		super(name, description, value);
	}

	@Override
	public String serialize()
	{
		return getValue().encode();
	}

	@Override
	public APIColour deserialize(String value)
	{
		return APIColour.decode(value);
	}

	@Override
	protected boolean isValid(APIColour value)
	{
		return true;
	}
}
