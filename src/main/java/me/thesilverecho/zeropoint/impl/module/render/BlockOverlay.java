package me.thesilverecho.zeropoint.impl.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.BlockOutlineEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV3;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import me.thesilverecho.zeropoint.impl.module.render2.BlurBackground;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientModule(name = "Block overlay", active = true, keyBinding = GLFW.GLFW_KEY_N)
public class BlockOverlay extends BaseModule
{

	@EventListener
	public void renderBlockOutline(BlockOutlineEvent event)
	{
		final CallbackInfo callbackInfo = event.ci();
//		if (renderOutline)
//			callbackInfo.cancel();
//		else if (renderCustom)
		{
			callbackInfo.cancel();
			event.voxelShape().forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> renderBoundingBox(event.matrixStack(),
					Tessellator.getInstance().getBuffer(),
					minX + event.x() - 0.005,
					minY + event.y() - 0.005,
					minZ + event.z() - 0.005,
					maxX + event.x() + 0.005,
					maxY + event.y() + 0.005,
					maxZ + event.z() + 0.005));
		}
	}

	private Framebuffer framebuffer;

	@EventListener
	public void renderBlockOutline(Render2dEvent.Pre event)
	{
	/*	if (framebuffer == null)
		{
			framebuffer = new Framebuffer();
		}
		final Window window = MinecraftClient.getInstance().getWindow();
		RenderUtilV2.setShader(APIShaders.BLUR_RECTANGLE_SHADER.getShader());
		RenderUtilV2.setTextureId(framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(window.getWidth(), window.getHeight(), 0, 0, 1, 1);

		framebuffer.clear();*/
	}

	public void renderBoundingBox(MatrixStack matrixStack, BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2)
	{
		drawBox1(matrixStack, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2);
	}

	private MatrixStack mat;

	public void drawBox1(MatrixStack matrixStack, float x1, float y1, float z1, float x2, float y2, float z2)
	{

		if (framebuffer == null)
		{
			framebuffer = new Framebuffer();
		}
		mat = matrixStack;
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();


		RenderUtilV3.setQuadColourHolder(ColourHolder.FULL/*("#2b2b2b")*/);
	/*	framebuffer.bind();
		final net.minecraft.client.gl.Framebuffer framebuffer1 = MinecraftClient.getInstance().getFramebuffer();
		final int textureWidth = framebuffer1.textureWidth;
		final int textureHeight = framebuffer1.textureHeight;
		RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
		RenderUtilV2.setShaderUniform("TextureSize", new Vec2f(textureWidth, textureHeight));
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", 30f);
		final int colorAttachment = framebuffer1.getColorAttachment();
		RenderUtilV2.setTextureId(colorAttachment);
		RenderUtilV2.postProcessRect(framebuffer1.viewportWidth, framebuffer1.viewportHeight, 0, 0, 1, 1);
		framebuffer.unbind();
*/
		final Shader shader = APIShaders.BOKEH_TEXTURE_SHADER.getShader();


		RenderUtilV3.setTextureId(RenderUtilV3.getTextureFromLocation(new Identifier(ZeroPointClient.MOD_ID, "/textures/bg.png")));
		RenderUtilV3.setShader(shader);
		shader.setShaderUniform("Size", 1f);

		//TODO: find out why framebuffer cant be drawn to when depth test is enabled.(It seems that the glDepth function, GL_LESS is not passing)
		RenderSystem.disableDepthTest();
		BlurBackground.renderToBlur(() ->
		{
			RenderUtilV3.quadTextureVertical(matrix4f, x1, y2, z1, x1, y1, z2);
			RenderUtilV3.quadTextureVertical(matrix4f, x2, y2, z2, x2, y1, z1);
			RenderUtilV3.quadTextureVertical(matrix4f, x2, y2, z1, x1, y1, z1);
			RenderUtilV3.quadTextureVertical(matrix4f, x1, y2, z2, x2, y1, z2);
			RenderUtilV3.quadTextureHorizontal(matrix4f, x2, y1, z1, x1, y1, z2);
			RenderUtilV3.quadTextureHorizontal(matrix4f, x2, y2, z2, x1, y2, z1);
		});
		RenderSystem.enableDepthTest();

//		RenderSystem.enableDepthTest();
//		RenderUtilV3.quadTextureVertical(matrix4f, x1, y2, z1, x1, y1, z2);
//		RenderUtilV3.quadTextureVertical(matrix4f, x2, y2, z2, x2, y1, z1);
//		RenderUtilV3.quadTextureVertical(matrix4f, x2, y2, z1, x1, y1, z1);
//		RenderUtilV3.quadTextureVertical(matrix4f, x1, y2, z2, x2, y1, z2);
//		RenderUtilV3.quadTextureHorizontal(matrix4f, x2, y1, z1, x1, y1, z2);
//		RenderUtilV3.quadTextureHorizontal(matrix4f, x2, y2, z2, x1, y2, z1);
//		RenderSystem.disableDepthTest();
	}


}
