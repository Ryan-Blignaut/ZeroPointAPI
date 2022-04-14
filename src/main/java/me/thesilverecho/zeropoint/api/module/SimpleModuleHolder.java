package me.thesilverecho.zeropoint.api.module;

public final class SimpleModuleHolder
{
	private final String name;
	private final int key;
	private final boolean enabled;

	public SimpleModuleHolder(String name, int key, boolean enabled)
	{
		this.name = name;
		this.key = key;
		this.enabled = enabled;
	}

	public SimpleModuleHolder(BaseModule baseModule)
	{
		this.name = baseModule.getName();
		this.key = baseModule.getKeybind().getKeyCode();
		this.enabled = !baseModule.isEnabled();
	}

	public String getName() {return name;}

	public int getKey() {return key;}

	public boolean isEnabled() {return enabled;}


}
