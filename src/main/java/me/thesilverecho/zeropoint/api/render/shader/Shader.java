package me.thesilverecho.zeropoint.api.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vector4f;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
	protected int programId;
	private final Identifier fragLocation;
	private final Identifier vertLocation;
	private static final FloatBuffer FLOAT_BUFFER = MemoryUtil.memAllocFloat(16);

	public Shader(Identifier fragLocation, Identifier vertLocation)
	{
		ZeroPointApiLogger.error(fragLocation);
		ZeroPointApiLogger.error(vertLocation);

		this.fragLocation = fragLocation;
		this.vertLocation = vertLocation;
	}

	public Optional<String> getShaderString(Identifier location)
	{
//		final InputStream inputStream = MinecraftClient.getInstance().getResourceManager().getResource(location).getInputStream();
//		final InputStream open = MinecraftClient.getInstance().getResourcePackProvider().getPack().open(ResourceType.CLIENT_RESOURCES, location);
		try (InputStream is = MinecraftClient.getInstance().getResourceManager().getResource(location).getInputStream())
		{
			return Optional.of(IOUtils.toString(is, StandardCharsets.UTF_8));
		} catch (IOException ignored)
		{
			System.err.println("is not found");
			return Optional.empty();
		}
	}

	private int genShader(int glFragmentShader, Identifier loc)
	{
		final int[] programId = {-1};
		getShaderString(loc).ifPresent(shaderSource ->
		{
			programId[0] = glCreateShader(glFragmentShader);
			glShaderSource(programId[0], shaderSource);
			glCompileShader(programId[0]);

			if (glGetShaderi(programId[0], GL_COMPILE_STATUS) == GL_FALSE)
				ZeroPointApiLogger.error(glGetShaderInfoLog(programId[0], 100));
		});
		return programId[0];
	}


	public void create()
	{
		int vertId = genShader(GL_VERTEX_SHADER, vertLocation);
		int fragId = genShader(GL_FRAGMENT_SHADER, fragLocation);

		programId = glCreateProgram();
		apply(vertId, id -> glAttachShader(programId, id));
		apply(fragId, id -> glAttachShader(programId, id));
		glLinkProgram(programId);

		apply(vertId, GL20::glDeleteShader);
		apply(fragId, GL20::glDeleteShader);

		if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE)
			ZeroPointApiLogger.error(GL20.glGetProgramInfoLog(programId));

		glValidateProgram(programId);

		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE)
			ZeroPointApiLogger.error(GL20.glGetProgramInfoLog(programId));


	}

	private void apply(int id, Consumer<Integer> consumer)
	{
		if (id != -1) consumer.accept(id);
	}


	public Shader bind()
	{
		glUseProgram(programId);
		setArgument("ModelViewMat", RenderSystem.getModelViewMatrix());
		setArgument("ProjMat", RenderSystem.getProjectionMatrix());
		return this;
	}

	public void unBind()
	{
		glUseProgram(0);
	}

	public void destroy()
	{
		glDeleteProgram(programId);
	}

	public void setArgument(String var, Object value)
	{
		int location = GL20.glGetUniformLocation(this.programId, var);
		if (value instanceof final Float floatNum)
			glUniform1f(location, floatNum);
		else if (value instanceof final Integer intNum)
			glUniform1i(location, intNum);
		else if (value instanceof final Vec2f vec2f)
			glUniform2f(location, vec2f.x, vec2f.y);
		else if (value instanceof final Vector4f vec)
			GL20.glUniform4f(location, vec.getX(), vec.getY(), vec.getZ(), vec.getW());
		else if (value instanceof final Matrix4f matrix4f)
		{
			FLOAT_BUFFER.position(0);
			matrix4f.writeColumnMajor(FLOAT_BUFFER);
			GL20.glUniformMatrix4fv(location, false, FLOAT_BUFFER);
		} else
			throw new UnsupportedOperationException("Failed to load data into shader: Unsupported data type.");

	}

}
