package me.thesilverecho.zeropoint.impl.screen;

import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.uiv2.ButtonComponent;
import me.thesilverecho.zeropoint.api.uiv2.Pane;
import me.thesilverecho.zeropoint.api.uiv2.VerticalPane;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vector4f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class TitleScreen
{
	private static final Identifier START_ID = new Identifier(ZeroPointClient.MOD_ID, "textures/zero-point_background_start.png");

	private static Pane pane;

	public static void init(MinecraftClient client, Screen screen, int width, int height)
	{
		pane = new Pane(0, 0, width, height);
//		pane.setImageBackground(RenderUtilV2.getTextureFromLocation(START_ID));
		final VerticalPane buttonPane = new VerticalPane(0, 0, 120, height);
//		buttonPane.setBackground(new ColourHolder(50, 50, 50, 190));

		ButtonComponent singlePlayerButton = new ButtonComponent(0, 0, 50, 16, "Singleplayer", () -> client.setScreen(new SelectWorldScreen(screen)));
		singlePlayerButton.setBackground(ColourHolder.decode("#FF2b2b"));
		singlePlayerButton.getText().setFontSize(0.5f);

		ButtonComponent multiplayerButton = new ButtonComponent(0, 0, 50, 16, "Multiplayer", () -> client.setScreen(new MultiplayerScreen(screen)));
		multiplayerButton.setBackground(ColourHolder.decode("#2b7b2b"));

		ButtonComponent settingsButton = new ButtonComponent(0, 0, 50, 16, "Settings", () -> client.setScreen(new OptionsScreen(screen, client.options)));
		settingsButton.setBackground(ColourHolder.decode("#FF2b2b"));

		ButtonComponent quitButton = new ButtonComponent(0, 0, 50, 16, "Quit", client::scheduleStop);
		quitButton.setBackground(ColourHolder.decode("#2b7b2b"));


		pane.addComponent(buttonPane);
		buttonPane.addComponent(singlePlayerButton);
		buttonPane.addComponent(multiplayerButton);
		buttonPane.addComponent(settingsButton);
		buttonPane.addComponent(quitButton);


	}


	private static float time = (float) glfwGetTime();
	private static float prev_time = 0;

	public static void render(MatrixStack matrixStack, float width, float height, int mouseX, int mouseY, float delta)
	{

		RenderUtilV2.setShader(APIShaders.WATER_RIPPLE.getShader());
		final int textureFromLocation = RenderUtilV2.getTextureFromLocation(new Identifier(ZeroPointClient.MOD_ID, "textures/bg12.png"));
		GLWrapper.activateTexture(0, textureFromLocation);
		RenderUtilV2.setShaderUniform("sampler0", 0);
		GLWrapper.activateTexture(1, RenderUtilV2.getTextureFromLocation(new Identifier(ZeroPointClient.MOD_ID, "textures/bg12_mask.png")));
		RenderUtilV2.setShaderUniform("sampler1", 1);
		GLWrapper.activateTexture(2, RenderUtilV2.getTextureFromLocation(new Identifier(ZeroPointClient.MOD_ID, "textures/n2.png")));
		RenderUtilV2.setShaderUniform("sampler2", 2);
		RenderUtilV2.setTextureId(textureFromLocation);
		RenderUtilV2.setShaderUniform("Sampler0", 0);
		RenderUtilV2.setShaderUniform("Sampler1", 1);
		RenderUtilV2.setShaderUniform("Sampler2", 2);
		RenderUtilV2.setShaderUniform("Strength", 0.1f);
		RenderUtilV2.setShaderUniform("Texture0Res", new Vector4f(0, 0, 3840, 2160));
		RenderUtilV2.setShaderUniform("Texture1Res", new Vector4f(0, 0, 3840, 2160));
		RenderUtilV2.setShaderUniform("AnimationSpeed", 0.15f);
		RenderUtilV2.setShaderUniform("Scale", 1f);
		RenderUtilV2.setShaderUniform("ScrollSpeed", 0f);
		RenderUtilV2.setShaderUniform("Direction", 0f);
		RenderUtilV2.setShaderUniform("Time", time);
		RenderUtilV2.setQuadColourHolder(ColourHolder.decode("#9aa4db"));
		RenderUtilV2.quadTexture(matrixStack, 0, 0, width, height, new ColourHolder(0, 0, 0, 255));


		float buttonPaneWidth = 94, buttonPaneHeight = 150;
		final ColourHolder primaryCol = ColourHolder.decode("#2d2c2c");
		RenderUtilV2.rectangle(matrixStack, width / 2 - buttonPaneWidth / 2, height / 2 - buttonPaneHeight / 2 - 60, buttonPaneWidth, buttonPaneHeight, primaryCol);
		buttonPaneWidth = buttonPaneWidth - 3;
		buttonPaneHeight = buttonPaneHeight - 3;
		RenderUtilV2.rectangle(matrixStack, width / 2 - buttonPaneWidth / 2, height / 2 - buttonPaneHeight / 2 - 60, buttonPaneWidth, buttonPaneHeight, ColourHolder.decode("#111111"));

		buttonPaneWidth = buttonPaneWidth - 10;
		buttonPaneHeight = buttonPaneHeight - 10;
		RenderUtilV2.rectangle(matrixStack, width / 2 - buttonPaneWidth / 2, height / 2 - buttonPaneHeight / 2 - 60, buttonPaneWidth, buttonPaneHeight, primaryCol);


		buttonPaneWidth = buttonPaneWidth - 2;
		buttonPaneHeight = buttonPaneHeight - 2;
		RenderUtilV2.rectangle(matrixStack, width / 2 - buttonPaneWidth / 2, height / 2 - buttonPaneHeight / 2 - 60, buttonPaneWidth, buttonPaneHeight, ColourHolder.decode("#111111"));

		buttonPaneWidth = buttonPaneWidth + 2 - 5;
		buttonPaneHeight = buttonPaneHeight + 2 + FontRenderer.getHeight(APIFonts.REGULAR.getFont(), 0.35f) / 2;
		FontRenderer.renderText(APIFonts.REGULAR.getFont(), 0.35f, "Main Menu", new ColourHolder(200, 222, 222, 255), false, matrixStack, width / 2 - buttonPaneWidth / 2, height / 2 - buttonPaneHeight / 2 - 60);


		buttonPaneWidth = buttonPaneWidth - 5;

		float buttonHeight = 16;

		final float bx = width / 2 - buttonPaneWidth / 2;
		final float by = height / 2 - buttonHeight / 2 - 60;

		final int radius = 5;

		RenderUtilV2.roundRectAdjust(matrixStack, bx, by - buttonHeight * 3, buttonPaneWidth, buttonHeight, radius, radius, -radius, 0, radius, primaryCol);

		//FixMe: when there is no rounding the edge of the button is not smoothly drawn(The smooth step is ineffective).
		final float sizeHack = 0.5f;
		if (mouseX > bx + sizeHack && mouseX < bx + sizeHack + buttonPaneWidth - 1 && mouseY > by - buttonHeight * 2 && mouseY < by - buttonHeight)
			RenderUtilV2.roundRectAdjust(matrixStack, bx + sizeHack, by - buttonHeight * 2, buttonPaneWidth - 1, buttonHeight, 0, 0, 0, 0, radius, ColourHolder.FULL);
		else
			RenderUtilV2.roundRectAdjust(matrixStack, bx + sizeHack, by - buttonHeight * 2, buttonPaneWidth - 1, buttonHeight, 0, 0, 0, 0, radius, primaryCol);


		RenderUtilV2.roundRectAdjust(matrixStack, bx + sizeHack, by - buttonHeight, buttonPaneWidth - 1, buttonHeight, 0, 0, 0, 0, radius, primaryCol);
		RenderUtilV2.roundRectAdjust(matrixStack, bx + sizeHack, by, buttonPaneWidth - 1, buttonHeight, 0, 0, 0, 0, radius, primaryCol);
		RenderUtilV2.roundRectAdjust(matrixStack, bx + sizeHack, by + buttonHeight, buttonPaneWidth - 1, buttonHeight, 0, 0, 0, 0, radius, primaryCol);
		RenderUtilV2.roundRectAdjust(matrixStack, bx + sizeHack, by + buttonHeight * 2, buttonPaneWidth - 1, buttonHeight, 0, 0, 0, 0, radius, primaryCol);
		RenderUtilV2.roundRectAdjust(matrixStack, bx, by + buttonHeight * 3, buttonPaneWidth, buttonHeight, radius, 0, -radius, -radius, radius, primaryCol);

//		RenderUtilV2.roundRectLine(matrixStack, bx-1, by - buttonHeight * 3, buttonPaneWidth+2, buttonHeight * 7, radius, 4f, primaryCol);


		float cur_time = (float) glfwGetTime();
		time += cur_time - prev_time;
		prev_time = cur_time;
		pane.render(matrixStack, mouseX, mouseY, delta);
	}

	public static void mouseClick(double mouseX, double mouseY, int button)
	{
		pane.onClick(mouseX, mouseY, button);
	}
}
