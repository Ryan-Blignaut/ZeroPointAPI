/*
package me.thesilverecho.zeropoint.impl.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;

public class ModRenderLayer extends RenderLayer
{
	public ModRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
	{
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		RenderLayer.MultiPhaseParameters
	}

	public static RenderLayer POT_OVERLAY;

	static
	{
		final MultiPhaseParameters build = MultiPhaseParameters.builder().shader(DIRECT_GLINT_SHADER).texture(new Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(RenderPhase.LEQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).build(false);
		POT_OVERLAY = RenderLayer.of("glint_direct1", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, false, false, build);
	}

//	public static RenderLayer POT_OVERLAY = RenderLayerAccessor.invokeOf("extreme", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, false, false, RenderLayerAccessor.invoke().build(false));

}
*/
