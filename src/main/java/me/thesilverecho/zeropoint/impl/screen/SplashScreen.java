package me.thesilverecho.zeropoint.impl.screen;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SplashScreen
{
	private static final Identifier BACKGROUND_TEXTURE_ID = new Identifier(ZeroPointClient.MOD_ID, "textures/zero-point_background-1080p.png");
	//	private static final UrlTexture URL_TEXT = new UrlTexture("https://github.com/TheSilverEcho/ZeroPointAPI/raw/master/src/main/resources/assets/zero-point/textures/test.png");

	public static void init(MinecraftClient client)
	{
//		URL_TEXT.loadTexture();
	}


	public static void render(MatrixStack matrices, int mouseX, int mouseY, float delta, int scaledWidth, int scaledHeight, float g, MinecraftClient client, boolean reloading, float progress)
	{
		final int id = RenderUtilV2.getTextureFromLocation(BACKGROUND_TEXTURE_ID);
		RenderUtilV2.rectangleTexture(matrices, 0, 0, scaledWidth, scaledHeight, id, ColourHolder.FULL);
		if (reloading)
		{
			if (client.currentScreen != null && g < 1.0F)
				client.currentScreen.render(matrices, mouseX, mouseY, delta);
		}/* else if (URL_TEXT.isLoaded())
			RenderUtilV2.rectangleTexture(matrices, 0, 0, scaledWidth, scaledHeight, URL_TEXT.getTexture().getID(), ColourHolder.FULL);*/


		double d = Math.min((double) scaledWidth * 0.75D, scaledHeight) * 0.25D;
		double e = d * 4.0D;
		int w = (int) (e * 0.5D);

		int x = (int) ((double) scaledHeight * 0.8325D);
		final float minX = scaledWidth / 2f - w;

		RenderUtilV2.roundRect(matrices, minX, x - 5, w * 2, 10, 3, new ColourHolder(255, 255, 255, 255));
		int i = MathHelper.ceil((w * 2 - 2) * progress);
		int height = 7;
		RenderUtilV2.roundRect(matrices, minX + 1, x - height / 2f, i, height, 3, new ColourHolder(0, 75, 85, 255));

		APIFonts.REGULAR.getFont().setFontScale(0.7f).render(matrices, "Progress: %d%%".formatted((int) (progress * 100)), 10, 10);
	}
}