package me.thesilverecho.zeropoint.api.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil
{
	public static float zIndex;
	public static Shader shader;

	public static void quad(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder color)
	{
		quad(matrixStack, x, y, width, height, color, color, color, color);
	}

	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u, float v, float u1, float v1, ColourHolder color)
	{
		quadTexture(matrixStack, x, y, width, height, u, v, u1, v1, color, color, color, color);

	}

	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u0, float v0, float u1, float v1, ColourHolder cTopLeft, ColourHolder cTopRight, ColourHolder cBottomRight, ColourHolder cBottomLeft)
	{
		enableGL2D();
		final Matrix4f matrix4f = matrixStack.peek().getModel();
		final BufferBuilder builder = Tessellator.getInstance().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		builder.vertex(matrix4f, x, y, zIndex).color(cTopLeft.red(), cTopLeft.green(), cTopLeft.blue(), cTopLeft.alpha()).texture(u0, v0).next();
		builder.vertex(matrix4f, x, height, zIndex).color(cBottomLeft.red(), cBottomLeft.green(), cBottomLeft.blue(), cBottomLeft.alpha()).texture(u0, v1).next();
		builder.vertex(matrix4f, width, height, zIndex).color(cBottomRight.red(), cBottomRight.green(), cBottomRight.blue(), cBottomRight.alpha()).texture(u1, v1).next();
		builder.vertex(matrix4f, width, y, zIndex).color(cTopRight.red(), cTopRight.green(), cTopRight.blue(), cTopRight.alpha()).texture(u1, v0).next();
		builder.end();
		shader.bind();
		BufferRenderer.postDraw(builder);
		shader.unBind();
		disableGL2D();

	}


	public static void quad(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder cTopLeft, ColourHolder cTopRight, ColourHolder cBottomRight, ColourHolder cBottomLeft)
	{
		enableGL2D();
		final Matrix4f matrix4f = matrixStack.peek().getModel();
		final BufferBuilder builder = Tessellator.getInstance().getBuffer();
		builder.vertex(matrix4f, x, y, zIndex).color(cTopLeft.red(), cTopLeft.green(), cTopLeft.blue(), cTopLeft.alpha()).next();
		builder.vertex(matrix4f, x, y + height, zIndex).color(cBottomLeft.red(), cBottomLeft.green(), cBottomLeft.blue(), cBottomLeft.alpha()).next();
		builder.vertex(matrix4f, x + width, y + height, zIndex).color(cBottomRight.red(), cBottomRight.green(), cBottomRight.blue(), cBottomRight.alpha()).next();
		builder.vertex(matrix4f, x + width, y, zIndex).color(cTopRight.red(), cTopRight.green(), cTopRight.blue(), cTopRight.alpha()).next();
		builder.end();
		shader.bind();
		BufferRenderer.postDraw(builder);
		shader.unBind();
		disableGL2D();
	}

	public static void setShaderColour(ColourHolder shaderColour)
	{

	}

	public static void setShaderTexture(int id)
	{
		GlStateManager._bindTexture(id);
		GL20.glUniform1i(0, 0);
		RenderSystem.activeTexture(GL43.GL_TEXTURE0);
	}

	public static void setShader(Shader shader)
	{
		RenderUtil.shader = shader;
	}

	public static void enableGL2D()
	{
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthMask(true);
		glEnable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
	}

	public static void disableGL2D()
	{
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
	}

}
