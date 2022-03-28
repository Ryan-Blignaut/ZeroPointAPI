/*
package me.thesilverecho.zeropoint.impl;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Optional;
import java.util.function.BiFunction;

public class ModRenderLayer extends RenderLayer
{

		public static RenderLayer POT_OVERLAY;

	*/
/*
	 	In 1.17, all of the inner classes of `RenderStateShard` (`RenderStateShard.OverlayStateShard`, `RenderStateShard.WriteMaskStateShard`, etc.) have been changed to protected. These classes are needed however to create new `RenderType.CompositeRenderType`'s using `RenderType.CompositeState.CompositeStateBuilder`.
		Since these are protected inner classes, their constructors also can't be directly be obtained using `ObfuscationReflectionHelper`.

		Currently, any mod using custom `RenderType`'s will have to either:
		- add their own access transformer for the inner classes, or
		- extend `RenderStateShard` to access `RenderStateShard`'s inner classes, and extend `RenderType` to access `RenderType.CompositeRenderType`

		This PR changes the visibility of `RenderStateShard`'s inner classes to public in Forge's `accesstransformer.cfg`.
	* *//*



	static
	{
		MultiPhaseParameters transparency = MultiPhaseParameters.builder().texture(new Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(LEQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(new Texturing("glint_transparency", () ->
				setupGlintTexturingCustom(8.0F), () ->
		{
//			RenderSystem.matrixMode(5890);
//			RenderSystem.popMatrix();
//			RenderSystem.matrixMode(5888);
		})).build(true);
		POT_OVERLAY = ModRenderLayer.of("extreme", VertexFormats.POSITION_TEXTURE, 7, 256, transparency);
	}

	private static RenderLayer of(String extreme, VertexFormat positionTexture, int i, int expectedBufferSize, MultiPhaseParameters transparency)
	{

	}


	static ModRenderLayer.ModMultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, ModRenderLayer.MultiPhaseParameters phaseData)
	{
		return of(name, vertexFormat, drawMode, expectedBufferSize, false, false, phaseData);
	}

	private static ModRenderLayer.ModMultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ModRenderLayer.MultiPhaseParameters phases)
	{
		return new ModRenderLayer.ModMultiPhase(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, phases);
	}


	private static void setupGlintTexturingCustom(float v)
	{

	}

	public ModRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
	{
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}


	public static class ModMultiPhase extends RenderLayer
	{

		public ModMultiPhase(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
		{
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		}

		static final BiFunction<Identifier, RenderPhase.Cull, RenderLayer> CULLING_LAYERS = Util.memoize((texture, culling) ->
		{
			return RenderLayer.of("outline", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, ModRenderLayer.ModMultiPhaseParameters.builder().shader(OUTLINE_SHADER).texture(new RenderPhase.Texture(texture, false, false)).cull(culling).depthTest(ALWAYS_DEPTH_TEST).target(OUTLINE_TARGET).build(OutlineMode.IS_OUTLINE));
		});
		private final ModRenderLayer.MultiPhaseParameters phases;
		private final Optional<RenderLayer> affectedOutline;
		private final boolean outline;

		ModMultiPhase(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ModRenderLayer.MultiPhaseParameters phases)
		{
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, () ->
			{
				phases.phases.forEach(RenderPhase::startDrawing);
			}, () ->
			{
				phases.phases.forEach(RenderPhase::endDrawing);
			});
			this.phases = phases;
			this.affectedOutline = phases.outlineMode == RenderLayer.OutlineMode.AFFECTS_OUTLINE ? phases.texture.getId().map((texture) ->
			{
				return (RenderLayer) CULLING_LAYERS.apply(texture, phases.cull);
			}) : Optional.empty();
			this.outline = phases.outlineMode == RenderLayer.OutlineMode.IS_OUTLINE;
		}

		public Optional<RenderLayer> getAffectedOutline()
		{
			return this.affectedOutline;
		}

		public boolean isOutline()
		{
			return this.outline;
		}

		protected final ModRenderLayer.MultiPhaseParameters getPhases()
		{
			return this.phases;
		}

		public String toString()
		{
			return "RenderType[" + this.name + ":" + this.phases + "]";
		}

	}


}
*/
