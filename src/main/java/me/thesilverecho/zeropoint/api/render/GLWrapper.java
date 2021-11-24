package me.thesilverecho.zeropoint.api.render;

import static org.lwjgl.opengl.GL43.*;

public class GLWrapper
{

	public static void enableGL2D()
	{
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthMask(true);
//		glEnable(GL_LINE_SMOOTH);
//		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
//		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
	}

	public static void disableGL2D()
	{
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
//		glDisable(GL_LINE_SMOOTH);
//		glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
//		glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
	}

}
