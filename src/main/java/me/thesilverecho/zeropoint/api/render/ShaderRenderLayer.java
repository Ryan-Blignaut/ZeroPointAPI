package me.thesilverecho.zeropoint.api.render;

import net.minecraft.client.render.RenderLayer;

public class ShaderRenderLayer extends RenderLayer
{

	private final net.minecraft.client.render.RenderLayer delegate;
	private final me.thesilverecho.zeropoint.api.render.shader.Shader shader;

	public ShaderRenderLayer(RenderLayer delegate, me.thesilverecho.zeropoint.api.render.shader.Shader shader)
	{
		super("shader_render_layer", delegate.getVertexFormat(), delegate.getDrawMode(), delegate.getExpectedBufferSize(), true, delegate.isOutline(), () ->
		{
			delegate.startDrawing();

//			RenderSystem.matrixMode(GL11.GL_PROJECTION);
//			RenderSystem.pushMatrix();
//			RenderSystem.scalef(0.9999F, 0.9999F, 0.9999F);
//			RenderSystem.matrixMode(GL11.GL_MODELVIEW);
			shader.bind();
		}, () ->
		{
			shader.unBind();
//			RenderSystem.matrixMode(GL11.GL_PROJECTION);
//			RenderSystem.popMatrix();
//			RenderSystem.matrixMode(GL11.GL_MODELVIEW);
			delegate.endDrawing();
		});
		this.delegate = delegate;
		this.shader = shader;
	}
}
