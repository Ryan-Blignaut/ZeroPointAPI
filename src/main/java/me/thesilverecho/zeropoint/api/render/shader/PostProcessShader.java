package me.thesilverecho.zeropoint.api.render.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class PostProcessShader extends Shader
{
	private static Framebuffer input, output;

	public PostProcessShader(Identifier fragLocation, Identifier vertLocation)
	{
		super(fragLocation, vertLocation);
	}

	public static void setUpFBO(Framebuffer input)
	{
		PostProcessShader.input = input;
		PostProcessShader.output = input;
//		PostProcessShader.output = new SimpleFramebuffer(input.viewportWidth, input.viewportHeight, true, MinecraftClient.IS_SYSTEM_MAC);
//		PostProcessShader.output.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
	}


	@Override
	public Shader bind()
	{
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
		RenderSystem.disableDepthTest();
		input.endWrite();
		float f = (float) output.textureWidth;
		float g = (float) output.textureHeight;
		RenderSystem.viewport(0, 0, (int) f, (int) g);
		glUseProgram(programId);
		setArgument("ProjMat", Matrix4f.projectionMatrix((float) input.textureWidth, (float) (-input.textureHeight), 1000.0F, 3000.0F));

//		setArgument("ProjMat", Matrix4f.projectionMatrix(0.0F, (float) input.textureWidth, (float) input.textureHeight, 0.0F, 0.1F, 1000.0F));
		GlStateManager._bindTexture(input.getColorAttachment());
		GL20.glUniform1i(0, 0);
		RenderSystem.activeTexture(GL43.GL_TEXTURE0);
		setArgument("InSize", new Vec2f((float) input.textureWidth, (float) input.textureHeight));
		setArgument("OutSize", new Vec2f(f, g));
		applyExtraUniforms();
		output.beginWrite(false);
		RenderSystem.depthFunc(519);
		return this;
	}


	public void drawInternal()
	{
		this.bind();
		float f = (float) output.textureWidth;
		float g = (float) output.textureHeight;
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.depthFunc(519);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
		bufferBuilder.vertex(0.0D, 0.0D, 500.0D).next();
		bufferBuilder.vertex(f, 0.0D, 500.0D).next();
		bufferBuilder.vertex(f, g, 500.0D).next();
		bufferBuilder.vertex(0.0D, g, 500.0D).next();
		bufferBuilder.end();
		this.unBind();
	}


	protected void applyExtraUniforms()
	{

	}

	@Override
	public void unBind()
	{
		RenderSystem.depthFunc(0x203);
		super.unBind();
//		output.endWrite();
		input.endRead();
	}

}
