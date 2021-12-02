package me.thesilverecho.zeropoint.api.render.shader;

import me.thesilverecho.zeropoint.api.render.GLWrapper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vector4f;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public final class ShaderUniform
{
	private final Shader shaderId;
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
	}

}
