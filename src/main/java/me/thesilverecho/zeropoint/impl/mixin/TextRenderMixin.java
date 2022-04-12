package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TextRenderer.class)
public abstract class TextRenderMixin
{

/*
	*//**
	 * @author
	 *//*
	@Overwrite
	public int draw(String text, float x, float y, int color, boolean dropShadow,
			Matrix4f matrix, VertexConsumerProvider source, boolean seeThrough,
			int colorBackground, int packedLight, boolean bidiFlag)
	{
		return Math.round(FontRenderer.renderText(APIFonts.REGULAR.getFont(), matrix, text, x, y));
	}*/

/*	*//**
	 * @author
	 *//*
	@Overwrite
	public int draw(OrderedText text, float x, float y, int color, Matrix4f matrix, boolean shadow)
	{
		final int[] round = {0};

		text.accept((index, style, codePoint) ->
		{
			round[0] += Math.round(FontRenderer.renderText(APIFonts.REGULAR.getFont(), matrix, Character.toString(codePoint), x + index * FontRenderer.getWidth(APIFonts.REGULAR.getFont(), 1, Character.toString(codePoint)), y));
			return true;
		});
		return round[0];
	}*/
/*
	@Shadow
	public abstract String mirror(String text);

	private static int tweakTransparency(int argb)
	{
		return (argb & -67108864) == 0 ? argb | -16777216 : argb;
	}

	@Inject(method = "drawLayer(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)F", at = @At(value = "HEAD"), cancellable = true)
	private void drawInternal(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light, CallbackInfoReturnable<Float> cir)
	{
		cir.cancel();
		cir.setReturnValue(APIFonts.THIN.getFont().render(matrix, text, new ColourHolder(255, 255, 255, 255), x, y, 1, false));
	}

	@Inject(method = "drawLayer(Lnet/minecraft/text/OrderedText;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)F", at = @At(value = "HEAD"), cancellable = true)
	private void drawInternal(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light, CallbackInfoReturnable<Float> cir)
	{
		cir.cancel();
		final String[] s = {""};
		text.accept((index, style, codePoint) ->
		{
			s[0] += (char) codePoint;
			return true;
		});
		final CustomFont font = APIFonts.THIN.getFont();
		cir.setReturnValue(font.render(matrix, s[0], new ColourHolder(255, 255, 255, 255), x, y - (font.getHeight() * 0.74f) / 2f, 0.74f, false));
//		cir.setReturnValue(light.get());
//		cir.setReturnValue(APIFonts.THIN.getFont().render(matrix, text, new ColourHolder(255, 255, 255, 255), x, y, 1, false));
	}
*/
}
