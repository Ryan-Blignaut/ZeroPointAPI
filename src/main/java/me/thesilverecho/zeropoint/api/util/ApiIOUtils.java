package me.thesilverecho.zeropoint.api.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public class ApiIOUtils
{


	public static Optional<InputStream> getResourceFromClientPack(Identifier location)
	{
//		final DefaultResourcePack PACK = MinecraftClient.getInstance().getResourcePackProvider().getPack();
//		try
//		{
//			return Optional.of(PACK.open(ResourceType.CLIENT_RESOURCES, location));
//		} catch (IOException e)
//		{
//			ZeroPointApiLogger.error("Error finding resource: " + location, e);
//		}

		try
		{
			InputStream inputStream = MinecraftClient.getInstance().getResourceManager().getResource(location).getInputStream();
			return Optional.of(inputStream);
		} catch (IOException e)
		{
			ZeroPointApiLogger.error("Error finding resource: " + location, e);
		}
		return Optional.empty();
	}

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
