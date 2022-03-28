package me.thesilverecho.zeropoint.api.render;

import com.mojang.blaze3d.platform.GlStateManager;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL43.*;

public class GLWrapper
{
	private static final FloatBuffer FLOAT_BUFFER = MemoryUtil.memAllocFloat(16);

	public static void enableGL2D()
	{
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//		GL45.glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_SRC_ALPHA);
//		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
//		glBlendFunc(GL_ONE, GL_ZERO);
//		glDepthMask(true);
//		glEnable(GL_LINE_SMOOTH);
//		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
//		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
	}

	public static void disableGL2D()
	{
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
//		glDisable(GL_LINE_SMOOTH);
//		glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
//		glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
	}


	public static void activateTexture(int textureNum, int textureId)
	{
		GlStateManager._activeTexture(GL_TEXTURE0 + textureNum);
		GlStateManager._bindTexture(textureId);
	}


	public static void setShaderBoundInt(int location, int value)
	{
		glUniform1i(location, value);
	}

	public static void setShaderBoundFloat(int location, float value)
	{
		glUniform1f(location, value);
	}

	public static void setShaderBoundVec2(int location, Vec2f value)
	{
		glUniform2f(location, value.x, value.y);
	}

	public static void setShaderBoundVec4(int location, Vector4f value)
	{
		glUniform4f(location, value.getX(), value.getY(), value.getZ(), value.getW());
	}

	public static void setShaderBoundMatrix4f(int location, Matrix4f value)
	{
		FLOAT_BUFFER.position(0);
		value.writeColumnMajor(FLOAT_BUFFER);
		glUniformMatrix4fv(location, false, FLOAT_BUFFER);
	}

	public static void useShader(int programId)
	{
		glUseProgram(programId);
	}

	public static int compileShader(int glFragmentShader, String shaderSource)
	{
		int programId = glCreateShader(glFragmentShader);
		glShaderSource(programId, shaderSource);
		glCompileShader(programId);

		if (glGetShaderi(programId, GL_COMPILE_STATUS) == GL_FALSE)
			ZeroPointApiLogger.error(glGetShaderInfoLog(programId, 100));
		return programId;
	}

	public static void generateShader(int programId)
	{
		glUseProgram(programId);
	}
}
