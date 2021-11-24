package me.thesilverecho.zeropoint.api.render.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import org.apache.commons.io.IOUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL45.*;


public class Texture2D implements AutoCloseable
{
	private int textureId = -1;
	private int width = -1, height = -1;
	private ByteBuffer imageBuffer;

	private void bindTexture()
	{
		GlStateManager._activeTexture(GL_TEXTURE0);
		if (!RenderSystem.isOnRenderThreadOrInit())
			RenderSystem.recordRenderCall(() -> GlStateManager._bindTexture(this.getID()));
		else
			GlStateManager._bindTexture(this.getID());
	}

	public Texture2D(int width, int height, Format format)
	{
		bindTexture();
		GlStateManager._pixelStore(GL_UNPACK_SWAP_BYTES, GL_FALSE);
		GlStateManager._pixelStore(GL_UNPACK_LSB_FIRST, GL_FALSE);
		GlStateManager._pixelStore(GL_UNPACK_ROW_LENGTH, 0);
		GlStateManager._pixelStore(GL_UNPACK_IMAGE_HEIGHT, 0);
		GlStateManager._pixelStore(GL_UNPACK_SKIP_ROWS, 0);
		GlStateManager._pixelStore(GL_UNPACK_SKIP_PIXELS, 0);
		GlStateManager._pixelStore(GL_UNPACK_SKIP_IMAGES, 0);
		GlStateManager._pixelStore(GL_UNPACK_ALIGNMENT, 4);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, format.toOpenGL(), width, height, 0, format.toOpenGL(), GL_UNSIGNED_BYTE, (ByteBuffer) null);
	}

	public Texture2D(int width, int height, ByteBuffer buffer, Format format)
	{
		bindTexture();
		this.width = width;
		this.height = height;
		this.imageBuffer = buffer;

		glPixelStorei(GL_UNPACK_SWAP_BYTES, GL_FALSE);
		glPixelStorei(GL_UNPACK_LSB_FIRST, GL_FALSE);
		glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
		glPixelStorei(GL_UNPACK_IMAGE_HEIGHT, 0);
		glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
		glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
		glPixelStorei(GL_UNPACK_SKIP_IMAGES, 0);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		buffer.rewind();
		glTexImage2D(GL_TEXTURE_2D, 0, format.toOpenGL(), width, height, 0, format.toOpenGL(), GL_UNSIGNED_BYTE, buffer);
	}

	public static Texture2D read(ByteBuffer byteBuffer)
	{
		return read(byteBuffer, Format.RGBA);
	}

	public static Texture2D read(ByteBuffer byteBuffer, Format format)
	{
		final MemoryStack stack = MemoryStack.stackPush();
		final IntBuffer w = stack.mallocInt(1);
		final IntBuffer h = stack.mallocInt(1);
		final IntBuffer channels = stack.mallocInt(1);
		final ByteBuffer imageFromMemory = STBImage.stbi_load_from_memory(byteBuffer, w, h, channels, format.channels);
		if (imageFromMemory == null)
			throw new RuntimeException("Failed to load image: ");
		return new Texture2D(w.get(), h.get(), imageFromMemory, format);
	}

	public static Texture2D read(InputStream inputStream) throws IOException
	{
		ByteBuffer byteBuffer = null;
		Texture2D texture2D;
		try
		{
			byteBuffer = TextureUtil.readResource(inputStream);
			byteBuffer.rewind();
			texture2D = read(byteBuffer, Format.RGBA);
		} finally
		{
			MemoryUtil.memFree(byteBuffer);
			IOUtils.closeQuietly(inputStream);
		}
		return texture2D;
	}

	public void setWrap(int wrapS, int wrapT)
	{
		final int texture = getID();
		glTextureParameteri(texture, GL_TEXTURE_WRAP_S, wrapS);
		glTextureParameteri(texture, GL_TEXTURE_WRAP_T, wrapT);
	}

	public void setFilter(int minFilter, int magFilter)
	{
		final int texture = getID();
		glTextureParameteri(texture, GL_TEXTURE_MIN_FILTER, minFilter);
		glTextureParameteri(texture, GL_TEXTURE_MAG_FILTER, magFilter);
	}

	public int getID()
	{
		if (this.textureId == -1)
			this.textureId = TextureUtil.generateTextureId();

		return this.textureId;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public ByteBuffer getImageBuffer()
	{
		return imageBuffer;
	}

	@Override
	public void close() throws Exception
	{
		if (this.textureId != -1)
		{
			STBImage.nstbi_image_free(this.textureId);
		}
		this.textureId = -1;
	}


	public enum Format
	{
		A(1),
		RGB(3),
		RGBA(4);
		private final int channels;

		Format(int channels) {this.channels = channels;}

		public int toOpenGL()
		{
			return switch (this)
					{
						case A -> GL_RED;
						case RGB -> GL_RGB;
						case RGBA -> GL_RGBA;
					};
		}
	}
}
