package me.thesilverecho.zeropoint.api.render.layer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Consumer;

public class ModRenderPhase extends RenderLayer
{

	public static final ModShader NO_MOD_SHADER = new ModShader();
	protected static final ModRenderPhase.TextureBase NO_TEXTURE = new TextureBase();


	public ModRenderPhase(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
	{
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}


	public static class TextureBase extends RenderPhase.TextureBase
	{
		public TextureBase(Runnable apply, Runnable unapply)
		{
			super(apply, unapply);
		}

		public TextureBase()
		{
			super(() ->
			{
			}, () ->
			{
			});
		}

		public Optional<Identifier> getId()
		{
			return Optional.empty();
		}
	}

	public static class Texture extends TextureBase
	{
		private final Optional<Identifier> id;
		private final boolean blur;
		private final boolean mipmap;

		public Texture(Identifier id, boolean blur, boolean mipmap)
		{
			super(() ->
			{
				RenderSystem.enableTexture();
				TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
				textureManager.getTexture(id).setFilter(blur, mipmap);
				RenderSystem.setShaderTexture(0, id);
//				GL20.glUniform1i(2, 0);
//				RenderSystem.activeTexture(GL43.GL_TEXTURE0);
			}, () ->
			{
			});
			this.id = Optional.of(id);
			this.blur = blur;
			this.mipmap = mipmap;
		}

		public String toString()
		{
			return this.name + "[" + this.id + "(blur=" + this.blur + ", mipmap=" + this.mipmap + ")]";
		}

		public Optional<Identifier> getId()
		{
			return this.id;
		}
	}


	public static class ModShader extends RenderPhase
	{

		public ModShader(me.thesilverecho.zeropoint.api.render.shader.Shader shader)
		{
			super("zero-point_shader", shader::bind, shader::unBind);
		}

		public ModShader(me.thesilverecho.zeropoint.api.render.shader.Shader shader, Consumer<me.thesilverecho.zeropoint.api.render.shader.Shader> extraTasks)
		{
			super("zero-point_shader", () ->
			{
				shader.bind();
				extraTasks.accept(shader);
			}, shader::unBind);
		}

		public ModShader()
		{
			super("zero-point_shader", () ->
			{
			}, () ->
			{
			});
		}
	}
}
