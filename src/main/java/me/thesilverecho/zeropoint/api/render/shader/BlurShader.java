package me.thesilverecho.zeropoint.api.render.shader;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.opengl.GL20;

public class BlurShader extends PostProcessShader
{
	private static float radius;
	private static Vec2f dir;

	public static void setUps(float radius, Vec2f dir)
	{
		BlurShader.radius = radius;
		BlurShader.dir = dir;
	}


	public BlurShader(Identifier fragLocation, Identifier vertLocation)
	{
		super(fragLocation, vertLocation);
	}

	@Override
	public void applyExtraUniforms()
	{
		GL20.glUniform1f(1, radius);
		GL20.glUniform2f(2, dir.x, dir.y);
	}
}
