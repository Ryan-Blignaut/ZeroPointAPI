package me.thesilverecho.zeropoint.api.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.util.ApiIOUtils;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL20.*;


public class Shader
{
	protected int programId;
	private final Identifier fragLocation, vertLocation;

	private final HashMap<String, ShaderUniform> uniformMap = new HashMap<>();

	//	private final List<ShaderUniform> uniforms = Lists.newArrayList();
	private int activeShaderId = -1;

	public Shader(Identifier fragLocation, Identifier vertLocation)
	{
		this.fragLocation = fragLocation;
		this.vertLocation = vertLocation;
	}

	public Optional<String> getShaderString(Identifier location)
	{
		AtomicReference<Optional<String>> optional = new AtomicReference<>(Optional.empty());
		ApiIOUtils.getResourceFromClientPack(location).ifPresent(inputStream ->
		{
			try
			{
				optional.set(Optional.of(IOUtils.toString(inputStream, StandardCharsets.UTF_8)));
			} catch (IOException e)
			{
				ZeroPointApiLogger.error("Shader is not found at location: " + location, e);
				optional.set(Optional.empty());
			}
		});
		return optional.get();
	}

	private int genShader(int glFragmentShader, Identifier loc)
	{
		final int[] programId = {-1};
		getShaderString(loc).ifPresent(shaderSource -> programId[0] = GLWrapper.compileShader(glFragmentShader, shaderSource));
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
		final Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();
		final Matrix4f modelViewMatrix = RenderSystem.getModelViewMatrix();
		return bind(projectionMatrix, modelViewMatrix);
	}


	public Shader bind(Matrix4f projectionMatrix, Matrix4f modelViewMatrix)
	{
		setShaderUniform("ProjMat", projectionMatrix);
		setShaderUniform("ModelViewMat", modelViewMatrix);
		if (this.activeShaderId != this.programId)
		{
			getShader();
			GLWrapper.useShader(programId);
			this.activeShaderId = programId;
		}
		this.uniformMap.forEach((s, shaderUniform) -> shaderUniform.bind());
		return this;
	}

	public void unBind()
	{
		GLWrapper.useShader(0);
		this.activeShaderId = -1;
	}

	public void destroy()
	{
		glDeleteProgram(programId);
//		this.uniformMap.clear();
	}

	private static final HashMap<Identifier, Shader> shaderHashMap = new HashMap<>();

	public Shader getShader()
	{

		shaderHashMap.computeIfAbsent(fragLocation, identifier ->
		{
			this.create();
			return this;
		});
		return shaderHashMap.get(fragLocation);
	}

	public static void resetShaderHashMap()
	{
		System.out.println("shader map cleared ");
		shaderHashMap.forEach((identifier, shader) ->
		{
			shader.uniformMap.clear();
		});
		shaderHashMap.clear();
	}

	public void setShaderUniform(String var, Object value)
	{
		if (!this.uniformMap.containsKey(var)) this.uniformMap.put(var, new ShaderUniform(this, var, value));
		else this.uniformMap.get(var).setValue(value);
	}

}
