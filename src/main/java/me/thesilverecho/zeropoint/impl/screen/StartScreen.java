package me.thesilverecho.zeropoint.impl.screen;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.ui.widgets.ColourSelector;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class StartScreen
{
	private static final Identifier START_ID = new Identifier(ZeroPointClient.MOD_ID, "textures/zero-point_background_start.png");

	private static ColourSelector selector;

	public static void init()
	{
		selector = new ColourSelector(120, 200, 100, 100, null);
	}

	public static void render(MatrixStack matrixStack, float width, float height, int mouseX, int mouseY, float delta)
	{
		final int loc = RenderUtilV2.getTextureFromLocation(START_ID);
//		RenderUtilV2.rectangleBokeh(matrixStack, 0, 0, width, height, 0.1f, loc, ColourHolder.FULL);

		RenderUtilV2.rectangleTexture(matrixStack, 0, 0, width, height, loc, ColourHolder.FULL);
//		RenderUtilV2.rectangleBokeh(matrixStack, 0, 0, width, height, (float) mouseX / width, loc, ColourHolder.FULL);

		RenderUtilV2.rectangle(matrixStack, 0, 0, 120, height, new ColourHolder(50, 50, 50, 190));

//		RenderUtilV2.setShader(APIShaders.BLUR_RECTANGLE_SHADER.getShader());
//		RenderUtilV2.fBOTexture(120, 120, false);


		RenderUtilV2.roundRect(matrixStack, width / 2 - 150 / 2f, 120, 280, 220, 4, new ColourHolder(255, 255, 255, 120));
		final CustomFont font = APIFonts.REGULAR.getFont();
//		font.setFontScale(0.7f).render(matrixStack, "${#2B2B2B}Change ${#2B2B2B}log", width / 2 - 150 / 2f + 5 + 0.5f, 125 + 0.5f);
		final float v = FontRenderer.renderRainbowText(font, 0.7f, "${#9FA6CE}Change log", true, matrixStack, width / 2 - 150 / 2f + 5, 125);
		FontRenderer.renderText(font, 0.7f, "Change log", ColourHolder.FULL, true, matrixStack, v + 10, 125);

//		font.setFontScale(0.7f).render(matrixStack, "${#9FA6CE}Change log", width / 2 - 150 / 2f + 5, 125);
//		final Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
	/*	final int fbo = framebuffer.fbo;


		framebuffer.endWrite();
		framebuffer2.bind();
//		RenderUtilV2.rectangleTexturePos(matrixStack, 120, 100, 32, 32, fbo);
		RenderUtilV2.rectangle(matrixStack, 0, 0, 1920, 1080, ColourHolder.FULL);
		framebuffer2.unbind();
		framebuffer.beginWrite(false);
		RenderUtilV2.rectangleTexturePos(matrixStack, 0, 0, 1920, 1080, framebuffer2.texture);*/

	}
}
