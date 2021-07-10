package me.thesilverecho.zeropoint.api.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

import static org.lwjgl.opengl.GL32C.*;

public class Framebuffer
{
	private int id;
	public int texture;

	public Framebuffer()
	{
		init();
	}

	private void init()
	{
		id = glGenFramebuffers();
		bind();
		final Window window = MinecraftClient.getInstance().getWindow();
		final int framebufferWidth = window.getFramebufferWidth();
		final int framebufferHeight = window.getFramebufferHeight();
		Texture2D texture2D = new Texture2D(framebufferWidth, framebufferHeight, Texture2D.Format.RGB);
		this.texture = texture2D.getID();
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture, 0);
		final int renderbuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, renderbuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, framebufferWidth, framebufferHeight);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderbuffer);

		unbind();
	}

	public void bind()
	{
		MinecraftClient.getInstance().getFramebuffer().endWrite();
		glBindFramebuffer(GL_FRAMEBUFFER, id);
	}

	public void unbind()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
	}

	public void resize()
	{
		glDeleteFramebuffers(id);
		glDeleteTextures(texture);

		init();
	}
}