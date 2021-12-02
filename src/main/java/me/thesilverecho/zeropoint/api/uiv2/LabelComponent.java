package me.thesilverecho.zeropoint.api.uiv2;

import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Supplier;

public class LabelComponent extends Component
{
	private ColourHolder foreground = ColourHolder.FULL;
	private CustomFont font = APIFonts.REGULAR.getFont();
	private float fontSize = 0.75f;
	private IconComponent iconComponent;
	private Positioning.Horizontal textHorizontalPositioning = Positioning.Horizontal.CENTER;
	private Positioning.Vertical textVerticalPositioning = Positioning.Vertical.CENTER;

	private Supplier<String> text;


	public LabelComponent(float x, float y, float w, float h, Supplier<String> text)
	{
		super(x, y, w, h);
		this.text = text;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		RenderUtilV2.rectangle(matrixStack, x, y, w, h, getBackground());
		final float width = FontRenderer.getWidth(font, fontSize, text.get());
		final float height = FontRenderer.getHeight(font, fontSize);

		final float textPosX = textHorizontalPositioning == Positioning.Horizontal.LEFT ? x : textHorizontalPositioning == Positioning.Horizontal.RIGHT ? x + w - width : x + w / 2 - width / 2;
		final float textPosY = textVerticalPositioning == Positioning.Vertical.TOP ? y : textVerticalPositioning == Positioning.Vertical.BOTTOM ? y + h - height : y + h / 2 - height / 2;

		FontRenderer.renderText(font, fontSize, matrixStack, text.get(), textPosX, textPosY);
	}

	public LabelComponent setForeground(ColourHolder foreground)
	{
		this.foreground = foreground;
		return this;
	}

	public LabelComponent setFont(CustomFont font)
	{
		this.font = font;
		return this;
	}

	public LabelComponent setFontSize(float fontSize)
	{
		this.fontSize = fontSize;
		return this;
	}

	public LabelComponent setIconComponent(IconComponent iconComponent)
	{
		this.iconComponent = iconComponent;
		return this;
	}

	public LabelComponent setTextHorizontalPositioning(Positioning.Horizontal textHorizontalPositioning)
	{
		this.textHorizontalPositioning = textHorizontalPositioning;
		return this;
	}

	public LabelComponent setTextVerticalPositioning(Positioning.Vertical textVerticalPositioning)
	{
		this.textVerticalPositioning = textVerticalPositioning;
		return this;
	}

	public LabelComponent setText(String text)
	{
		this.text = () -> text;
		return this;
	}
}
