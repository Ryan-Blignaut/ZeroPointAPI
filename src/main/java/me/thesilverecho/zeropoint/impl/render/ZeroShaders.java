package me.thesilverecho.zeropoint.impl.render;

import me.thesilverecho.zeropoint.api.render.shader.BlurShader;
import me.thesilverecho.zeropoint.api.render.shader.MaskTextShader;
import me.thesilverecho.zeropoint.api.render.shader.PosTextShader;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.util.ReflectionUtil;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.util.Identifier;

public enum ZeroShaders
{



	ROUND_RECT("round_rect", false), BLUR_SHADER("blur", "defaultpp", BlurShader.class), TEXT_SHADER("rect_text", "colour_text", PosTextShader.class),MASK_SHADER("mask_text", "colour_text", MaskTextShader.class);

	Shader shader;

	private static final String BASE_ID = ZeroPointClient.MOD_ID;
	private static final String BASE_SHADER_PATH = "shaders/";
	private static final String COLOUR_VERT = "colour";
	private static final String TEXTURE_VERT = "colour_text";


	ZeroShaders(String fragLoc, String vertLoc)
	{
		this(new Identifier(BASE_ID, BASE_SHADER_PATH + fragLoc + ".frag"), new Identifier(BASE_ID, BASE_SHADER_PATH + vertLoc + ".vert"));
	}

	ZeroShaders(Identifier fragLoc, Identifier vertLoc)
	{
		this.shader = new Shader(fragLoc, vertLoc);
	}

	ZeroShaders(String fragLoc, boolean isTextured)
	{
		this(new Identifier(BASE_ID, BASE_SHADER_PATH + fragLoc + ".frag"), new Identifier(BASE_ID, BASE_SHADER_PATH + (isTextured ? TEXTURE_VERT : COLOUR_VERT) + ".vert"));

	}

	<T extends Shader> ZeroShaders(String fragLoc, String vertLoc, Class<T> blurShaderClass)
	{
		ReflectionUtil.callConstructor(blurShaderClass, Shader.class, new Identifier(BASE_ID, BASE_SHADER_PATH + fragLoc + ".frag"), new Identifier(BASE_ID, BASE_SHADER_PATH + vertLoc + ".vert"))
		              .ifPresent(t -> this.shader = t);
	}

	public Shader getShader()
	{
		return shader;
	}
}
