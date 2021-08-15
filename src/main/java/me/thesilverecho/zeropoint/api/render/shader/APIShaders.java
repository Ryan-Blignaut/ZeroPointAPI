package me.thesilverecho.zeropoint.api.render.shader;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.util.Identifier;

/**
 * Shaders for internal use
 */
public enum APIShaders
{
	RECTANGLE_SHADER("rectangle.frag", false),
	RECTANGLE_TEXTURE_SHADER("rectangle_texture.frag", true),

	ROUND_RECTANGLE_SHADER("round_rectangle.frag", false),
	ROUND_RECTANGLE_TEXTURE_SHADER("round_rectangle_texture.frag", true),

	TEST_TEXTURE_SHADER("test.frag", true),

	CIRCLE_SHADER("circle.frag", false),
	CIRCLE_TEXTURE_SHADER("circle_texture.frag", true),

	TEXT_MASK_TEXTURE("text_mask_texture.frag", true),
	TEXT_MASK_TEXTURE_V2("text_mask_texturev2.frag", true);
	private final Shader shader;
	private static final String BASE_SHADER_PATH = "shaders/";
	private static final String DEFAULT_COL_VERT_PATH = "colour.vert";
	private static final String DEFAULT_TEXT_VERT_PATH = "colour_text.vert";

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
		return shader.getShader();
	}
}
