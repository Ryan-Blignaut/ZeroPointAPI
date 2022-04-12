package me.thesilverecho.zeropoint.impl.render;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.ui.APIScreen;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ConfigScreen2 extends APIScreen
{
	private boolean test;

	private float dragX, dragY;
	private boolean drag = false;
	private int valuemodx = 0;
	private static float modsRole, modsRoleNow;
	private static float valueRoleNow, valueRole;

//	public static ArrayList<Config> configs = new ArrayList<Config>();
//	public static EmptyInputBox configInputBox;


	public float lastPercent;
	public float percent;
	public float percent2;
	public float lastPercent2;
	public float outro;
	public float lastOutro;


	public ConfigScreen2()
	{
		super(new LiteralText("config screen"));

		percent = 1.33f;
		lastPercent = 1f;
		percent2 = 1.33f;
		lastPercent2 = 1f;
		outro = 1;
		lastOutro = 1;
//		valuetimer.reset();
//		configs.clear();
//		FileUtil.getConfigs();
//		new GetConfigs().start();

	}

	/*
	Main window width = 500
    Main window height = 310
    Function list start position = 100
    Feature width = 325 (unopened values)
    Feature start height = 60
	* */
	static float windowX = 200, windowY = 200;
	static float width = 500, height = 310;

//	static ClickType selectType = ClickType.Home;
//	static ModCategory modCategory = ModCategory.Combat;
//	static Mod selectMod;

	float[] typeXAnim = new float[]{windowX + 10, windowX + 10, windowX + 10, windowX + 10};

	float hy = windowY + 40;

//	TimerUtil valuetimer = new TimerUtil();

	private boolean close = false;
	private boolean closed;


	public float smoothTrans(double current, double last)
	{
		return (float) (current + (last - current) / (MinecraftClientAccessor.getCurrentFps() / 10));
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		final Window window = client.getWindow();
		percent = smoothTrans(this.percent, lastPercent);
		percent2 = smoothTrans(this.percent2, lastPercent2);
		matrixStack.push();

		final int scaledWidth = window.getScaledWidth();
		final int scaledHeight = window.getScaledHeight();
		if (percent > 0.98)
		{
			matrixStack.translate(scaledWidth / 2f, scaledHeight / 2f, 0);
			matrixStack.scale(percent, percent, 0);
			matrixStack.translate(-scaledWidth / 2f, -scaledHeight / 2f, 0);
		} else if (percent2 <= 1)
		{
			matrixStack.translate(scaledWidth / 2f, scaledHeight / 2f, 0);
			matrixStack.scale(percent2, percent2, 0);
			matrixStack.translate(-scaledWidth / 2f, -scaledHeight / 2f, 0);
		}


		if (percent <= 1.5 && close)
		{
			percent = smoothTrans(this.percent, 2);
			percent2 = smoothTrans(this.percent2, 2);
		}

		if (percent >= 1.4 && close)
		{
			percent = 1.5f;
			closed = true;
			MinecraftClient.getInstance().currentScreen = null;
		}

		RenderUtilV2.rectangle(matrixStack, 0, 0, scaledWidth, scaledHeight, new ColourHolder(107, 147, 255, 100));
//		RenderUtils.drawGradientRect(0, 0, sResolution.getScaledWidth(), sResolution.getScaledHeight(), new Color(107, 147, 255, 100).getRGB(), new Color(0, 0, 0, 30).getRGB());

//		TODO: dragging
		if (mouseX >= windowX && mouseX <= windowX + width && mouseY >= windowY && mouseY <= windowY + 20 && this.isMouseDown)
		{
			if (dragX == 0 && dragY == 0)
			{
				dragX = mouseX - windowX;
				dragY = mouseY - windowY;
			} else
			{
				windowX = mouseX - dragX;
				windowY = mouseY - dragY;
			}
			drag = true;
		} else if (dragX != 0 || dragY != 0)
		{
			dragX = 0;
			dragY = 0;
		}

	/*	if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
			if (dragX == 0 && dragY == 0) {
				dragX = mouseX - windowX;
				dragY = mouseY - windowY;
			} else {
				windowX = mouseX - dragX;
				windowY = mouseY - dragY;
			}
			drag = true;
		} else if (dragX != 0 || dragY != 0) {
			dragX = 0;
			dragY = 0;
		}*/

		RenderUtilV2.rectangle(matrixStack, windowX, windowY, width, height, new ColourHolder(21, 22, 25, 255));

		FontRenderer.renderText(APIFonts.REGULAR.getFont(), 1, "Config", new ColourHolder(77, 78, 84, 255), false, matrixStack, windowX + 20, windowY + height - 20);


		matrixStack.pop();

	}

	private boolean isMouseDown;

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		this.isMouseDown = true;
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button)
	{
		this.isMouseDown = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}


	@Override
	public void close()
	{
		this.close = true;
	}
}
