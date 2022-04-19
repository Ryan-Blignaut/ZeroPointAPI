package me.thesilverecho.zeropoint.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;


public class RenderUtilV3
{
	public static float zIndex;
	public static Shader shader;
	private static int textureId = -1;
	private static final APIColour.ColourQuad COLOUR_QUAD_COLOUR_HOLDER = new APIColour.ColourQuad(APIColour.WHITE);

	public static void setShaderUniform(String var, Object value)
	{
		shader.setShaderUniform(var, value);
	}

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

	public static void setShader(Shader shader)
	{
		RenderUtilV3.shader = shader;
	}

	public static void setTextureId(int textureId)
	{
		RenderUtilV3.textureId = textureId;
	}

	public static float getZIndex()
	{
		return zIndex;
	}

	public static void setZIndex(float zIndex)
	{
		RenderUtilV3.zIndex = zIndex;
	}

	public static void applyTextureToShader()
	{
		GLWrapper.activateTexture(0, textureId);
		shader.setShaderUniform("Sampler0", 0);
	}

	public static void quadTextureHorizontal(Matrix4f matrix4f, float x1, float y1, float z1, float x2, float y2, float z2)
	{
		quadTextureHorizontal(matrix4f, x1, y1, z1, x2, y2, z2, 0, 0, 1, 1);
	}

	public static void quadTextureVertical(Matrix4f matrix4f, float x1, float y1, float z1, float x2, float y2, float z2)
	{
		quadTextureVertical(matrix4f, x1, y1, z1, x2, y2, z2, 0, 0, 1, 1);
	}

	public static void quadTextureHorizontal(Matrix4f matrix4f, float x1, float y1, float z1, float x2, float y2, float z2, float u0, float v0, float u1, float v1)
	{
		quadTexture(matrix4f, x1, y1, z1, x2, y2, z2, u0, v0, u1, v1, true);
	}

	public static void quadTextureVertical(Matrix4f matrix4f, float x1, float y1, float z1, float x2, float y2, float z2, float u0, float v0, float u1, float v1)
	{
		quadTexture(matrix4f, x1, y1, z1, x2, y2, z2, u0, v0, u1, v1, false);
	}

	public static void quadTexture(Matrix4f matrix4f, float x1, float y1, float z1, float x2, float y2, float z2, float u0, float v0, float u1, float v1, boolean vertical)
	{
		final BufferBuilder builder = RenderSystem.renderThreadTesselator().getBuffer();
		final APIColour tL = COLOUR_QUAD_COLOUR_HOLDER.getTopLeft();
		final APIColour bL = COLOUR_QUAD_COLOUR_HOLDER.getBottomLeft();
		final APIColour bR = COLOUR_QUAD_COLOUR_HOLDER.getTopRight();
		final APIColour tR = COLOUR_QUAD_COLOUR_HOLDER.getBottomRight();

		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		builder.vertex(matrix4f, x1, y1, z1).color(tL.getRed(), tL.getGreen(), tL.getBlue(), tL.getAlpha()).texture(u0, v0).next();
		builder.vertex(matrix4f, x1, y2, vertical ? z2 : z1).color(bL.getRed(), bL.getGreen(), bL.getBlue(), bL.getAlpha()).texture(u0, v1).next();
		builder.vertex(matrix4f, x2, y2, z2).color(bR.getRed(), bR.getGreen(), bR.getBlue(), bR.getAlpha()).texture(u1, v1).next();
		builder.vertex(matrix4f, x2, y1, vertical ? z1 : z2).color(tR.getRed(), tR.getGreen(), tR.getBlue(), tR.getAlpha()).texture(u1, v0).next();
		builder.end();

		applyTextureToShader();
		shader.bind();
		BufferRenderer.postDraw(builder);
		GLWrapper.activateTexture(0, 0);
		shader.unBind();
	}


	public static void quadFramebuffer(float x, float y, float w, float h, float u0, float v0, float u1, float v1)
	{
//		GLWrapper.enableGL2D();
		final BufferBuilder builder = RenderSystem.renderThreadTesselator().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		final Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		final Matrix4f matrix4f = Matrix4f.projectionMatrix((float) framebuffer.textureWidth, (float) (-framebuffer.textureHeight), 0, 300.0F);
		shader.bind(matrix4f, Matrix4f.translate(0.0F, 0.0F, -2000.0F));
		RenderSystem.setProjectionMatrix(matrix4f);

		builder.vertex(x, y, zIndex).texture(u0, v0).next();
		builder.vertex(x, w, zIndex).texture(u0, v1).next();
		builder.vertex(w, h, zIndex).texture(u1, v1).next();
		builder.vertex(w, y, zIndex).texture(u1, v0).next();
		builder.end();
		BufferRenderer.postDraw(builder);
		shader.unBind();
//		GLWrapper.disableGL2D();

	}


}
