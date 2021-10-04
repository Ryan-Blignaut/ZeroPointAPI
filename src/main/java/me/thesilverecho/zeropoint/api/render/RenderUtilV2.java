package me.thesilverecho.zeropoint.api.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vector4f;
import org.lwjgl.opengl.GL43;

import java.util.ArrayList;
import java.util.function.Consumer;


public class RenderUtilV2
{
	public static float zIndex;
	public static Shader shader;
	private static int textureId = -1;
	private static Consumer<Shader> afterBindTasks = shader ->
	{
	};

	/*-----------------------2D QUAD DRAW---------------------*/
	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder color)
	{
		quadTexture(matrixStack, x, y, x + width, y + height, 0, 0, 1, 1, color, color, color, color);
	}

	public static void quad(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder color)
	{
		quad(matrixStack, x, y, x + width, y + height, color, color, color, color);
	}

	public static int getTextureFromLocation(Identifier identifier)
	{
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		final AbstractTexture texture = textureManager.getTexture(identifier);
		return texture.getGlId();
	}

	public static void setAfterBindTasks(Consumer<Shader> afterBindTasks)
	{
		RenderUtilV2.afterBindTasks = afterBindTasks;
	}

	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u0, float v0, float u1, float v1, ColourHolder cTopLeft, ColourHolder cTopRight, ColourHolder cBottomRight, ColourHolder cBottomLeft)
	{
		quadTexture(matrixStack.peek().getModel(), x, y, width, height, u0, v0, u1, v1, cTopLeft, cTopRight, cBottomRight, cBottomLeft);
	}

	public static void quadTexture(Matrix4f matrix4f, float x, float y, float width, float height, float u0, float v0, float u1, float v1, ColourHolder cTopLeft, ColourHolder cTopRight, ColourHolder cBottomRight, ColourHolder cBottomLeft)
	{
		GLWrapper.enableGL2D();
		if (textureId != -1)
		{
			final BufferBuilder builder = Tessellator.getInstance().getBuffer();
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
			builder.vertex(matrix4f, x, y, zIndex).color(cTopLeft.red(), cTopLeft.green(), cTopLeft.blue(), cTopLeft.alpha()).texture(u0, v0).next();
			builder.vertex(matrix4f, x, height, zIndex).color(cBottomLeft.red(), cBottomLeft.green(), cBottomLeft.blue(), cBottomLeft.alpha()).texture(u0, v1).next();
			builder.vertex(matrix4f, width, height, zIndex).color(cBottomRight.red(), cBottomRight.green(), cBottomRight.blue(), cBottomRight.alpha()).texture(u1, v1).next();
			builder.vertex(matrix4f, width, y, zIndex).color(cTopRight.red(), cTopRight.green(), cTopRight.blue(), cTopRight.alpha()).texture(u1, v0).next();
			builder.end();
			shader.bind();
			afterBindTasks/*.andThen(RenderUtilV2::applyTextureToShader)*/.accept(shader);
			applyTextureToShader(shader);
			BufferRenderer.postDraw(builder);
			shader.unBind();
		}
		GLWrapper.disableGL2D();
	}

	public static void quadFramebuffer(float x, float y, float width, float height, ColourHolder color)
	{
		quadFramebuffer(x, y, x + width, y + height, 0, 0, 1, 1, color, color, color, color);
	}

	public static void quadFramebuffer(float x, float y, float width, float height, float u0, float v0, float u1, float v1, ColourHolder cTopLeft, ColourHolder cTopRight, ColourHolder cBottomRight, ColourHolder cBottomLeft)
	{
		GLWrapper.enableGL2D();
		if (textureId != -1)
		{
			final BufferBuilder builder = Tessellator.getInstance().getBuffer();
			RenderSystem.depthFunc(519);
			final Framebuffer output = MinecraftClient.getInstance().getFramebuffer();
			float f = (float) output.textureWidth;
			float g = (float) output.textureHeight;

			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
			builder.vertex(0.0D, 0.0D, 500.0D).next();
			builder.vertex(f, 0.0D, 500.0D).next();
			builder.vertex(f, g, 500.0D).next();
			builder.vertex(0.0D, g, 500.0D).next();
			builder.end();
			shader.bind();
			afterBindTasks.accept(shader);
			applyTextureToShader(shader);
			BufferRenderer.postDraw(builder);
			shader.unBind();
		}
		GLWrapper.disableGL2D();
	}


	public static void quad(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder cTopLeft, ColourHolder cTopRight, ColourHolder cBottomRight, ColourHolder cBottomLeft)
	{
		GLWrapper.enableGL2D();
		final Matrix4f matrix4f = matrixStack.peek().getModel();
		final BufferBuilder builder = Tessellator.getInstance().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		builder.vertex(matrix4f, x, y, zIndex).color(cTopLeft.red(), cTopLeft.green(), cTopLeft.blue(), cTopLeft.alpha()).next();
		builder.vertex(matrix4f, x, height, zIndex).color(cBottomLeft.red(), cBottomLeft.green(), cBottomLeft.blue(), cBottomLeft.alpha()).next();
		builder.vertex(matrix4f, width, height, zIndex).color(cBottomRight.red(), cBottomRight.green(), cBottomRight.blue(), cBottomRight.alpha()).next();
		builder.vertex(matrix4f, width, y, zIndex).color(cTopRight.red(), cTopRight.green(), cTopRight.blue(), cTopRight.alpha()).next();
		builder.end();
		shader.bind();
		afterBindTasks.accept(shader);
		BufferRenderer.postDraw(builder);
		shader.unBind();
		GLWrapper.disableGL2D();
	}

	public static void setShader(Shader shader)
	{
		RenderUtilV2.shader = shader;
	}

	public static void setTextureId(int textureId)
	{
		RenderUtilV2.textureId = textureId;
	}

	public static float getZIndex()
	{
		return zIndex;
	}

	public static void setZIndex(float zIndex)
	{
		RenderUtilV2.zIndex = zIndex;
	}

	public static void applyTextureToShader(Shader shader)
	{
		GlStateManager._bindTexture(textureId);
//		GL11.glBindTexture(3553, textureId);
		RenderSystem.activeTexture(GL43.GL_TEXTURE0);
		shader.setArgument("u_Texture", 0);
	}

	public static void rectangle(MatrixStack matrixStack, float x, float y, float width, float height, float radius, ColourHolder colourHolder)
	{
		setShader(APIShaders.RECTANGLE_SHADER.getShader());
		setAfterBindTasks(shader -> shader.setArgument("u_Radius", new Vec2f(radius, radius)));
		quad(matrixStack, x, y, width, height, colourHolder);
	}

	public static void rectangle(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder colourHolder)
	{
		RenderUtilV2.rectangle(matrixStack, x, y, width, height, 0, colourHolder);
	}

	public static void roundRect(MatrixStack matrixStack, float x, float y, float width, float height, float radius, ColourHolder colourHolder)
	{
		RenderUtilV2.roundRect(matrixStack, x, y, width, height, x + radius, y + radius, x + width - radius, y + height - radius, radius, colourHolder);
	}

	public static void roundRect(MatrixStack matrixStack, float x, float y, float width, float height, float innerX, float innerY, float innerWidth, float innerHeight, float radius, ColourHolder colourHolder)
	{
		RenderUtilV2.setShader(APIShaders.ROUND_RECTANGLE_SHADER.getShader());
		RenderUtilV2.setAfterBindTasks(shader ->
		{
			shader.setArgument("u_Radius", new Vec2f(radius, 1));
			shader.setArgument("u_InnerRect", new Vector4f(innerX, innerY, innerWidth, innerHeight));
		});
		RenderUtilV2.quad(matrixStack, x, y, width, height, colourHolder);
	}

	public static void circle(MatrixStack matrixStack, float x, float y, float width, float height, float radius, ColourHolder colourHolder)
	{
		circle(matrixStack, x, y, width, height, radius, 1, colourHolder);
	}

	public static void circle(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int feather, ColourHolder colourHolder)
	{
		RenderUtilV2.setShader(APIShaders.CIRCLE_SHADER.getShader());
		RenderUtilV2.setAfterBindTasks(shader ->
		{
			shader.setArgument("u_Radius", new Vec2f(radius, Math.min(radius, feather)));
			shader.setArgument("u_Center_Pos", new Vec2f(x + height / 2, y + width / 2));
		});
		RenderUtilV2.quad(matrixStack, x, y, width, height, colourHolder);
	}


	public static void hexagon(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder colourHolder)
	{
		setShader(APIShaders.HEXAGON_SHADER.getShader());
		shader.setArgument("u_Center_Pos", new Vec2f(x + height / 2, y + width / 2));
		quad(matrixStack, x, y, width, height, colourHolder);
	}

	//	TODO: change this so that we dont need a width and height, using midPoint for the max/min y of the quad
	public static void bezier(MatrixStack matrixStack, float x, float y, Vec2f point1, Vec2f midPoint, Vec2f point2, float thickness, ColourHolder colourHolder)
	{
		final Vec2f offset = new Vec2f(x, y);
		bezier(matrixStack, x, y, point2.add(offset).x + thickness * 10, midPoint.add(offset).y + thickness * 10, point1.add(offset), midPoint.add(offset), point2.add(offset), thickness, colourHolder);
	}

	public static void bezier(MatrixStack matrixStack, float x, float y, float width, float height, Vec2f point1, Vec2f midPoint, Vec2f point2, float thickness, ColourHolder colourHolder)
	{
		setShader(APIShaders.BEZIER_SHADER.getShader());
		setAfterBindTasks(shader ->
		{
			shader.setArgument("point1", point1);
			shader.setArgument("point2", midPoint);
			shader.setArgument("point3", point2);
			shader.setArgument("thickness", thickness);
			shader.setArgument("smoothR", 0.6f);

		});
		quad(matrixStack, x, y, width, height, colourHolder);
	}


	public static void multiBezier(MatrixStack matrixStack, float x, float y, float width, float height, ArrayList<Vec2f> points, float thickness, ColourHolder colourHolder)
	{

		for (int i = 1; i < points.size() - 2; i++)
		{
			final Vec2f point1 = points.get(i);
			final Vec2f point2 = points.get(i + 1);
			final Vec2f point3 = point1.add(point2).multiply(0.5f);
			bezier(matrixStack, x, y, width, height, point1, point2, point3, thickness, colourHolder);
			if (i == 1)
			{
				bezier(matrixStack, x, y, width, height, points.get(0), point2, point3, thickness, colourHolder);
			}
			if (i == points.size() - 2)
			{
				bezier(matrixStack, x, y, width, height, point1, point2, points.get(points.size() - 1), thickness, colourHolder);
			}
		}

	}


	public static void rectangleFbo(float x, float y, float width, float height, float radius, int textureLoc, ColourHolder colourHolder)
	{
		setShader(APIShaders.BLUR_RECTANGLE_SHADER.getShader());
		setTextureId(textureLoc);
		quadFramebuffer(x, y, width, height, colourHolder);
	}


	public static void rectangleTexture(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int textureLoc, ColourHolder colourHolder)
	{
		setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		setTextureId(textureLoc);
		setAfterBindTasks(shader -> shader.setArgument("u_Radius", new Vec2f(radius, 0)));
		quadTexture(matrixStack, x, y, width, height, colourHolder);
	}

	public static void rectangleTexture(MatrixStack matrixStack, float x, float y, float width, float height, int textureLoc, ColourHolder colourHolder)
	{
		RenderUtilV2.rectangleTexture(matrixStack, x, y, width, height, 0, textureLoc, colourHolder);
	}

	public static void roundRectTexture(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int textureLoc, ColourHolder colourHolder)
	{
		RenderUtilV2.roundRectTexture(matrixStack, x, y, width, height, x + radius, y + radius, x + width - radius, y + height - radius, radius, textureLoc, colourHolder);
	}

	public static void roundRectTexture(MatrixStack matrixStack, float x, float y, float width, float height, float innerX, float innerY, float innerWidth, float innerHeight, float radius, int textureLoc, ColourHolder colourHolder)
	{
		RenderUtilV2.setShader(APIShaders.ROUND_RECTANGLE_TEXTURE_SHADER.getShader());
		RenderUtilV2.setTextureId(textureLoc);
		RenderUtilV2.setAfterBindTasks(shader ->
		{
			shader.setArgument("u_Radius", new Vec2f(radius, 1));
			shader.setArgument("u_InnerRect", new Vector4f(innerX, innerY, innerWidth, innerHeight));
		});
		RenderUtilV2.quadTexture(matrixStack, x, y, width, height, colourHolder);
	}

	public static void circleTexture(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int textureLoc, ColourHolder colourHolder)
	{
		circleTexture(matrixStack, x, y, width, height, radius, 1, textureLoc, colourHolder);
	}

	public static void circleTexture(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int feather, int textureLoc, ColourHolder colourHolder)
	{
		RenderUtilV2.setShader(APIShaders.CIRCLE_TEXTURE_SHADER.getShader());
		RenderUtilV2.setTextureId(textureLoc);
		RenderUtilV2.setAfterBindTasks(shader ->
		{
			shader.setArgument("u_Radius", new Vec2f(radius, Math.min(radius, feather)));
			shader.setArgument("u_Center_Pos", new Vec2f(x + height / 2, y + width / 2));
		});
		RenderUtilV2.quadTexture(matrixStack, x, y, width, height, colourHolder);
	}

	public static double getAnimationState(double animation, double finalState, double speed)
	{
		float add = (float) (0.055 * speed);
		return animation < finalState ? (Math.min(animation + (double) add, finalState)) : (Math.max(animation - (double) add, finalState));
	}
}
