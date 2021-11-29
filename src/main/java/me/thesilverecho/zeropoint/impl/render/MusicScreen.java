package me.thesilverecho.zeropoint.impl.render;

import me.thesilverecho.zeropoint.api.ui.APIScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class MusicScreen extends APIScreen
{
	private boolean test;

	public MusicScreen()
	{
		super(new LiteralText("Music screen"));
	}

	private void initComps()
	{
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	@Override
	public void onClose()
	{
		super.onClose();
	}
}
