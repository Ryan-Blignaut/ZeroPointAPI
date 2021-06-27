package me.thesilverecho.zeropoint.api.event;

import java.lang.reflect.Method;

/**
 * Record {@code MethodData} is used to store data about a dynamically loaded module
 * The source, being the loaded class where the we want to call lies.
 * The target being the method we want to call.
 * The priority is used to determine what method will run first when called.
 */
public record MethodData(Object source, Method target, byte priority)
{
}
