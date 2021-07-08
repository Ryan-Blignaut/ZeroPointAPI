package me.thesilverecho.zeropoint.api.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class IOUtils
{

	public static Optional<InputStream> getResourceByID(Identifier identifier)
	{
		return getResourceByID(MinecraftClient.getInstance().getResourceManager(), identifier);
	}

	public static Optional<InputStream> getResourceByID(ResourceManager manager, Identifier identifier)
	{
		try
		{
			ZeroPointApiLogger.error(identifier);
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
			return org.apache.commons.io.IOUtils.toByteArray(inputStream);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
//		try (ByteArrayOutputStream out = new ByteArrayOutputStream())
//		{
//			byte[] buffer = new byte[256];
//			int read;
//			while ((read = inputStream.read(buffer)) > 0)
//				out.write(buffer, 0, read);
//			inputStream.close();
//			return out.toByteArray();
//		} catch (IOException e)
//		{
//			ZeroPointApiLogger.error("Error reading bytes from input stream: ", e);
//		}
		return new byte[0];
	}

}
