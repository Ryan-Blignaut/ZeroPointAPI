package me.thesilverecho.zeropoint.api.render.shader;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

/**
 * Shaders for internal use
 */
public enum APIShaders
{
	MASK_SHADER("mask_text.frag", true), RECT("rect.frag", false), ROUND_RECT("round_rect.frag", false), SHADE_MASK_SHADER("shade_mask_text.frag", "shade_text.vert"), TEXT_SHADER("rect_text.frag", true);
	private final Shader shader;
	private static final String BASE_SHADER_PATH = "shaders/";
	private static final String DEFAULT_COL_VERT_PATH = "colour.vert";
	private static final String DEFAULT_TEXT_VERT_PATH = "colour_text.vert";
//	BiFunction<Identifier, Identifier, Shader> customCreate;

	APIShaders(String frag, boolean textured)
	{
		this(frag, textured ? DEFAULT_TEXT_VERT_PATH : DEFAULT_COL_VERT_PATH);
	}

	APIShaders(String frag, String vert)
	{
		shader = new Shader(new Identifier(ZeroPointClient.MOD_ID, BASE_SHADER_PATH + frag), new Identifier(ZeroPointClient.MOD_ID, BASE_SHADER_PATH + vert));
	}

	public Shader getShader()
	{
		return shader;
	}
}
