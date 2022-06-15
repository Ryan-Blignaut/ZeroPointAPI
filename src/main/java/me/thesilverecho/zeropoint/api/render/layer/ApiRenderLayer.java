package me.thesilverecho.zeropoint.api.render.layer;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

public class ApiRenderLayer extends RenderLayer
{
	public static final ArrayList<RenderLayer> ALL_LAYERS = new ArrayList<>();
/*	public static final RenderLayer POT_OVERLAY;
	public static final RenderLayer BLUR = of("test:glint_direct", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256,
			ApiMultiPhaseParameters.builder()
			                       .shader(RenderPhase.GLINT_SHADER)
			                       .writeMaskState(COLOR_MASK)
			                       .texture(new ApiRenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false))
			                       .cull(DISABLE_CULLING)
			                       .depthTest(*//*RenderPhase.EQUAL_DEPTH_TEST*//*RenderPhase.ALWAYS_DEPTH_TEST)
			                       .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			                       .texturing(GLINT_TEXTURING)
//			                       .target(new Target("blur_out", () -> BlurBackground.blurMask.bind(), () -> BlurBackground.blurMask.unbind()))
                                   .build(false));*/


//	public static final RenderLayer CHARMING = of("test:depth_miss_write", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256,
//			ApiMultiPhaseParameters.builder()
//			                       .shader(COLOR_SHADER)
//			                       .writeMaskState(ALL_MASK)
//			                       .cull(DISABLE_CULLING)
////			                       .depthTest(EQUAL_DEPTH_TEST/*ALWAYS_DEPTH_TEST*/)
//                                   /*  .layering(new Layering("overlay", () ->
//								   {
//									   MatrixStack matrixStack = RenderSystem.getModelViewStack();
//									   matrixStack.push();
////				                       matrixStack.scale(1.1f, 1.1f, 1.1f);
//									   RenderSystem.applyModelViewMatrix();
//								   }, () ->
//								   {
//									   MatrixStack matrixStack = RenderSystem.getModelViewStack();
//									   matrixStack.pop();
//									   RenderSystem.applyModelViewMatrix();
//								   }))
//								   .target(new Target("charming", () -> BlockEntityESP.getFramebuffer().bind(), () -> BlockEntityESP.getFramebuffer().unbind()))*/
//                                   .build(false));

/*	public static final RenderLayer CHARMING = of("test:2", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256,
			ModMultiPhaseParameters.builder()
			                       .shader(RenderPhase.COLOR_SHADER)
			                       .writeMaskState(COLOR_MASK)
			                       .cull(DISABLE_CULLING)
			                       .depthTest(RenderPhase.EQUAL_DEPTH_TEST)
			                       .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			                       .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
			                       .build(false));*/


//	private static final RenderLayer ARMOR_ENTITY_GLINT = of("armor_entity_glint", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(ARMOR_ENTITY_GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(ENTITY_GLINT_TEXTURING).layering(VIEW_OFFSET_Z_LAYERING).build(false));


	/*public static final RenderLayer CHARMING = of("test:2", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256,
				ModMultiPhaseParameters.builder()
									   .shader(RenderPhase.COLOR_SHADER)
									   .cull(DISABLE_CULLING)
									   .layering(POLYGON_OFFSET_LAYERING)
									   .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
									   .depthTest(RenderPhase.ALWAYS_DEPTH_TEST)
									   			                       .target(new Target("blur_out", () -> BlurBackground.getBlurMask()BlockEntityESP.getFramebuffer().bind(), () -> BlurBackground.getBlurMask()BlockEntityESP.getFramebuffer().unbind()))

		.build(false));*/
/*	public static final RenderLayer DG = of(ZeroPointClient.MOD_ID + ":blur",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			ApiMultiPhaseParameters.builder()
			                       .shader(SOLID_SHADER)
			                       .writeMaskState(COLOR_MASK)
			                       .cull(DISABLE_CULLING)
			                       .depthTest(EQUAL_DEPTH_TEST)
			                       .transparency(new Transparency("default", () ->
			                       {
				                       RenderSystem.enableBlend();
				                       RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
			                       }, () ->
			                       {
				                       RenderSystem.disableBlend();
				                       RenderSystem.defaultBlendFunc();
			                       }))
			                       *//* .target(new Target("blur_out", () ->
									{
										BlurBackground.blurMask.bind();
									}, () ->
									{
										BlurBackground.blurMask.unbind();
									}))*//*
			                       .build(false));*/

	private final boolean isTranslucent;

	@Override
	public void draw(BufferBuilder buffer, int cameraX, int cameraY, int cameraZ)
	{
		if (!buffer.isBuilding())
		{
			return;
		}
		if (this.isTranslucent)
		{
			buffer.sortFrom(cameraX, cameraY, cameraZ);
		}
		buffer.end();
		this.startDrawing();
		BufferRenderer.postDraw(buffer);
//		BufferRenderer.draw(buffer);
		this.endDrawing();
	}

