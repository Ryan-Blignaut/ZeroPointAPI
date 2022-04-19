package me.thesilverecho.zeropoint.api.config.selector;

public class FloatSliderHolder extends SettingHolder<Float>
{

	private final float min, max, increment, defaultValue;

	public FloatSliderHolder(String name, String description, float value, float min, float max, float increment, float defaultValue)
	{
		super(name, description, value);
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.defaultValue = defaultValue;
	}

	public FloatSliderHolder(String name, String description, Float value, float min, float max, float increment)
	{
		super(name, description, value);
		this.defaultValue = value;
		this.min = min;
		this.max = max;
		this.increment = increment;
	}

	@Override
	public String serialize()
	{
		return String.valueOf(getValue());
	}

	@Override
	public Float deserialize(String value)
	{

		float number = defaultValue;
		try
		{
			number = Float.parseFloat(value);
		} catch (Exception ignored)
		{
		}

		if (number < min) number = min;
		else if (number > max) number = max;

		return number;
	}

	@Override
	protected boolean isValid(Float value)
	{
		return true;
	}

	public float getIncrement()
	{
		return increment;
	}
}
