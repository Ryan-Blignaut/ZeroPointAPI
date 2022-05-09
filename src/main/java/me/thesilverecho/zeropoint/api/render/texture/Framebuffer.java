package me.thesilverecho.zeropoint.api.render.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer
{
	private static final ArrayList<Framebuffer> LOADED_FRAME_BUFFERS = new ArrayList<>();
	private int id;
	public Texture2D texture, depth;
	private boolean useMipMaps;

	private boolean isBoundTest = false;

	public Framebuffer()
	{
		init(MinecraftClient.getInstance().getFramebuffer().textureWidth, MinecraftClient.getInstance().getFramebuffer().textureHeight, 0);
		LOADED_FRAME_BUFFERS.add(this);
	}

	public Framebuffer(boolean mip)
	{
		this.useMipMaps = true;
		init(MinecraftClient.getInstance().getFramebuffer().textureWidth, MinecraftClient.getInstance().getFramebuffer().textureHeight, 0);
		LOADED_FRAME_BUFFERS.add(this);
	}

	public void copyDepthFrom(net.minecraft.client.gl.Framebuffer framebuffer)
	{
		framebuffer.beginRead();
		GlStateManager._glBindFramebuffer(GL_READ_FRAMEBUFFER, framebuffer.fbo);
//		bind();
		GlStateManager._glBindFramebuffer(GL_DRAW_FRAMEBUFFER, this.id);
		GlStateManager._glBlitFrameBuffer(0, 0, framebuffer.textureWidth, framebuffer.textureHeight, 0, 0, framebuffer.textureWidth, framebuffer.textureHeight, 0x100, 0x2600);
		GlStateManager._glBindFramebuffer(36160, 0);
//		unbind();
	}

	private void init(int width, int height, int location)
	{
		id = glGenFramebuffers();
		bind();
		this.texture = new Texture2D(width, height, Texture2D.Format.RGBA);
		this.texture.setFilter(GL_NEAREST, GL_NEAREST);
		this.texture.setMipmap(useMipMaps);

		this.depth = new Texture2D(width, height, Texture2D.Format.DEPTH);


		final int renderbuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, renderbuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);


		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + location, GL_TEXTURE_2D, this.texture.getID(), 0);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderbuffer);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, this.depth.getID(), 0);



		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
		{
			System.err.println("Frame buffer ran into issue");
		}
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
		MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
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
		glClearColor(0, 0, 0, 0);
//		glClearDepth(0);
		GL11.glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
		this.unbind();
	}


	public static void resizeAllFBOs()
	{
		LOADED_FRAME_BUFFERS.forEach(Framebuffer::resize);
	}

}