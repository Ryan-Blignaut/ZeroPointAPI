package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Custom Crosshair", keyBinding = GLFW.GLFW_KEY_C, active = true)
public class CrosshairModule extends BaseModule
{
	@EventListener
	public void renderEvent(Render2dEvent.RenderCrosshair e)
	{
		e.ci().cancel();
		final MatrixStack matrixStack = e.matrixStack();
		final int scaledHeight = e.scaledHeight() / 2;
		final int scaledWidth = e.scaledWidth() / 2;
		int width = 5;
		int height = 5;
//		RenderUtilV2.circle(matrixStack, scaledWidth - width / 2f, scaledHeight - height / 2f, width, height, height / 2f, ColourHolder.FULL.setAlpha(255));
		int s = 1;
		drawXCrosshair(matrixStack, scaledWidth, scaledHeight, 0, ColourHolder.FULL);
	}

	private static void drawXCrosshair(MatrixStack matrix, int screenWidth, int screenHeight, int renderGap, ColourHolder renderColour)
	{

		float i = 5 / 2f;

//		RenderUtilV2.line(matrix, screenWidth - i, screenHeight + 1, screenWidth + i + 11, screenHeight + i + 10, 0, renderColour);
//		RenderUtilV2.line(matrix, screenWidth - i, screenHeight, screenWidth + i, screenHeight + i, 0, renderColour);
//		RenderUtilV2.line(matrix, screenWidth + 1, screenHeight - i, screenWidth + i, screenHeight + i, 0, renderColour);
//		RenderUtilV2.line(matrix, screenWidth, screenHeight - i, screenWidth + i, screenHeight + i, 0, renderColour);
//

//		DisplayUtil.displayFilledRectangle(screenWidth - i, screenHeight - renderGap + 1, screenWidth + i + 1, screenHeight - renderGap - this.height + 1, color);
//		DisplayUtil.displayFilledRectangle(screenWidth - i, screenHeight + renderGap, screenWidth + i + 1, screenHeight + renderGap + this.height, color);
//		DisplayUtil.displayFilledRectangle(screenWidth - renderGap + 1, screenHeight - this.thickness / 2, screenWidth - renderGap - this.width + 1, screenHeight + i + 1, color);
//		DisplayUtil.displayFilledRectangle(screenWidth + renderGap, screenHeight - this.thickness / 2, screenWidth + renderGap + this.width, screenHeight + i + 1, color);

//		glEnable(GL_POINT_SMOOTH);
//		glEnable(GL_LINE_SMOOTH);
//		glDisable(GL_TEXTURE_2D);




		/*if (true)
		{
			glLineWidth((float) (3 + 5));
			GuiHelper.line(GL_LINE_STRIP, screenWidth - width - 1, screenHeight + height + 1, screenWidth, screenHeight, Color.pink.getRGB());
			GuiHelper.line(GL_LINE_STRIP, screenWidth, screenHeight, screenWidth + width + 1, screenHeight + height + 1, Color.pink.getRGB());
			GuiHelper.line(GL_LINE_STRIP, screenWidth + width + 1, screenHeight - height - 1, screenWidth, screenHeight, Color.pink.getRGB());
			GuiHelper.line(GL_LINE_STRIP, screenWidth, screenHeight, screenWidth - width - 1, screenHeight - height - 1, Color.pink.getRGB());
		}*/
//		glLineWidth((float) (4 + 1));
//		//bottom left
//		GuiHelper.line(GL_LINES, screenWidth - width - renderGap, screenHeight + height + renderGap, screenWidth - renderGap,
//				screenHeight + renderGap, renderColour.getRGB());
//		//bottom right
//		GuiHelper.line(GL_LINE_STRIP, screenWidth + renderGap, screenHeight + renderGap, screenWidth + width + renderGap,
//				screenHeight + height + renderGap, renderColour.getRGB());
//		//top right
//		GuiHelper.line(GL_LINE_STRIP, screenWidth + width + renderGap, screenHeight - height - renderGap, screenWidth + renderGap,
//				screenHeight - renderGap, renderColour.getRGB());
//		//top left
//		GuiHelper.line(GL_LINE_STRIP, screenWidth - renderGap, screenHeight - renderGap, screenWidth - width - renderGap,
//				screenHeight - height - renderGap, renderColour.getRGB());
//		glDisable(GL_LINE_SMOOTH);
//		glDisable(GL_POINT_SMOOTH);

	}

}
