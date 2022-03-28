package me.thesilverecho.zeropoint.impl.screen;

import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import me.thesilverecho.zeropoint.api.uiv2.ButtonComponent;
import me.thesilverecho.zeropoint.api.uiv2.Pane;
import me.thesilverecho.zeropoint.api.uiv2.VerticalPane;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vector4f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class TitleScreen
{
	private static final Identifier START_ID = new Identifier(ZeroPointClient.MOD_ID, "textures/zero-point_background_start.png");

	private static Pane pane;

	private static Framebuffer framebuffer;
	private static Framebuffer blurPing, blurPong;

	public static void init(MinecraftClient client, Screen screen, int width, int height)
	{
		pane = new Pane(0, 0, width, height);
		pane.setImageBackground(RenderUtilV2.getTextureFromLocation(START_ID));
		final VerticalPane buttonPane = new VerticalPane(0, 0, 120, height);
		buttonPane.setBackground(new ColourHolder(50, 50, 50, 190));

		ButtonComponent singlePlayerButton = new ButtonComponent(0, 0, 120, 32, "Singleplayer", () -> client.setScreen(new SelectWorldScreen(screen)));
		singlePlayerButton.setBackground(ColourHolder.decode("#FF2b2b"));
		ButtonComponent multiplayerButton = new ButtonComponent(0, 0, 120, 32, "Multiplayer", () -> client.setScreen(new SelectWorldScreen(screen)));
		multiplayerButton.setBackground(ColourHolder.decode("#2b7b2b"));


		pane.addComponent(buttonPane);
		buttonPane.addComponent(singlePlayerButton);
		buttonPane.addComponent(multiplayerButton);

		framebuffer = new Framebuffer();


	}


	private static float time = (float) glfwGetTime();
	private static float prev_time = 0;

	public static void render(MatrixStack matrixStack, float width, float height, int mouseX, int mouseY, float delta)
	{

		if (blurPing == null)
		{
			blurPong = new Framebuffer();
			blurPong.texture.setMipmap(true);
			blurPing = new Framebuffer();
			blurPing.texture.setMipmap(true);
		}

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


		RenderUtilV2.setShader(APIShaders.LOADING.getShader());
		RenderUtilV2.setShaderUniform("CenterPos", new Vec2f(32 + 50, 32 + 50));
		RenderUtilV2.setShaderUniform("Time", time);
		RenderUtilV2.setShaderUniform("P", new Vec2f(width, height));

		RenderUtilV2.quadTexture(matrixStack, 0, 0, width, height, ColourHolder.FULL);


		/*framebuffer.bind();
		final int test = RenderUtilV2.getTextureFromLocation(new Identifier(ZeroPointClient.MOD_ID, "textures/girl.png"));
		RenderUtilV2.rectangleTexture(matrixStack, 20, 20, 120, 120, test, ColourHolder.FULL);
		framebuffer.unbind();


		RenderUtilV2.setShader(APIShaders.OUT.getShader());
		RenderUtilV2.setTextureId(framebuffer.texture);
		final ColourHolder x = ColourHolder.decode("#0f111a");
		final ColourHolder x1 = ColourHolder.decode("#074621");
		final ColourHolder x2 = ColourHolder.decode("#0f111a");

		RenderUtilV2.setShaderUniform("u_model_size", new Vec2f(width, height));
		RenderUtilV2.setShaderUniform("u_threshold", 1.f);
		RenderUtilV2.setShaderUniform("u_width", 3f);
		RenderUtilV2.setShaderUniform("u_step_start", -1.0f);
		RenderUtilV2.setShaderUniform("u_step_end", 0.7f);
		RenderUtilV2.setShaderUniform("u_color", new Vector4f(x.red(), x.blue(), x.green(), 255));
		RenderUtilV2.setShaderUniform("u_far_color", new Vector4f(x1.red(), x1.blue(), x1.green(), 255));
		RenderUtilV2.setShaderUniform("u_low_color", new Vector4f(x2.red(), x2.blue(), x2.green(), 255));
		RenderUtilV2.setShaderUniform("u_low_color_fade", 0f);
		RenderUtilV2.setShaderUniform("u_mesh_pad", 0f);


		blurFbo.bind();
		RenderUtilV2.quadTexture(matrixStack, 0, 0, width, height, 0, 1, 1, 0, ColourHolder.FULL);
		blurFbo.unbind();

		blurFboP2.bind();
		final Shader shader = APIShaders.GAUSSIAN_BLUR_SHADER.getShader();
		RenderUtilV2.setShader(shader);
		RenderUtilV2.setTextureId(blurFbo.texture);
		RenderUtilV2.setShaderUniform("Radius", 6f);
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1f, 0f));
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);
		blurFboP2.unbind();




		RenderUtilV2.setShader(shader);
		RenderUtilV2.setTextureId(blurFboP2.texture);
		RenderUtilV2.setShaderUniform("Radius", 6f);
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(0f, 1f));
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);
		RenderUtilV2.rectangleTexture(matrixStack, 20, 20, 120, 120, test, ColourHolder.FULL);*/


	/*	RenderUtilV2.setTextureId(framebuffer.texture);
		//Set the after bounds of the shader again to change the direction.
		RenderUtilV2.setShaderUniform("Radius", 10f);
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1f, 0f));
		//Render the final product to the screen.
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);*/


		/*framebuffer.bind();
		final Shader shader = APIShaders.GAUSSIAN_BLUR_SHADER.getShader();
		RenderUtilV2.setShader(shader);
		RenderUtilV2.setTextureId(blurFbo.texture);
		RenderUtilV2.setShaderUniform("Radius", 10f);
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(0f, 1f));
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);
		framebuffer.unbind();
		//Blur shader is still bound so no need to change shaders.
		//Set the texture to the fbo texture.
		RenderUtilV2.setTextureId(framebuffer.texture);
		//Set the after bounds of the shader again to change the direction.
		RenderUtilV2.setShaderUniform("Radius", 10f);
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1f, 0f));
		//Render the final product to the screen.
		RenderUtilV2.postProcessRect(width, height, 0, 0, 1, 1);*/


		blurPong.clear();
		framebuffer.clear();


		float cur_time = (float) glfwGetTime();
		time += cur_time - prev_time;
		prev_time = cur_time;
//		pane.render(matrixStack, mouseX, mouseY, delta);
	}
}
