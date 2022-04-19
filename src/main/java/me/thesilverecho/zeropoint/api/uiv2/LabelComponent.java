package me.thesilverecho.zeropoint.api.uiv2;

import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer2;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Supplier;

public class LabelComponent extends Component
{
	private IconComponent iconComponent;
	private CustomFont font = APIFonts.REGULAR.getFont();
	private float fontSize = 0.75f;
	private Positioning.Horizontal textHorizontalPositioning = Positioning.Horizontal.CENTER;
	private Positioning.Vertical textVerticalPositioning = Positioning.Vertical.CENTER;
	private Supplier<String> text;
	private APIColour colour = APIColour.WHITE;

	public LabelComponent(float x, float y, float w, float h, Supplier<String> text)
	{
		super(x, y, w, h);
		this.text = text;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		final float width = FontRenderer.getWidth(font, fontSize, text.get());
		final float height = FontRenderer.getHeight(font, fontSize);
		final float textPosX = textHorizontalPositioning == Positioning.Horizontal.LEFT ? x : textHorizontalPositioning == Positioning.Horizontal.RIGHT ? x + w - width : x + w / 2 - width / 2;
		final float textPosY = textVerticalPositioning == Positioning.Vertical.TOP ? y : textVerticalPositioning == Positioning.Vertical.BOTTOM ? y + h - height : y + h / 2 - height / 2;
		FontRenderer2.renderText(matrixStack, textPosX, textPosY, font, fontSize, text.get(), colour);
	}

	public LabelComponent setColour(APIColour colour)
	{
		this.colour = colour;
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
