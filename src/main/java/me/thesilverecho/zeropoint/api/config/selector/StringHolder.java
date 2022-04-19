package me.thesilverecho.zeropoint.api.config.selector;

public class StringHolder extends SettingHolder<String>
{

	public StringHolder(String name, String description, String value)
	{
		super(name, description, value);
	}

	@Override
	public String serialize()
	{
		return getValue();
	}

	@Override
	public String deserialize(String value)
	{
		return getValue();
	}

	@Override
	protected boolean isValid(String value)
	{
		return true;
	}
}
