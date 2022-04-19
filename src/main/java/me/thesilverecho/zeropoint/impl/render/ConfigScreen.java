package me.thesilverecho.zeropoint.impl.render;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.ui.APIScreen;
import me.thesilverecho.zeropoint.api.ui.widgets.ButtonComponent;
import me.thesilverecho.zeropoint.api.ui.widgets.TextBoxComponent;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ConfigScreen extends APIScreen
{
	private boolean test;

	public ConfigScreen()
	{
		super(new LiteralText("config screen"));
	}

	private TextBoxComponent textBoxComponent;
	private ButtonComponent buttonComponent;

	private void initComps()
	{
		test = true;
		final Window window = client.getWindow();
		final int halfScaledWidth = window.getScaledWidth() / 2;
		final int halfScaledHeight = client.getWindow().getScaledHeight() / 2;
		final double scaleFactor = window.getScaleFactor();
		final float winHeight = (float) (600 / scaleFactor);
		final float winWidth = (float) (920 / scaleFactor);
		final float leftOfFrame = halfScaledWidth - winWidth / 2f;
		final float topOfFrame = halfScaledHeight - winHeight / 2f;
		final float sizeOfColumn = 80;

//		ModuleManager.baseModules.forEach(simpleModuleHolder -> {simpleModuleHolder.});

		textBoxComponent = new TextBoxComponent(leftOfFrame + sizeOfColumn + 5 + 2, topOfFrame + 8, 120, 120, null, 2);
		buttonComponent = new ButtonComponent(32, 32, 32, 32, APIColour.WHITE, "test", null).setClickTask(() ->
		{
//			MusicPlayer.getPlayer().skipSong();
		});
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		if (!test)
			initComps();
		final Window window = client.getWindow();
		final int halfScaledWidth = window.getScaledWidth() / 2;
		final int halfScaledHeight = client.getWindow().getScaledHeight() / 2;
		final double scaleFactor = window.getScaleFactor();
		final float winHeight = (float) (600 / scaleFactor);
		final float winWidth = (float) (920 / scaleFactor);
		final float leftOfFrame = halfScaledWidth - winWidth / 2f;
		final float topOfFrame = halfScaledHeight - winHeight / 2f;
		final float sizeOfColumn = 80;

		RenderUtilV2.roundRect(matrixStack, leftOfFrame, topOfFrame, halfScaledWidth + winWidth / 2f, halfScaledHeight + winHeight / 2f, 10, APIColour.decode("#272537"));
//		APIFonts.FREE_SANS.getFont().setFontScale((float) ((1 / scaleFactor) / 18 * 22)).render(matrixStack, "${#f8f8ff}ZERO-POINT", leftOfFrame + 10, topOfFrame + 8);
		textBoxComponent.render(matrixStack, mouseX, mouseY, delta);
		buttonComponent.render(matrixStack, mouseX, mouseY, delta);
		super.render(matrixStack, mouseX, mouseY, delta);
//		//search button
//		final float scale = (float) ((1 / scaleFactor) / 18 * 20);
//		final String searchSting = "${#f8f8ff}Search";
//		final float width = APIFonts.FREE_SANS.getFont().getWidth(searchSting);
//		RenderUtil.roundRect(matrixStack, leftOfFrame + sizeOfColumn + 5 + 2, topOfFrame + 8, leftOfFrame + sizeOfColumn + 5 + 2 + width, topOfFrame + 16, 2, ColourHolder.decode("#B0B0B0"));
//		APIFonts.FREE_SANS.getFont().setFontScale(scale).render(matrixStack, searchSting, leftOfFrame + sizeOfColumn + 5 + 2, topOfFrame + 8);
	}

	/*@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	@Override
	public void onClose()
	{
		super.onClose();
	}*/
}
