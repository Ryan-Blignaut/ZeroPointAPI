package me.thesilverecho.zeropoint.api.render.shader;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.util.Identifier;

/**
 * Shaders for internal use
 */
public enum APIShaders
{
	//  BASIC SHADERS
	BEZIER_SHADER("bezier.frag"),
	CIRCLE_SHADER("circle.frag"),
	COLOUR_PICKER("colour_picker.frag"),
	RECTANGLE_SHADER("rectangle.frag"),
	ROUND_RECTANGLE_SHADER("round_rectangle.frag"),
	ROUND_RECTANGLE_LINE_SHADER("round_rectangle_line.frag"),
	LOADING("loading_screen.frag"),

	//  TEXTURE SHADERS
	RECTANGLE_TEXTURE_SHADER("rectangle_texture.frag", false),
	ROUND_RECTANGLE_TEXTURE_SHADER("round_rectangle_texture.frag", false),
	CIRCLE_TEXTURE_SHADER("circle_texture.frag", false),

	FONT_MASK_TEXTURE("text_mask_texture.frag", false),

	BOKEH_TEXTURE_SHADER("bokeh_disc.frag", false),
	WATER_RIPPLE("water_ripple.frag", "water_ripple.vert"),
	OUTLINE("outline.frag", "colour_texture.vert"),
	OUT("ol1.frag", "colour_texture.vert"),
	ITEM("item.frag", "colour_texture.vert"),
	BLURV3("blur.frag", "post_process_texture.vert"),
	COMPOSITE("composite.frag", "post_process_texture.vert"),


	OUTLINE0("ol.frag", "post_process_texture.vert"),
	GLOW("glow.frag", "post_process_texture.vert"),
	BLOOM("bloom.frag", true),
	ADD_EFFECTS("add.frag", true),


	//	POST SHADERS
	BLUR_RECTANGLE_SHADER("blur.frag", true),
	BLUR_MIP_SHADER("blur_mip.frag", true),

	MASK_RECTANGLE_SHADER("mask.frag", true),
	GAUSSIAN_BLUR_SHADER("gaussian_blur.frag", true);


	private final Shader shader;
	private static final String BASE_SHADER_PATH = "shaders/";
	private static final String DEFAULT_COL_VERT_PATH = "colour.vert";
	private static final String DEFAULT_TEXTURE_VERT_PATH = "colour_texture.vert";
	private static final String DEFAULT_POST_TEXTURE_VERT_PATH = "post_process_texture.vert";


	APIShaders(String frag)
	{
		this("core/" + frag, DEFAULT_COL_VERT_PATH);
	}


	APIShaders(String frag, boolean post)
	{
		this((post ? "post/" : "core/") + frag, post ? DEFAULT_POST_TEXTURE_VERT_PATH : DEFAULT_TEXTURE_VERT_PATH);
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
