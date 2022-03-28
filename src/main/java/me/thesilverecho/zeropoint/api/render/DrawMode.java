package me.thesilverecho.zeropoint.api.render;

import org.lwjgl.opengl.GL32;

public enum DrawMode
{
	Lines(2),
	Triangles(3);

	public final int indicesCount;

	DrawMode(int indicesCount)
	{
		this.indicesCount = indicesCount;
	}

	public int getGL()
	{
		return switch (this)
				{
					case Lines -> GL32.GL_LINES;
					case Triangles -> GL32.GL_TRIANGLES;
				};
	}
}
