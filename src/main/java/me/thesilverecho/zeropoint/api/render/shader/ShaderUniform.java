package me.thesilverecho.zeropoint.api.render.shader;

import me.thesilverecho.zeropoint.api.render.GLWrapper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vector4f;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public final class ShaderUniform
{
	/*	private final Shader shaderId;
		private final String shaderUniformName;
		private final Object value;

		public ShaderUniform(Shader shaderId, String shaderUniformName, Object value)
		{
			this.shaderId = shaderId;
			this.shaderUniformName = shaderUniformName;
			this.value = value;
		}

		public void bind()
		{
			int location = glGetUniformLocation(shaderId.programId, shaderUniformName);
			if (value instanceof final Float floatNum)
				GLWrapper.setShaderBoundFloat(location, floatNum);
			else if (value instanceof final Integer intNum)
				GLWrapper.setShaderBoundInt(location, intNum);
			else if (value instanceof final Vec2f vec2f)
				GLWrapper.setShaderBoundVec2(location, vec2f);
			else if (value instanceof final Vector4f vec4f)
				GLWrapper.setShaderBoundVec4(location, vec4f);
			else if (value instanceof final Matrix4f matrix4f)
				GLWrapper.setShaderBoundMatrix4f(location, matrix4f);
			else
				throw new UnsupportedOperationException("Failed to load data into shader: Unsupported data type.");
		}*/
	private final Shader shaderId;
	private final String shaderUniformName;
	private Object value;

	private int cachedLocation = -5;

	public ShaderUniform(Shader shaderId,
			String shaderUniformName, Object value)
	{
		this.shaderId = shaderId;
		this.shaderUniformName = shaderUniformName;
		this.value = value;
	}

	public void bind()
	{
		//Prevent CPU overhead looking for the location.
		if (this.cachedLocation == -5)
			this.cachedLocation = GL20.glGetUniformLocation(shaderId.programId, shaderUniformName);
		if (value instanceof final Float floatNum)
			GLWrapper.setShaderBoundFloat(cachedLocation, floatNum);
		else if (value instanceof final Integer intNum)
			GLWrapper.setShaderBoundInt(cachedLocation, intNum);
		else if (value instanceof final Vec2f vec2f)
			GLWrapper.setShaderBoundVec2(cachedLocation, vec2f);
		else if (value instanceof final Vector4f vec4f)
			GLWrapper.setShaderBoundVec4(cachedLocation, vec4f);
		else if (value instanceof final Matrix4f matrix4f)
			GLWrapper.setShaderBoundMatrix4f(cachedLocation, matrix4f);
		else if (value instanceof final FloatBuffer floatBuffer)
			GL20.glUniform1fv(cachedLocation, floatBuffer);
//			GLWrapper.setShaderBoundMatrix4f(cachedLocation, matrix4f);
		else
			throw new UnsupportedOperationException("Failed to load data into shader: Unsupported data type.");
	}

	public void setValue(Object value)
	{
		this.value = value;
	}
}
