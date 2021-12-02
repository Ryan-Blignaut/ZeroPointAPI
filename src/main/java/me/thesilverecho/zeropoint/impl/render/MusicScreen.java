package me.thesilverecho.zeropoint.impl.render;

import me.thesilverecho.zeropoint.api.ui.APIScreen;
import me.thesilverecho.zeropoint.api.uiv2.LabelComponent;
import me.thesilverecho.zeropoint.api.uiv2.Pane;
import me.thesilverecho.zeropoint.api.uiv2.Positioning;
import me.thesilverecho.zeropoint.api.uiv2.VerticalPane;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.Random;

public class MusicScreen extends APIScreen
{
	public MusicScreen()
	{
		super(new LiteralText("Music screen"));
		initComps();
	}

	private Pane pane;
	private ArrayList<LabelComponent> components = new ArrayList<>();

	private void initComps()
	{
		pane = new VerticalPane(100, 100, 100, 100);
		components.clear();
		for (int i = 0; i < 32; i++)
		{
			LabelComponent lblTest = new LabelComponent(32, 32, 90, 30, () -> String.valueOf(Math.round(Math.random() * 100)))
					.setTextHorizontalPositioning(Positioning.Horizontal.CENTER)
					.setTextVerticalPositioning(Positioning.Vertical.CENTER)
					.setFontSize(0.72f);
			lblTest.setBackground(ColourHolder.decode("#2b2b2b"));
			components.add(lblTest);

			pane.addComponent(lblTest);
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		pane.render(matrixStack, mouseX, mouseY, delta);
		components.forEach(l ->
		{
			final Positioning.Horizontal textHorizontalPositioning = new Random().nextBoolean() ? Positioning.Horizontal.RIGHT : Positioning.Horizontal.LEFT;
			l.setTextHorizontalPositioning(textHorizontalPositioning);
		});
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
