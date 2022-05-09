package me.thesilverecho.zeropoint.impl.module.render3;

import me.thesilverecho.zeropoint.api.config.selector.FloatSliderHolder;
import me.thesilverecho.zeropoint.api.config.selector.FontHolder;
import me.thesilverecho.zeropoint.api.config.selector.SettingHolder;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV4;
import me.thesilverecho.zeropoint.api.render.font.*;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.util.APIColour;
import me.thesilverecho.zeropoint.impl.event.TextRenderEvent;
import me.thesilverecho.zeropoint.impl.mixin.TextRenderAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "CustomFont", active = true, keyBinding = GLFW.GLFW_KEY_ENTER)
public class CustomClientFont extends BaseModule
{

	private final SettingHolder<CustomFont> font = settingHolders.addItem(new FontHolder("Custom fon name", "Changes the default minecraft font", APIFonts.UD.getFontUnloaded()));
	private final SettingHolder<Float> size = settingHolders.addItem(new FloatSliderHolder("Font size", "Changes the size of the font", 0.5F, 0.1f, 2.0f, 0.1f));

	@EventListener
	public void renderOrderedText(TextRenderEvent.RenderOrderedText e)
	{
		e.cir().cancel();
		final StyledTextLookup styledTextLookup = new StyledTextLookup();
		styledTextLookup.apply(e.text());
		float w = 0;
		final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		RenderUtilV4.setShader(APIShaders.FONT_MASK_TEXTURE.getShader());
		RenderUtilV4.setTexture(font.getValue().getTexture().getID());
//		GLWrapper.enableGL2D();
		final float oldZ = RenderUtilV4.getZIndex();
		RenderUtilV4.setZIndex(e.zIndex());
		RenderUtilV4.beginDraw(buffer);
		for (int i = 0; i < styledTextLookup.getCharterList().size(); i++)
		{
			final char charter = styledTextLookup.getCharterList().get(i);
			final Style style = styledTextLookup.getStyleChangeList().get(i);
			final TextColor styleColor = style.getColor();
			final int rgb = styleColor == null ? e.color() : styleColor.getRgb();
			RenderUtilV4.setQuadColourHolder(new APIColour(rgb));
			w += FontRenderer3.renderCharOffset(font.getValue(), size.getValue(), charter, e.matrix(), e.x() + w, e.y(), buffer);
		}
		RenderUtilV4.endDraw(buffer);
		RenderUtilV4.renderQuad(buffer, false);
		RenderUtilV4.setZIndex(oldZ);
		e.cir().setReturnValue(e.x() + w);
	}

	@EventListener
	public void renderOrderedText(TextRenderEvent.RenderStringText e)
	{
		e.cir().cancel();
		final float oldZ = RenderUtilV4.getZIndex();
		RenderUtilV4.setZIndex(e.zIndex());
		float w = 0;
		w += FontRenderer3.renderText(font.getValue(), size.getValue(), e.text(), APIColour.WHITE, e.shadow(), e.matrix(), e.x() + w, e.y());
		RenderUtilV4.setZIndex(oldZ);
		e.cir().setReturnValue(e.x() + w);
	}

	@EventListener
	public void modifyWidthString(TextRenderEvent.WidthStringText e)
	{
		e.cir().setReturnValue(MathHelper.ceil(FontRenderer.getWidth(font.getValue(), size.getValue(), e.text())));
	}

	@EventListener
	public void modifyWidthOrdered(TextRenderEvent.WidthOrderedText e)
	{
		e.cir().cancel();
		final StyledTextLookup styledTextLookup = new StyledTextLookup();
		styledTextLookup.apply(e.text());
		int w = 0;
		for (int i = 0; i < styledTextLookup.getCharterList().size(); i++)
		{
			final char charter = styledTextLookup.getCharterList().get(i);
			w += MathHelper.ceil(FontRenderer.getWidth(font.getValue(), size.getValue(), String.valueOf(charter)));
		}
		e.cir().setReturnValue(w);
	}

	@Override
	public void onEnable()
	{
		((TextRenderAccessor) MinecraftClient.getInstance().textRenderer).setFontHeight(MathHelper.ceil(FontRenderer.getHeight(font.getValue(), size.getValue())));
		super.onEnable();
	}

	@Override
	public void onDisable()
	{
		((TextRenderAccessor) MinecraftClient.getInstance().textRenderer).setFontHeight(9);


		super.onEnable();
	}


}
