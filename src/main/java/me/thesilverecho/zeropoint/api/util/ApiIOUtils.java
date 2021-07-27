package me.thesilverecho.zeropoint.api.util;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public class ApiIOUtils
{
	/**
	 * @param manager
	 * @param identifier
	 * @return
	 */
	public static Optional<InputStream> getResourceByID(ResourceManager manager, Identifier identifier)
	{
		try
		{
			InputStream inputStream = manager.getResource(identifier).getInputStream();
			return Optional.of(inputStream);
		} catch (IOException e)
		{
			ZeroPointApiLogger.error("Error finding resource: " + identifier, e);
		}
		return Optional.empty();
	}

	public static byte[] readBytes(InputStream inputStream)
	{
		try
		{
			return IOUtils.toByteArray(inputStream);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return new byte[0];
	}

	public static ByteBuffer readBytesToBuffer(InputStream inputStream)
	{
		final byte[] bytes = readBytes(inputStream);
		final ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length).put(bytes);
		buffer.flip();
		return buffer;
	}
}
