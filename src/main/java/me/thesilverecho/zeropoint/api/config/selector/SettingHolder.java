package me.thesilverecho.zeropoint.api.config.selector;

public abstract class SettingHolder<T>
{
	private final String name;
	private final String description;

	private T value;

	public SettingHolder()
	{
		this.name = "";
		this.description = "";
	}

	public SettingHolder(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	public SettingHolder(String name, String description, T value)
	{
		this.name = name;
		this.description = description;
		this.value = value;
	}

	public String getDescription()
	{
		return description;
	}

	public String getName()
	{
		return name;
	}

	public T getValue()
	{
		if (!isValid(value)) return null;
		return value;
	}

	public boolean setValue(T value)
	{
		if (!isValid(value)) return false;
		this.value = value;
		return true;
	}

	public boolean setValueFromSting(String s)
	{
		if (!isValid(value)) return false;
		this.value = deserialize(s);
		return true;
	}


	public abstract String serialize();

	public abstract T deserialize(String value);

	protected abstract boolean isValid(T value);
}
