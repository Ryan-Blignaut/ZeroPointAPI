package me.thesilverecho.zeropoint.impl.screen;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
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
		APIFonts.REGULAR.getFont().setFontScale(0.7f).render(matrixStack, "${#2B2B2B}Change ${#2B2B2B}log", width / 2 - 150 / 2f + 5 + 0.5f, 125 + 0.5f);
		APIFonts.REGULAR.getFont().setFontScale(0.7f).render(matrixStack, "${#9FA6CE}Change log", width / 2 - 150 / 2f + 5, 125);



		selector.render(matrixStack, mouseX, mouseY, delta);

//		RenderUtilV2.rectangle(matrixStack, 0, 0, width, height, 0, new ColourHolder(50, 50, 50, 255));
	}
}
