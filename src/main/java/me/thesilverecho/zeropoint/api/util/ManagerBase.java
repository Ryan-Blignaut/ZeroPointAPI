package me.thesilverecho.zeropoint.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ManagerBase<T>
{
	protected final Map<Class<? extends T>, T> classCacheMap = new HashMap<>();
	protected final ArrayList<T> managerArray = new ArrayList<>();

	private final String name;

	public ManagerBase(String name)
	{
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public <Q extends T> Q getClassCacheMap(Class<Q> clazz)
	{
		return (Q) classCacheMap.computeIfAbsent(clazz, this::apply);
	}

	private T apply(Class<? extends T> c)
	{
		return managerArray.stream().filter(data -> data.getClass().equals(c)).findFirst().orElse(null);
	}
}
