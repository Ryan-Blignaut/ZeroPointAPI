package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.ui.widgets.ButtonComponent;
import me.thesilverecho.zeropoint.api.ui.widgets.TextBoxComponent;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ScreenRender
{
	private static final TextBoxComponent textBoxComponent = new TextBoxComponent(52, 52, 20, 20, null, 2);
	private static final String base = "";

	public static void render(MatrixStack matrixStack, float width, float height, int mouseX, int mouseY, float delta)
	{
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		final Identifier id = new Identifier(ZeroPointClient.MOD_ID, "textures/bg.png");
		AbstractTexture abstractTexture = textureManager.getTexture(id);
		int glId = abstractTexture.getGlId();
//		framebuffer.resize();
		RenderUtil.setShader(APIShaders.TEXT_SHADER.getShader());
		RenderUtil.setShaderTexture(glId);
		RenderUtil.quadTexture(matrixStack, 0, 0, width, height, 0, 0, 1, 1, new ColourHolder(255, 255, 255, 255));
		APIFonts.REGULAR.getFont().setFontScale(0.4f).render(matrixStack, "${#1D1D1D}Version A - 1.2", 1, 1);
		final ButtonComponent test = new ButtonComponent(10, 10, 32, 32, ColourHolder.decode("#F4F4D7"), "test", null);
		test.setFont(APIFonts.REGULAR.getFont());
		test.render(matrixStack, mouseX, mouseY, delta);
		APIFonts.ICON.getFont().setFontScale(0.7f).render(matrixStack, "a b c d e ${#FFCD4A}f g h i j k l m n o p q r s ${#7CF384}t u", 32, 32);
		APIFonts.ICON.getFont().setFontScale(0.85f).render(matrixStack, "${#457b9d,#457b9d,#b3dee2,#b3dee2}t", 64, 64);
		APIFonts.ICON.getFont().setFontScale(0.85f).render(matrixStack, "${#a8dadc}p", 64, 64);


		//		textBoxComponent.render(matrixStack, mouseX, mouseY, delta);
//		framebuffer.bind();
//		APIFonts.REGULAR.getFont().render(matrixStack, "FBO texture text", width, height, 1);
//		framebuffer.unbind();
//		RenderUtil.setShader(APIShaders.TEXT_SHADER.getShader());
//		RenderUtil.postProcessText(framebuffer.texture);
	}
}
