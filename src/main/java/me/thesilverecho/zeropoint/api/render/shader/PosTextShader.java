package me.thesilverecho.zeropoint.api.render.shader;

import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL20;

public class PosTextShader extends Shader
{
	public PosTextShader(Identifier fragLocation, Identifier vertLocation)
	{
		super(fragLocation, vertLocation);
	}

	@Override
	public Shader bind()
	{
		final Shader bind = super.bind();
			GL20.glUniform2f(0, 0, 0);
		return bind;
	}
}
