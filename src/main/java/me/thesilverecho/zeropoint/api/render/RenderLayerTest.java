package me.thesilverecho.zeropoint.api.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;

public class RenderLayerTest extends RenderLayer
{
	public RenderLayerTest(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
	{
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}


}
