package me.thesilverecho.zeropoint.api.render.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer
{
	private static final ArrayList<Framebuffer> LOADED_FRAME_BUFFERS = new ArrayList<>();
	private int id;
	public Texture2D texture;
	private boolean useMipMaps;

	private boolean isBoundTest = false;

	public Framebuffer()
	{
		init(MinecraftClient.getInstance().getFramebuffer().textureWidth, MinecraftClient.getInstance().getFramebuffer().textureHeight, 0);
		LOADED_FRAME_BUFFERS.add(this);
	}


	private void init(int width, int height, int location)
	{
		id = glGenFramebuffers();
		bind();
		this.texture = new Texture2D(width, height, Texture2D.Format.RGBA);
		this.texture.setMipmap(useMipMaps);
		this.texture.setFilter(GL_NEAREST, GL_NEAREST);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + location, GL_TEXTURE_2D, this.texture.getID(), 0);
		final int renderbuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, renderbuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderbuffer);
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			System.err.println("Frame buffer ran into issue");
		unbind();
	}

	public boolean isBoundTest()
	{
		return isBoundTest;
	}

	public void bind()
	{
//		MinecraftClient.getInstance().getFramebuffer().endWrite();
		isBoundTest = true;
		glBindFramebuffer(GL_FRAMEBUFFER, id);
	}

	public void unbind()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		isBoundTest = false;
		MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
	}

	private void resize()
	{
		glDeleteFramebuffers(id);
		glDeleteTextures(texture.getID());
		init(MinecraftClient.getInstance().getFramebuffer().textureWidth, MinecraftClient.getInstance().getFramebuffer().textureHeight, 0);
	}

	public void clear()
	{
		this.bind();
		GlStateManager._clearColor(0, 0, 0, 0);
		int i = 16384;
		GlStateManager._clear(i, false);
		this.unbind();
	}


	public static void resizeAllFBOs()
	{
		LOADED_FRAME_BUFFERS.forEach(Framebuffer::resize);
	}

}