package me.thesilverecho.zeropoint.api.config.selector;

public class BooleanHolder extends SettingHolder<Boolean>
{

	public BooleanHolder(String name, String description, Boolean value)
	{
		super(name, description, value);
	}

	@Override
	public String serialize()
	{
		return getValue() ? "True" : "False";
	}

	@Override
	public Boolean deserialize(String value)
	{
		if (value.equalsIgnoreCase("True")) return true;
		else if (value.equalsIgnoreCase("False")) return false;
		else return null;
	}

	@Override
	protected boolean isValid(Boolean value)
	{
		return true;
	}
}
