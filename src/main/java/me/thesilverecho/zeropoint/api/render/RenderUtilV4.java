package me.thesilverecho.zeropoint.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;


public class RenderUtilV4
{
	public static float zIndex;
	public static Shader shader;
	private static int textureId = -1;

	private static APIColour.ColourQuad COLOUR_QUAD_COLOUR_HOLDER = new APIColour.ColourQuad(APIColour.WHITE);

	public static int getTextureFromLocation(Identifier identifier)
	{
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		final AbstractTexture texture = textureManager.getTexture(identifier);
		return texture.getGlId();
	}

	public static void setQuadColourHolder(APIColour newColour)
	{
		COLOUR_QUAD_COLOUR_HOLDER.setTopLeft(newColour);
		COLOUR_QUAD_COLOUR_HOLDER.setTopRight(newColour);
		COLOUR_QUAD_COLOUR_HOLDER.setBottomRight(newColour);
		COLOUR_QUAD_COLOUR_HOLDER.setBottomLeft(newColour);
	}

	public static void setQuadColourHolder(APIColour.ColourQuad newColour)
	{
		COLOUR_QUAD_COLOUR_HOLDER = newColour;
	}

	public static void setShader(Shader shader)
	{
		RenderUtilV4.shader = shader;
	}

	public static void setTextureId(int textureId)
	{
		RenderUtilV4.textureId = textureId;
	}

	public static float getZIndex()
	{
		return zIndex;
	}

	public static void setZIndex(float zIndex)
	{
		RenderUtilV4.zIndex = zIndex;
	}


	public static void setShaderUniform(String var, Object value)
	{
		shader.setShaderUniform(var, value);
	}


	public static void quadTexture(MatrixStack stack, float x, float y, float width, float height)
	{
		quadTexture(stack.peek().getPositionMatrix(), x, y, width, height, 0, 0, 1, 1);
	}

	public static void quadTexture(Matrix4f matrix4f, float x, float y, float width, float height)
	{
		quadTexture(matrix4f, x, y, width, height, 0, 0, 1, 1);
	}

	public static void quadTexture(Matrix4f matrix4f, float x, float y, float width, float height, float u0, float v0, float u1, float v1)
	{

		final BufferBuilder builder = RenderSystem.renderThreadTesselator().getBuffer();
		beginDraw(builder);
		generateQuad(builder, matrix4f, x, y, width, height, u0, v0, u1, v1);
		endDraw(builder);

		renderQuad(builder, true);
	}

	public static void setTexture(int textureId)
	{
		setTexture(0, textureId);
	}

	public static void setTexture(int location, int textureId)
	{
		GLWrapper.activateTexture(location, textureId);
		setShaderUniform("Sampler" + location, location);
	}

	public static void renderQuad(BufferBuilder bufferBuilder, boolean handleBlend)
	{
		if (handleBlend) GLWrapper.enableGL2D();
		shader.bind();
		BufferRenderer.postDraw(bufferBuilder);
		shader.unBind();
//		Destroy current texture.
		GLWrapper.activateTexture(0, 0);
		if (handleBlend) GLWrapper.disableGL2D();
	}

	public static void beginDraw(BufferBuilder bufferBuilder)
	{
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
	}

	public static void endDraw(BufferBuilder bufferBuilder)
	{
		bufferBuilder.end();
	}

	public static void generateQuad(BufferBuilder bufferBuilder, Matrix4f matrix4f, float x, float y, float width, float height, float u0, float v0, float u1, float v1)
	{
		final APIColour tL = COLOUR_QUAD_COLOUR_HOLDER.getTopLeft();
		final APIColour bL = COLOUR_QUAD_COLOUR_HOLDER.getBottomLeft();
		final APIColour bR = COLOUR_QUAD_COLOUR_HOLDER.getTopRight();
		final APIColour tR = COLOUR_QUAD_COLOUR_HOLDER.getBottomRight();
		bufferBuilder.vertex(matrix4f, x, y, zIndex).color(tL.getRed(), tL.getGreen(), tL.getBlue(), tL.getAlpha()).texture(u0, v0).next();
		bufferBuilder.vertex(matrix4f, x, height, zIndex).color(bL.getRed(), bL.getGreen(), bL.getBlue(), bL.getAlpha()).texture(u0, v1).next();
		bufferBuilder.vertex(matrix4f, width, height, zIndex).color(bR.getRed(), bR.getGreen(), bR.getBlue(), bR.getAlpha()).texture(u1, v1).next();
		bufferBuilder.vertex(matrix4f, width, y, zIndex).color(tR.getRed(), tR.getGreen(), tR.getBlue(), tR.getAlpha()).texture(u1, v0).next();
	}

	public static void generateLayerQuad(VertexConsumer consumer, Matrix4f matrix4f, float x, float y, float width, float height, float u0, float v0, float u1, float v1)
	{
		final APIColour tL = COLOUR_QUAD_COLOUR_HOLDER.getTopLeft();
		final APIColour bL = COLOUR_QUAD_COLOUR_HOLDER.getBottomLeft();
		final APIColour bR = COLOUR_QUAD_COLOUR_HOLDER.getTopRight();
		final APIColour tR = COLOUR_QUAD_COLOUR_HOLDER.getBottomRight();
		consumer.vertex(matrix4f, x, y, zIndex).color(tL.getRed(), tL.getGreen(), tL.getBlue(), tL.getAlpha()).texture(u0, v0).next();
		consumer.vertex(matrix4f, x, height, zIndex).color(bL.getRed(), bL.getGreen(), bL.getBlue(), bL.getAlpha()).texture(u0, v1).next();
		consumer.vertex(matrix4f, width, height, zIndex).color(bR.getRed(), bR.getGreen(), bR.getBlue(), bR.getAlpha()).texture(u1, v1).next();
		consumer.vertex(matrix4f, width, y, zIndex).color(tR.getRed(), tR.getGreen(), tR.getBlue(), tR.getAlpha()).texture(u1, v0).next();
	}
}