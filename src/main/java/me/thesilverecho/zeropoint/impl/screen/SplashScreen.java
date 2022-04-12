package me.thesilverecho.zeropoint.impl.screen;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SplashScreen
{
	private static final Identifier BACKGROUND_TEXTURE_ID = new Identifier(ZeroPointClient.MOD_ID, "textures/zero-point_background-1080p.png");

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
		}


		double d = Math.min((double) scaledWidth * 0.75D, scaledHeight) * 0.25D;
		double e = d * 4.0D;
		int w = (int) (e * 0.5D);

		int x = (int) ((double) scaledHeight * 0.8325D);
		final float minX = scaledWidth / 2f - w;

		RenderUtilV2.roundRect(matrices, minX, x - 5, w * 2, 10, 3, new ColourHolder(255, 255, 255, 255));
		int i = MathHelper.ceil((w * 2 - 2) * progress);
		int height = 7;
		RenderUtilV2.roundRect(matrices, minX + 1, x - height / 2f, i, height, 3, new ColourHolder(0, 75, 85, 255));
		final CustomFont font = APIFonts.REGULAR.getFont();
		FontRenderer.renderText(font, 0.75f, matrices, "Progress: %d%%".formatted((int) (progress * 100)), true, 10, 10);

		final long maxRam = Runtime.getRuntime().maxMemory();
		final long totalRam = Runtime.getRuntime().totalMemory();
		final long freeRam = Runtime.getRuntime().freeMemory();
		final long usedRam = totalRam - freeRam;
		final String amountString = "Ram used: %03d/%03d MB".formatted((int) (usedRam / 1024L / 1024L), (int) (maxRam / 1024L / 1024L));
		FontRenderer.renderText(font, 0.75f, matrices, amountString, true, scaledWidth / 2f - FontRenderer.getWidth(font, 0.75f, amountString) / 2, x - 20);


	}
}