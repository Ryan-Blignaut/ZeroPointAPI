package me.thesilverecho.zeropoint.api.render.shader;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.util.Identifier;

/**
 * Shaders for internal use
 */
public enum APIShaders
{
	//  BASIC SHADERS
	RECTANGLE_SHADER("rectangle.frag"),
	ROUND_RECTANGLE_SHADER("round_rectangle.frag"),
	CIRCLE_SHADER("circle.frag"),
	HEXAGON_SHADER("hexagon.frag"),
	BEZIER_SHADER("bezier.frag"),
	POLYGON_SHADER("polygon.frag"),
	COLOUR_PICKER("colour_picker.frag"),

	//  TEXTURE SHADERS
	RECTANGLE_TEXTURE_SHADER("rectangle_texture.frag", false),
	ROUND_RECTANGLE_TEXTURE_SHADER("round_rectangle_texture.frag", false),
	CIRCLE_TEXTURE_SHADER("circle_texture.frag", false),

	FONT_MASK_TEXTURE("text_mask_texture.frag", false),

	BOKEH_TEXTURE_SHADER("bokeh_disc.frag", false),


	//	POST SHADERS
	BLUR_RECTANGLE_SHADER("blur.frag", true);

	private final Shader shader;
	private static final String BASE_SHADER_PATH = "shaders/";
	private static final String DEFAULT_COL_VERT_PATH = "colour.vert";
	private static final String DEFAULT_TEXTURE_VERT_PATH = "colour_texture.vert";
	private static final String DEFAULT_POST_TEXTURE_VERT_PATH = "post_process_texture.vert";


	APIShaders(String frag)
	{
		this(frag, DEFAULT_COL_VERT_PATH);
	}


	APIShaders(String frag, boolean post)
	{
		this(frag, post ? DEFAULT_POST_TEXTURE_VERT_PATH : DEFAULT_TEXTURE_VERT_PATH);
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
