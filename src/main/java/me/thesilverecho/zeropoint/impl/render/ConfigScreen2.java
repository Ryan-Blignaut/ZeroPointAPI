package me.thesilverecho.zeropoint.impl.render;

import me.thesilverecho.zeropoint.api.config.selector.FloatSliderHolder;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer2;
import me.thesilverecho.zeropoint.api.ui.APIScreen;
import me.thesilverecho.zeropoint.api.util.APIColour;
import me.thesilverecho.zeropoint.impl.mixin.MinecraftClientAccessor;
import me.thesilverecho.zeropoint.impl.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.concurrent.atomic.AtomicReference;

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

	@Override
	protected void init()
	{
		super.init();
//		addComp(new Pane(0, 0, width, height));


	}

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

		RenderUtilV2.rectangle(matrixStack, 0, 0, scaledWidth, scaledHeight, new APIColour(107, 147, 255, 100));
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

		RenderUtilV2.rectangle(matrixStack, windowX, windowY, width, height, new APIColour(21, 22, 25, 255));

		FontRenderer.renderText(APIFonts.REGULAR.getFont(), 1, "Config", new APIColour(77, 78, 84, 255), false, matrixStack, windowX + 20, windowY + height - 20);
		AtomicReference<Float> pos = new AtomicReference<>(1f);

		ModuleManager.INSTANCE.baseModules.forEach((s, baseModule) ->
		{
			final CustomFont font = APIFonts.UD.getFont();
			final float size = 0.65f;
//			FontRenderer.renderText(font, size, baseModule.getName(), new APIColour(77, 78, 84, 255), false, matrixStack, windowX, windowY + pos.get());
			FontRenderer2.renderText(matrixStack, windowX, windowY + pos.get(), font, size, baseModule.getName(), new APIColour(77, 78, 84, 255), false);


			pos.set(pos.get() + FontRenderer.getHeight(font, size));

			baseModule.getSettingHolders().getAllSettings().forEach(settingHolder ->
			{

				final float w = FontRenderer2.renderText(matrixStack, windowX + 13, windowY + pos.get() + 2, font, size, settingHolder.getName(), new APIColour(77, 78, 84, 255), false);
				final float v = FontRenderer2.renderText(matrixStack, w + 15, windowY + pos.get() + 2, font, size, settingHolder.serialize(), new APIColour(77, 78, 84, 255), false);
				final float height = FontRenderer.getHeight(font, size);
				if (settingHolder instanceof FloatSliderHolder)
					RenderUtilV2.rectangle(matrixStack, v + 15, windowY + pos.get() + 2, 32, height - 2, new APIColour(77, 78, 84, 255));

				pos.set(pos.get() + height);

				/*final float w = FontRenderer2.renderText(matrixStack, windowX + 13, windowY + pos.get() + 2, APIFonts.THIN.getFont(), settingHolder.getName(), new APIColour(77, 78, 84, 255), false);
				final float v = FontRenderer2.renderText(matrixStack, w + 15, windowY + pos.get() + 2, APIFonts.THIN.getFont(), settingHolder.serialize(), new APIColour(77, 78, 84, 255), false);
				if (settingHolder instanceof FloatSliderHolder)
					RenderUtilV2.rectangle(matrixStack, v + 15, windowY + pos.get() + 2, 32, 5, new APIColour(77, 78, 84, 255));

				pos.set(pos.get() + FontRenderer2.getHeight(APIFonts.THIN.getFont()));*/
			});


		});


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
