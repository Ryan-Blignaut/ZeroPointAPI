package me.thesilverecho.zeropoint.api.render;

import com.mojang.blaze3d.platform.GlStateManager;
import me.thesilverecho.zeropoint.api.render.texture.Texture2D;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteTextures;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30C.glCheckFramebufferStatus;

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

	public void resize()
	{
		glDeleteFramebuffers(id);
		glDeleteTextures(texture);

		init();
	}
}