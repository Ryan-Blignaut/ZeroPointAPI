package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.render.Framebuffer2;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;

public class ScreenRender
{
	private static final String urlStr = "https://media.forgecdn.net/avatars/thumbnails/392/393/64/64/637586553764037908.png";
	private static final Identifier id = new Identifier(ZeroPointClient.MOD_ID, "textures/bg.png");
	private static Framebuffer2 framebuffer;
	private static ArrayList<Vec2f> points = new ArrayList<>();

	public static void render(MatrixStack matrixStack, float width, float height, int mouseX, int mouseY, float delta)
	{
		final int loc = RenderUtilV2.getTextureFromLocation(new Identifier(ZeroPointClient.MOD_ID, "textures/bg.png"));
//		RenderUtilV2.rectangleTexture(matrixStack, 0, 0, width, height, loc, new ColourHolder(255, 255, 255, 255));
		RenderUtilV2.rectangle(matrixStack, 0, 0, width, height, 0, new ColourHolder(50, 50, 50, 255));
		RenderUtilV2.bezier(matrixStack, 0, 0, 128, 128, new Vec2f(10, 100), new Vec2f(25, 25), new Vec2f(40, 100), 1f, new ColourHolder(255, 255, 255, 50));
		RenderUtilV2.bezier(matrixStack, 0, 30, new Vec2f(40, 0), new Vec2f(40 + (70 - 40) / 2f, 120), new Vec2f(70, 0), 1f, ColourHolder.FULL);






		//		RenderUtilV2.bezier(matrixStack, 0, 0, 128, 128, ColourHolder.FULL);
//		RenderUtilV2.rectangleFbo(0, 0, width, height, 0, framebuffer.texture, new ColourHolder(255, 0, 0, 255));
//		RenderUtilV2.roundRectTexture(matrixStack, mouseX - 32 / 2f, mouseY - 32 / 2f, 32, 32, 3, loc, new ColourHolder(255, 255, 255, 255));
//		RenderUtilV2.circleTexture(matrixStack, 32, 32, 64, 64, 32, 4, loc, new ColourHolder(255, 255, 255, 255));
//		RenderUtilV2.rectangle(matrixStack, 0, height - 32, 32, 32, 0, new ColourHolder(0, 32, 32, 255));
//		RenderUtilV2.roundRect(matrixStack, 32, height - 32, 32, 32, 4, new ColourHolder(0, 32, 32, 255));
//		RenderUtilV2.circle(matrixStack, 64, height - 32, 32, 32, 4, new ColourHolder(0, 32, 32, 255));
//		RenderUtilV2.hexagon(matrixStack, 0, 0, 300, 300, new ColourHolder(32, 32, 0, 255));
	/*	if (texture2D == null && attempts < 20)
		{
			try
			{
				final HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
				final InputStream stream = conn.getInputStream();
				texture2D = Texture2D.read(ApiIOUtils.readBytesToBuffer(stream), Texture2D.Format.RGB);

			} catch (IOException e)
			{
				attempts++;
				e.printStackTrace();
			}
		}

		AbstractTexture abstractTexture = textureManager.getTexture(id);
		int glId = abstractTexture.getGlId();
		RenderUtil.setShader(APIShaders.FONT_MASK_TEXTURE.getShader());
		RenderUtil.setShaderTexture(glId);
		RenderUtil.quadTexture(matrixStack, 0, 0, width, height, new ColourHolder(1, 1, 1, 255));
		if (texture2D != null)
		{
			RenderUtil.setShaderTexture(texture2D.getID());
			RenderUtil.quadTexture(matrixStack, 90, 90, 180, 180, new ColourHolder(0, 255, 0, 255));
		}
		framebuffer2.bind();
		APIFonts.ICON.getFont().setFontScale(0.7f).render(matrixStack, "a b c d ${#2E464C,#7CF384,#2E464C,#2E464C}e ${#FFCD4A,#FFCD4A,#2E464C,#2E464C}f g h i j k l m n o p q r s ${#7CF384,#2E464C,#2E464C,#2E464C}t u", 32, 32);
		APIFonts.ICON.getFont().setFontScale(0.85f).render(matrixStack, "${#457b9d,#457b9d,#b3dee2,#b3dee2}t", 64, 64);
		APIFonts.ICON.getFont().setFontScale(0.85f).render(matrixStack, "${#a8dadc}p", 64, 64);
		framebuffer2.unbind();
		final Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		framebuffer.beginWrite(false);
		APIFonts.ICON.getFont().setFontScale(0.85f).render(matrixStack, "${#457b9d,#457b9d,#b3dee2,#b3dee2}t", 64, 64);

		RenderUtil.setShaderTexture(framebuffer2.texture);
		RenderUtil.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		RenderUtil.quadTexture(matrixStack, 0, 0, framebuffer.textureWidth, framebuffer.viewportHeight, new ColourHolder(1, 1, 1, 255));
*/

//		BlurShader.setUpFBO(MinecraftClient.getInstance().getFramebuffer());
//		BlurShader.setUps(2, new Vec2f(0, 1));
//		final Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();

		//		ZeroPointClient.postProcessShader.drawInternal();
	}
}
