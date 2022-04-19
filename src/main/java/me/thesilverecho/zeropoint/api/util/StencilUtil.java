package me.thesilverecho.zeropoint.api.util;

import me.thesilverecho.zeropoint.api.mixin.FramebufferAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;

import static org.lwjgl.opengl.GL11.*;

public class StencilUtil
{
	static MinecraftClient mc = MinecraftClient.getInstance();

	public static void checkSetupFBO(Framebuffer framebuffer)
	{
		if (framebuffer != null)
		{
			if (framebuffer.getDepthAttachment() > -1)
			{
				setupFBO(framebuffer);
				((FramebufferAccessor) framebuffer).setDepthAttachment(-1);
			}
		}
	}

	/**
	 * @param framebuffer
	 * @implNote Sets up the Framebuffer for Stencil use
	 */

	public static void setupFBO(Framebuffer framebuffer)
	{
		EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.getDepthAttachment());
		final int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID);
		EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, mc.getFramebuffer().viewportWidth, mc.getFramebuffer().viewportHeight);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID);
	}

	/**
	 * @implNote Initializes the Stencil Buffer to write to
	 */
	public static void initStencilToWrite()
	{
		//init
		mc.getFramebuffer().beginWrite(false);
		checkSetupFBO(mc.getFramebuffer());
		glClear(GL_STENCIL_BUFFER_BIT);
		glEnable(GL_STENCIL_TEST);

		glStencilFunc(GL_ALWAYS, 1, 1);
		glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
		glColorMask(false, false, false, false);
	}

	/**
	 * @param ref (usually 1)
	 * @implNote Reads the Stencil Buffer and stencils it onto everything until
	 * @see StencilUtil#uninitStencilBuffer()  is called
	 */
	public static void readStencilBuffer(int ref)
	{
		glColorMask(true, true, true, true);
		glStencilFunc(GL_EQUAL, ref, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
	}

	public static void uninitStencilBuffer()
	{
		glDisable(GL_STENCIL_TEST);
	}
}
