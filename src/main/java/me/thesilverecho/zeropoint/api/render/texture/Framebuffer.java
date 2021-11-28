package me.thesilverecho.zeropoint.api.render.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer
{
	private static final ArrayList<Framebuffer> LOADED_FRAME_BUFFERS = new ArrayList<>();
	private int id;
	public int texture;

	public Framebuffer()
	{
		init();
		LOADED_FRAME_BUFFERS.add(this);
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
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
		{
			ZeroPointApiLogger.error("Frame buffer ran into issue");
		}
		unbind();
	}

	public void bind()
	{
//		MinecraftClient.getInstance().getFramebuffer().endWrite();
		GlStateManager._glBindFramebuffer(36160, id);
//		glBindFramebuffer(GL_FRAMEBUFFER, id);
	}

	public void unbind()
	{
		MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
	}

	private void resize()
	{
		glDeleteFramebuffers(id);
		glDeleteTextures(texture);
		init();
	}

	public void clear()
	{
		this.bind();
		GlStateManager._clearColor(1, 1, 1, 1);
		int i = 16384;
		GlStateManager._clear(i, false);
		this.unbind();
	}


	public static void resizeAllFBOs()
	{
		LOADED_FRAME_BUFFERS.forEach(Framebuffer::resize);
	}

}