	public static ApiMultiPhase apiCreate(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, ApiMultiPhaseParameters phases)
	{
		return apiCreate(name, vertexFormat, drawMode, expectedBufferSize, false, false, phases, true);
	}

	public static ApiMultiPhase apiCreate(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ApiMultiPhaseParameters phases, boolean enabled)
	{
		//		ALL_LAYERS.add(apiMultiPhase);
		return new ApiMultiPhase(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, phases, enabled);
	}

	public static ApiMultiPhase apiCreate(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ApiMultiPhaseParameters phases)
	{
		//		ALL_LAYERS.add(apiMultiPhase);
		return new ApiMultiPhase(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, phases, true);
	}

	public ApiRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction, boolean enable)
	{
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		this.isTranslucent = translucent;
		if (enable) ALL_LAYERS.add(this);
	}

	public static class ApiMultiPhaseParameters
	{
		public final ApiRenderPhase.TextureBase texture;
		public final ApiRenderPhase.Shader shader;
		public final ApiRenderPhase.ModShader modShader;
		public final ApiRenderPhase.Transparency transparency;
		public final ApiRenderPhase.DepthTest depthTest;
		public final ApiRenderPhase.Cull cull;
		public final ApiRenderPhase.Lightmap lightmap;
		public final ApiRenderPhase.Overlay overlay;
		public final ApiRenderPhase.Layering layering;
		public final ApiRenderPhase.Target target;
		public final ApiRenderPhase.Texturing texturing;
		public final ApiRenderPhase.WriteMaskState writeMaskState;
		public final ApiRenderPhase.LineWidth lineWidth;
		public final ApiOutlineMode outlineMode;
		public final ImmutableList<RenderPhase> phases;

		ApiMultiPhaseParameters(ApiRenderPhase.TextureBase texture, Shader shader, ApiRenderPhase.ModShader modShader, Transparency transparency, DepthTest depthTest, Cull cull, Lightmap lightmap, Overlay overlay, Layering layering, Target target, Texturing texturing, WriteMaskState writeMaskState, LineWidth lineWidth, ApiOutlineMode outlineMode)
		{
			this.texture = texture;
			this.shader = shader;
			this.modShader = modShader;
			this.transparency = transparency;
			this.depthTest = depthTest;
			this.cull = cull;
			this.lightmap = lightmap;
			this.overlay = overlay;
			this.layering = layering;
			this.target = target;
			this.texturing = texturing;
			this.writeMaskState = writeMaskState;
			this.lineWidth = lineWidth;
			this.outlineMode = outlineMode;


			this.phases = ImmutableList.of(this.texture, this.modShader, /*this.shader,*/ this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth);
		}

		public String toString()
		{
			return "CompositeState[" + this.phases + ", outlineProperty=" + this.outlineMode + "]";
		}

		public static Builder builder()
		{
			return new Builder();
		}

		@Environment(EnvType.CLIENT)
		public static class Builder
		{
			private ApiRenderPhase.TextureBase texture;
			private ApiRenderPhase.Shader shader;
			private ApiRenderPhase.ModShader modShader;
			private ApiRenderPhase.Transparency transparency;
			private ApiRenderPhase.DepthTest depthTest;
			private ApiRenderPhase.Cull cull;
			private ApiRenderPhase.Lightmap lightmap;
			private ApiRenderPhase.Overlay overlay;
			private ApiRenderPhase.Layering layering;
			private ApiRenderPhase.Target target;
			private ApiRenderPhase.Texturing texturing;
			private ApiRenderPhase.WriteMaskState writeMaskState;
			private ApiRenderPhase.LineWidth lineWidth;

			Builder()
			{
				this.texture = ApiRenderPhase.NO_TEXTURE;
				this.shader = ApiRenderPhase.NO_SHADER;
				this.modShader = ApiRenderPhase.NO_MOD_SHADER;
				this.transparency = ApiRenderPhase.NO_TRANSPARENCY;
				this.depthTest = ApiRenderPhase.LEQUAL_DEPTH_TEST;
				this.cull = ApiRenderPhase.ENABLE_CULLING;
				this.lightmap = ApiRenderPhase.DISABLE_LIGHTMAP;
				this.overlay = ApiRenderPhase.DISABLE_OVERLAY_COLOR;
				this.layering = ApiRenderPhase.NO_LAYERING;
				this.target = ApiRenderPhase.MAIN_TARGET;
				this.texturing = ApiRenderPhase.DEFAULT_TEXTURING;
				this.writeMaskState = ApiRenderPhase.ALL_MASK;
				this.lineWidth = ApiRenderPhase.FULL_LINE_WIDTH;
			}

			public Builder texture(ApiRenderPhase.TextureBase texture)
			{
				this.texture = texture;
				return this;
			}

			public Builder shader(ApiRenderPhase.Shader shader)
			{
				this.shader = shader;
				return this;
			}

			public Builder modShader(ApiRenderPhase.ModShader modShader)
			{
				this.modShader = modShader;
				return this;
			}


			public Builder transparency(ApiRenderPhase.Transparency transparency)
			{
				this.transparency = transparency;
				return this;
			}

			public Builder depthTest(ApiRenderPhase.DepthTest depthTest)
			{
				this.depthTest = depthTest;
				return this;
			}

			public Builder cull(ApiRenderPhase.Cull cull)
			{
				this.cull = cull;
				return this;
			}

			public Builder lightmap(ApiRenderPhase.Lightmap lightmap)
			{
				this.lightmap = lightmap;
				return this;
			}

			public Builder overlay(ApiRenderPhase.Overlay overlay)
			{
				this.overlay = overlay;
				return this;
			}

			public Builder layering(ApiRenderPhase.Layering layering)
			{
				this.layering = layering;
				return this;
			}

			public Builder target(ApiRenderPhase.Target target)
			{
				this.target = target;
				return this;
			}

			public Builder texturing(ApiRenderPhase.Texturing texturing)
			{
				this.texturing = texturing;
				return this;
			}

			public Builder writeMaskState(ApiRenderPhase.WriteMaskState writeMaskState)
			{
				this.writeMaskState = writeMaskState;
				return this;
			}

			public Builder lineWidth(ApiRenderPhase.LineWidth lineWidth)
			{
				this.lineWidth = lineWidth;
				return this;
			}

			public ApiMultiPhaseParameters build(boolean affectsOutline)
			{
				return this.build(affectsOutline ? ApiOutlineMode.AFFECTS_OUTLINE : ApiOutlineMode.NONE);
			}

			public ApiMultiPhaseParameters build(ApiOutlineMode outlineMode)
			{

//              return new MultiPhaseParameters(this.texture, this.shader, this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth, outlineMode);
				return new ApiMultiPhaseParameters(this.texture, this.shader, this.modShader, this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth, outlineMode);
			}

		}

		public enum ApiOutlineMode
		{
			NONE("none"),
			IS_OUTLINE("is_outline"),
			AFFECTS_OUTLINE("affects_outline");
			private final String name;

			ApiOutlineMode(String name)
			{
				this.name = name;
			}

			public String toString()
			{
				return this.name;
			}
		}
	}

	public static class ApiMultiPhase extends ApiRenderLayer
	{
		//Renders the
		static final BiFunction<Identifier, RenderPhase.Cull, RenderLayer> CULLING_LAYERS = Util.memoize((texture, culling) -> apiCreate("outline", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, ApiMultiPhaseParameters.builder().shader(OUTLINE_SHADER).texture(new ApiRenderPhase.Texture((Identifier) texture, false, false)).cull(culling).depthTest(ALWAYS_DEPTH_TEST).target(new Target("test", () ->
		{
//			BlockEntityESP.getFramebuffer().bind();
		}, () ->
		{
//			BlockEntityESP.getFramebuffer().unbind();

		})).build(ApiMultiPhaseParameters.ApiOutlineMode.IS_OUTLINE)));

		//		static final BiFunction<Identifier, Cull, ApiRenderLayer> CULLING_LAYERS = Util.memoize((texture, culling) -> ApiRenderLayer.apiCreate("outline_test", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, ApiMultiPhaseParameters.builder().shader(OUTLINE_SHADER).texture(new ApiRenderPhase.Texture(texture, false, false)).cull(culling).depthTest(ALWAYS_DEPTH_TEST).target(OUTLINE_TARGET).build(ApiMultiPhaseParameters.OutlineMode.IS_OUTLINE)));
		private final ApiMultiPhaseParameters phases;
		private final Optional<RenderLayer> affectedOutline;
		private final boolean outline;

		ApiMultiPhase(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ApiMultiPhaseParameters phases, boolean ena)
		{
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, () -> phases.phases.forEach(RenderPhase::startDrawing), () -> phases.phases.forEach(RenderPhase::endDrawing), ena);
			this.phases = phases;
			this.affectedOutline = phases.outlineMode == ApiMultiPhaseParameters.ApiOutlineMode.AFFECTS_OUTLINE ? phases.texture.getId().map((texture) -> CULLING_LAYERS.apply(texture, phases.cull)) : Optional.empty();
			this.outline = phases.outlineMode == ApiMultiPhaseParameters.ApiOutlineMode.IS_OUTLINE;
		}

		public Optional<RenderLayer> getAffectedOutline()
		{
//			System.out.println("Affected outline for API layer " + this.name + ": " + this.affectedOutline);
			return affectedOutline;
		}

		public boolean isOutline()
		{
			return this.outline;
		}

		protected final ApiMultiPhaseParameters getPhases()
		{
			return this.phases;
		}

		public String toString()
		{
			return "ApiRenderType[" + this.name + ":" + this.phases + "]";
		}


	}

}
