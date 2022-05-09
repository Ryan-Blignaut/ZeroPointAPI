package me.thesilverecho.zeropoint.impl.render.layering;

import me.thesilverecho.zeropoint.api.render.layer.ApiRenderLayer;
import me.thesilverecho.zeropoint.api.render.layer.ApiRenderPhase;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import me.thesilverecho.zeropoint.impl.module.render3.BlockEntityESP;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;

import java.util.ArrayList;

public class ZeroLayers extends ApiRenderLayer
{
	public ZeroLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
	{
		//dummy constructor.
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	//TODO: outline function, have a look at CULLING_LAYERS( when it is enabled/ used/ called) and how to modify it
	public static RenderLayer TEST_LAYER = apiCreate("test:2", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256,
			ApiMultiPhaseParameters.builder()
			                       .texture(new ApiRenderPhase.Texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, false, true))
			                       .shader(RenderPhase.COLOR_SHADER)
			                       .cull(DISABLE_CULLING)
			                       .depthTest(RenderPhase.EQUAL_DEPTH_TEST)
			                       .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			                       .build(true));


	public static RenderLayer BLOCK_ENTITY_LAYER = apiCreate(ZeroPointClient.MOD_ID + ":block_entity_outline", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256,
			ApiMultiPhaseParameters.builder()
			                       .target(new Target("a", () ->
			                       {
				                       BlockEntityESP.getFramebuffer().bind();
			                       }, () ->
			                       {
				                       BlockEntityESP.getFramebuffer().unbind();
			                       }))
			                       .depthTest(RenderPhase.ALWAYS_DEPTH_TEST)
			                       .shader(RenderPhase.COLOR_SHADER)
			                       .cull(RenderPhase.ENABLE_CULLING)
			                       .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			                       .build(false));


	public static ArrayList<RenderLayer> registerLayers()
	{
		return ALL_LAYERS;
	}


}
