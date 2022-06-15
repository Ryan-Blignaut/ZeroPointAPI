package me.thesilverecho.zeropoint.impl.render.layering;

import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.render.layer.ApiRenderLayer;
import me.thesilverecho.zeropoint.api.render.layer.ApiRenderPhase;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.texture.Texture2D;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import me.thesilverecho.zeropoint.impl.module.render3.BlockEntityESP;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.function.Function;

public class ZeroLayers extends ApiRenderLayer
{

	//TODO: outline function, have a look at CULLING_LAYERS( when it is enabled/ used/ called) and how to modify it
	public static RenderLayer TEST_LAYER = apiCreate("test:2", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256,
			ApiMultiPhaseParameters.builder()
			                       .texture(new ApiRenderPhase.Texture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, false, true))
			                       .shader(RenderPhase.COLOR_SHADER)
			                       .cull(DISABLE_CULLING)
			                       .depthTest(RenderPhase.EQUAL_DEPTH_TEST)
			                       .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			                       .build(true));


	public static RenderLayer BLOCK_ENTITY_LAYER = apiCreate(ZeroPointClient.MOD_ID + ":block_entity_outline", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256,
			ApiMultiPhaseParameters.builder()
			                       .texture(new ApiRenderPhase.Texture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, false, true))
			                       .target(new Target("a", () ->
			                       {
				                       BlockEntityESP.getFramebuffer().bind();
			                       }, () ->
			                       {
				                       BlockEntityESP.getFramebuffer().unbind();
			                       }))
			                       .depthTest(RenderPhase.ALWAYS_DEPTH_TEST)
//			                       .shader(RenderPhase.COLOR_SHADER)
                                   .modShader(new ApiRenderPhase.ModShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader()))
                                   .cull(RenderPhase.ENABLE_CULLING)
                                   .transparency(/*new Transparency("a", () ->
                                   {
	                                   RenderSystem.enableBlend();
	                                   RenderSystem.defaultBlendFunc();
                                   }, () ->
                                   {
	                                   RenderSystem.disableBlend();
	                                   RenderSystem.defaultBlendFunc();
                                   })*/RenderPhase.TRANSLUCENT_TRANSPARENCY)
                                   .build(false));

	public static final Function<Texture2D, RenderLayer> TEXT = Util.memoize(texture ->
	{
		final APIShaders fontMaskTexture = APIShaders.SDF_FONT_MASK_TEXTURE;
		return apiCreate(ZeroPointClient.MOD_ID + "text", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, false, false,
				ApiMultiPhaseParameters.builder()
				                       .modShader(new ApiRenderPhase.ModShader(fontMaskTexture.getShader()))
				                       .texture(new ApiRenderPhase.TextureBase(() ->
				                       {
					                       GLWrapper.activateTexture(0, texture.getID());
//					                       GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
					                       fontMaskTexture.getShader().setShaderUniform("Sampler0", 0);
				                       }, () ->
				                       {
//					                       glPixelStorei(GL_UNPACK_ALIGNMENT, 4);
				                       }))
				                       .transparency(/*new Transparency("a", () ->
				                       {
					                       RenderSystem.enableBlend();
					                       RenderSystem.defaultBlendFunc();
				                       }, () ->
				                       {
					                       RenderSystem.disableBlend();
					                       RenderSystem.defaultBlendFunc();
				                       })*/RenderPhase.TRANSLUCENT_TRANSPARENCY)
//				                       .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
//				                       .cull(RenderPhase.ENABLE_CULLING)
                                       .build(false), false);
	});

	public ZeroLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction, boolean enable)
	{
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction, enable);
	}

	public static ArrayList<RenderLayer> registerLayers()
	{
		return ALL_LAYERS;
	}


}
