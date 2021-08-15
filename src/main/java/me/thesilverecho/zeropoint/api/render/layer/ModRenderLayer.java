package me.thesilverecho.zeropoint.api.render.layer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

public class ModRenderLayer extends RenderLayer
{
	public static final ArrayList<ModRenderLayer> ALL_LAYERS = new ArrayList<>();
	public static final RenderLayer POT_OVERLAY = basic("glint_direct", ModMultiPhaseParameters.builder().shader(DIRECT_GLINT_SHADER).texture(new ModRenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(ENABLE_CULLING).depthTest(LEQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).build(false));
	public static final RenderLayer RENDER_LAYER = of("sword_test", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, ModMultiPhaseParameters.builder().shader(POSITION_TEXTURE_SHADER).texture(new ModRenderPhase.Texture(new Identifier(ZeroPointClient.MOD_ID, "textures/bg.png"), true, false)).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).build(false));
	public static final RenderLayer POT_OVERLAY2;

	static
	{
		final ModRenderPhase.Texture texture = new ModRenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false);
		POT_OVERLAY2 = basic("test", ModMultiPhaseParameters.builder().shader(RenderPhase.POSITION_COLOR_TEXTURE_SHADER).modShader(new ModRenderPhase.ModShader(APIShaders.TEST_TEXTURE_SHADER.getShader(), shader ->
				texture.getId().ifPresent(identifier ->
				{
					RenderUtil.setShaderTexture(identifier);
					TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
					AbstractTexture abstractTexture = textureManager.getTexture(identifier);
					GlStateManager._bindTexture(abstractTexture.getGlId());
					GL20.glUniform1i(2, 0);
					RenderSystem.activeTexture(GL43.GL_TEXTURE0);
				}))).texture(texture).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).build(false));
	}

	public static ModRenderLayer.ModMultiPhase basic(String name, ModMultiPhaseParameters parameters)
	{
		final ModMultiPhase of = of(name, VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, parameters);
		ALL_LAYERS.add(of);
		return of;
	}

	public ModRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
	{
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static ModRenderLayer.ModMultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, ModRenderLayer.ModMultiPhaseParameters phaseData)
	{
		return of(name, vertexFormat, drawMode, expectedBufferSize, false, false, phaseData);
	}

	private static ModRenderLayer.ModMultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ModRenderLayer.ModMultiPhaseParameters phases)
	{
		return new ModRenderLayer.ModMultiPhase(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, phases);
	}

	private static RenderLayer.MultiPhaseParameters of(RenderPhase.Shader shader)
	{
		return RenderLayer.MultiPhaseParameters.builder().lightmap(ENABLE_LIGHTMAP).shader(shader).texture(MIPMAP_BLOCK_ATLAS_TEXTURE).transparency(TRANSLUCENT_TRANSPARENCY).target(TRANSLUCENT_TARGET).build(true);
	}

	public static class ModMultiPhaseParameters
	{
		//		TODO: add support for api shaders
		public final ModRenderPhase.TextureBase texture;
		public final ModRenderPhase.Shader shader;
		public final ModRenderPhase.ModShader modShader;
		public final ModRenderPhase.Transparency transparency;
		public final ModRenderPhase.DepthTest depthTest;
		public final ModRenderPhase.Cull cull;
		public final ModRenderPhase.Lightmap lightmap;
		public final ModRenderPhase.Overlay overlay;
		public final ModRenderPhase.Layering layering;
		public final ModRenderPhase.Target target;
		public final ModRenderPhase.Texturing texturing;
		public final ModRenderPhase.WriteMaskState writeMaskState;
		public final ModRenderPhase.LineWidth lineWidth;
		public final OutlineMode outlineMode;
		public final ImmutableList<RenderPhase> phases;

		ModMultiPhaseParameters(ModRenderPhase.TextureBase texture, Shader shader, ModRenderPhase.ModShader modShader, Transparency transparency, DepthTest depthTest, Cull cull, Lightmap lightmap, Overlay overlay, Layering layering, Target target, Texturing texturing, WriteMaskState writeMaskState, LineWidth lineWidth, OutlineMode outlineMode)
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
			this.phases = ImmutableList.of(this.texture, this.shader, this.modShader, this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth);
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
			private ModRenderPhase.TextureBase texture;
			private ModRenderPhase.Shader shader;
			private ModRenderPhase.ModShader modShader;
			private ModRenderPhase.Transparency transparency;
			private ModRenderPhase.DepthTest depthTest;
			private ModRenderPhase.Cull cull;
			private ModRenderPhase.Lightmap lightmap;
			private ModRenderPhase.Overlay overlay;
			private ModRenderPhase.Layering layering;
			private ModRenderPhase.Target target;
			private ModRenderPhase.Texturing texturing;
			private ModRenderPhase.WriteMaskState writeMaskState;
			private ModRenderPhase.LineWidth lineWidth;

			Builder()
			{
				this.texture = ModRenderPhase.NO_TEXTURE;
				this.shader = ModRenderPhase.NO_SHADER;
				this.modShader = ModRenderPhase.NO_MOD_SHADER;
				this.transparency = ModRenderPhase.NO_TRANSPARENCY;
				this.depthTest = ModRenderPhase.LEQUAL_DEPTH_TEST;
				this.cull = ModRenderPhase.ENABLE_CULLING;
				this.lightmap = ModRenderPhase.DISABLE_LIGHTMAP;
				this.overlay = ModRenderPhase.DISABLE_OVERLAY_COLOR;
				this.layering = ModRenderPhase.NO_LAYERING;
				this.target = ModRenderPhase.MAIN_TARGET;
				this.texturing = ModRenderPhase.DEFAULT_TEXTURING;
				this.writeMaskState = ModRenderPhase.ALL_MASK;
				this.lineWidth = ModRenderPhase.FULL_LINE_WIDTH;
			}

			public Builder texture(ModRenderPhase.TextureBase texture)
			{
				this.texture = texture;
				return this;
			}

			public Builder shader(ModRenderPhase.Shader shader)
			{
				this.shader = shader;
				return this;
			}

			public Builder modShader(ModRenderPhase.ModShader modShader)
			{
				this.modShader = modShader;
				return this;
			}


			public Builder transparency(ModRenderPhase.Transparency transparency)
			{
				this.transparency = transparency;
				return this;
			}

			public Builder depthTest(ModRenderPhase.DepthTest depthTest)
			{
				this.depthTest = depthTest;
				return this;
			}

			public Builder cull(ModRenderPhase.Cull cull)
			{
				this.cull = cull;
				return this;
			}

			public Builder lightmap(ModRenderPhase.Lightmap lightmap)
			{
				this.lightmap = lightmap;
				return this;
			}

			public Builder overlay(ModRenderPhase.Overlay overlay)
			{
				this.overlay = overlay;
				return this;
			}

			public Builder layering(ModRenderPhase.Layering layering)
			{
				this.layering = layering;
				return this;
			}

			public Builder target(ModRenderPhase.Target target)
			{
				this.target = target;
				return this;
			}

			public Builder texturing(ModRenderPhase.Texturing texturing)
			{
				this.texturing = texturing;
				return this;
			}

			public Builder writeMaskState(ModRenderPhase.WriteMaskState writeMaskState)
			{
				this.writeMaskState = writeMaskState;
				return this;
			}

			public Builder lineWidth(ModRenderPhase.LineWidth lineWidth)
			{
				this.lineWidth = lineWidth;
				return this;
			}

			public ModMultiPhaseParameters build(boolean affectsOutline)
			{
				return this.build(affectsOutline ? OutlineMode.AFFECTS_OUTLINE : OutlineMode.NONE);
			}

			public ModMultiPhaseParameters build(OutlineMode outlineMode)
			{
				return new ModMultiPhaseParameters(this.texture, this.shader, this.modShader, this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth, outlineMode);
			}

		}

		private enum OutlineMode
		{
			NONE("none"),
			IS_OUTLINE("is_outline"),
			AFFECTS_OUTLINE("affects_outline");
			private final String name;

			OutlineMode(String name)
			{
				this.name = name;
			}

			public String toString()
			{
				return this.name;
			}
		}
	}

	public static class ModMultiPhase extends ModRenderLayer
	{
		static final BiFunction<Identifier, Cull, ModRenderLayer> CULLING_LAYERS = Util.memoize((texture, culling) -> ModRenderLayer.of("outline", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, ModMultiPhaseParameters.builder().shader(OUTLINE_SHADER).texture(new ModRenderPhase.Texture(texture, false, false)).cull(culling).depthTest(ALWAYS_DEPTH_TEST).target(OUTLINE_TARGET).build(ModMultiPhaseParameters.OutlineMode.IS_OUTLINE)));
		private final ModMultiPhaseParameters phases;
		private final Optional<RenderLayer> affectedOutline;
		private final boolean outline;

		ModMultiPhase(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ModMultiPhaseParameters phases)
		{
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, () -> phases.phases.forEach(RenderPhase::startDrawing), () -> phases.phases.forEach(RenderPhase::endDrawing));
			this.phases = phases;
			this.affectedOutline = phases.outlineMode == ModMultiPhaseParameters.OutlineMode.AFFECTS_OUTLINE ? phases.texture.getId().map((texture) -> CULLING_LAYERS.apply(texture, phases.cull)) : Optional.empty();
			this.outline = phases.outlineMode == ModMultiPhaseParameters.OutlineMode.IS_OUTLINE;
		}

		public Optional<RenderLayer> getAffectedOutline()
		{
			return affectedOutline;
		}

		public boolean isOutline()
		{
			return this.outline;
		}

		protected final ModMultiPhaseParameters getPhases()
		{
			return this.phases;
		}

		public String toString()
		{
			return "ModRenderType[" + this.name + ":" + this.phases + "]";
		}


	}

}
