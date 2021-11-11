package me.thesilverecho.zeropoint.api.render.cursor;

import me.thesilverecho.zeropoint.api.render.texture.Texture2D;
import me.thesilverecho.zeropoint.api.util.ApiIOUtils;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import java.io.IOException;
import java.util.HashMap;

public class CustomCursor
{
	private static final HashMap<Identifier, CustomCursor> CURSORS = new HashMap<>();

	private long cursor;

	private final Identifier location;

	public CustomCursor(Identifier identifier)
	{
		this.location = identifier;
	}

	private void create()
	{
		ApiIOUtils.getResourceFromClientPack(location).ifPresent(stream ->
		{
			try
			{
				final Texture2D text = Texture2D.read(stream);
				final GLFWImage cursorImage = GLFWImage.malloc();
				cursorImage.set(text.getWidth(), text.getHeight(), text.getImageBuffer());
				cursor = GLFW.glfwCreateCursor(cursorImage, 0, 0);
			} catch (IOException e)
			{
				ZeroPointApiLogger.error("Error reading texture for cursor", e);
			}
		});
	}

	public void activateCursor()
	{
		CURSORS.computeIfAbsent(location, s ->
		{
			this.create();
			return this;
		});
		GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), cursor);
	}

}
