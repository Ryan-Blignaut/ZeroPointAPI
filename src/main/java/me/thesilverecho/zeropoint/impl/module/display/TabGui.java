package me.thesilverecho.zeropoint.impl.module.display;

import com.google.common.util.concurrent.AtomicDouble;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.KeyEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.module.render2.BlurBackground;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@ClientModule(name = "Tab Gui", active = true, keyBinding = GLFW.GLFW_KEY_RIGHT_ALT)
public class TabGui extends BaseModule
{
	private static final Comparator<String> COMPARATOR = Comparator.comparingDouble(value -> FontRenderer.getWidth(APIFonts.THIN.getFont(), 0.35f, value));

	@EventListener
	public void renderEvent(Render2dEvent.Pre event)
	{
		final MatrixStack matrixStack = event.matrixStack();
		AtomicDouble yCord = new AtomicDouble(3);
		AtomicInteger index = new AtomicInteger(0);
		BaseModule.ENABLE_MODULES2.values().stream().map(BaseModule::getName).sorted(COMPARATOR.reversed()).
		                          forEach((s) ->
		                          {
			                          final CustomFont font = APIFonts.REGULAR.getFont();
			                          final double v = yCord.get();
			                          final float height = FontRenderer.getHeight(font, 0.35f);
			                          final float andAdd = (float) yCord.getAndAdd(height);

			                          BlurBackground.renderToBlur(() ->
			                          {
				                          RenderUtilV2.rectangle(matrixStack, 2, (float) v, FontRenderer.getWidth(font, 0.35f, s) + 3, height, 0, new ColourHolder(10, 10, 10, 89));
			                          });
			                          Color analogous = getAnalogousColor(ColourHolder.decode("#a43bcb"))[0];
			                          Color textColor = interpolateColorsBackAndForth(35, index.get() * 20, new Color(55 * 2, 2, 88 * 2, 255), analogous, true);

			                          FontRenderer.renderText(font, 0.35f, s, new ColourHolder(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 255), false, matrixStack, 3, andAdd - 1);
			                          index.getAndIncrement();
		                          });

	}

	@EventListener
	public void renderEvent(KeyEvent event)
	{
		final int key = event.key();

	}

	public static Color[] getAnalogousColor(ColourHolder color)
	{
		Color[] colors = new Color[2];
		float[] hsb = Color.RGBtoHSB(color.red(), color.green(), color.blue(), null);

		float degree = 30 / 360f;

		float newHueAdded = hsb[0] + degree;
		colors[0] = new Color(Color.HSBtoRGB(newHueAdded, hsb[1], hsb[2]));

		float newHueSubtracted = hsb[0] - degree;

		colors[1] = new Color(Color.HSBtoRGB(newHueSubtracted, hsb[1], hsb[2]));

		return colors;
	}

	public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor)
	{
		int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
		angle = (angle >= 180 ? 360 - angle : angle) * 2;
		return trueColor ? interpolateColorHue(start, end, angle / 360f) : interpolateColorC(start, end, angle / 360f);
	}

	public static Color interpolateColorHue(Color color1, Color color2, float amount)
	{
		amount = Math.min(1, Math.max(0, amount));

		float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
		float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

		Color resultColor = Color.getHSBColor(interpolateFloat(color1HSB[0], color2HSB[0], amount),
				interpolateFloat(color1HSB[1], color2HSB[1], amount), interpolateFloat(color1HSB[2], color2HSB[2], amount));

		return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(),
				interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
	}

	public static Color interpolateColorC(Color color1, Color color2, float amount)
	{
		amount = Math.min(1, Math.max(0, amount));
		return new Color(interpolateInt(color1.getRed(), color2.getRed(), amount),
				interpolateInt(color1.getGreen(), color2.getGreen(), amount),
				interpolateInt(color1.getBlue(), color2.getBlue(), amount),
				interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
	}

	public static float interpolateFloat(float oldValue, float newValue, double interpolationValue)
	{
		return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
	}

	public static int interpolateInt(int oldValue, int newValue, double interpolationValue)
	{
		return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
	}

	public static Double interpolate(double oldValue, double newValue, double interpolationValue)
	{
		return (oldValue + (newValue - oldValue) * interpolationValue);
	}


}
