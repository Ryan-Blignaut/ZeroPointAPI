
package me.thesilverecho.zeropoint.api.util;

/**
 * Record {@code Pair} is simple implementation of a two value storage system where the values are not specific to type.
 *
 * @param <K> the key variable type
 * @param <V> the value variable type
 */
public record Pair<K, V>(K key, V value)
{
}
