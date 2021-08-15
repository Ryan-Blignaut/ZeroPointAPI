package me.thesilverecho.zeropoint.impl.module;

import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
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
	private static final Identifier id = new Identifier(ZeroPointClient.MOD_ID, "textures/bg.png");

	public static void render(MatrixStack matrixStack, float width, float height, int mouseX, int mouseY, float delta)
	{

		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		AbstractTexture abstractTexture = textureManager.getTexture(id);
		int glId = abstractTexture.getGlId();
		RenderUtil.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		RenderUtil.setShaderTexture(glId);
		RenderUtil.quadTexture(matrixStack, 0, 0, width, height, new ColourHolder(255, 255, 255, 255));
		APIFonts.REGULAR.getFont().render(matrixStack, "testing 12345", 100, 100);
	}
}
