package me.thesilverecho.zeropoint.impl.module.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.Framebuffer;
import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;

@ClientModule(name = "Radar", keyBinding = GLFW.GLFW_KEY_M, active = true)
public class Radar extends BaseModule
{
	private float direction;

	private float offsetX = 0, offsetY = 0, size = 90;

	private final ArrayList<PositionIndicator> positionIndicators = new ArrayList<>();
	private Framebuffer fbo1;

	@EventListener
	public void renderEvent(Render2dEvent.Pre e)
	{
		offsetX = 90;
		offsetY = 90;
		final MatrixStack matrixStack = e.matrixStack();
		RenderUtilV2.rectangle(matrixStack, offsetX, offsetY, size, size, 0, ColourHolder.decode("#6b2b6b").setAlpha(100));
//		RenderUtilV2.circleTexture(matrixStack, offsetX, offsetY, size, size,  offsetX/(1920*2), offsetY/(1080*2), (offsetX + size) / (1920*2),  (offsetY +size)/(1080*2), size / 2, fbo1.texture, ColourHolder.FULL /*ColourHolder.decode("#2b2b2b").setAlpha(255 * 80 / 100)*/);
		RenderUtilV2.circleTexture(matrixStack, offsetX, offsetY, size, size, size / 2, fbo1.texture, ColourHolder.decode("#2b2b2b").setAlpha(255 * 80 / 100));

//		RenderUtilV2.rectangle(matrixStack, offsetX, offsetY, size, 4, ColourHolder.decode("#002FFF"));

		final CustomFont font = APIFonts.FREE_SANS.getFont();
		final float fontSize = 0.25f;
//		RenderUtilV2.rectangle(matrixStack, offsetX - 2,offsetY,4,size,ColourHolder.decode("#0000FF"));

//		RenderUtilV2.rectangle(matrixStack, offsetX - 2,offsetY,4,size,ColourHolder.decode("#0000FF"));


//		TODO:fix glyph positions
		renderLetterPos(matrixStack, font, fontSize, 90, "N", 1, -1);
		renderLetterPos(matrixStack, font, fontSize, 180, "E", 1, -1);
		renderLetterPos(matrixStack, font, fontSize, 270, "S", 1, 1);
		renderLetterPos(matrixStack, font, fontSize, 360, "W", 1, -1);

		final float midX = offsetX + size / 2;
		final float midY = offsetY + size / 2;


		final int size = 5;
		RenderUtilV2.circle(matrixStack, midX - size / 2f, midY - size / 2f, size, size, size / 2f, ColourHolder.decode("#FFFF"));
		for (int j = 1; j < 10; j++)
		{
			for (int i = 0; i < 4; i++)
			{
				float x = (float) Math.cos(Math.toRadians(direction + i * 90)) * 5 * j + midX;
				float y = (float) Math.sin(Math.toRadians(direction + i * 90)) * 5 * j + midY;
				RenderUtilV2.circle(matrixStack, x - size / 2f, y - size / 2f, size, size, size / 2f, ColourHolder.decode("#5fFF2dFF"));
			}
		}

		positionIndicators.forEach(p ->
		{
			RenderUtilV2.circle(matrixStack, p.x - size / 2f, p.y - size / 2f, size, size, size / 2f, p.viable ? ColourHolder.decode("#FF2d2d") : ColourHolder.decode("#3FFFFFFF"));
		});


	/*	MinecraftClient.getInstance().world.getEntities().forEach(entity ->
		{
			final ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (entity instanceof final PlayerEntity playerEntity)
			{
				if (playerEntity.isAlive() && !playerEntity.isInvisible() && !playerEntity.isInvisibleTo(player))
				{

				}

			} else
			{
				float posX = (float) ((entity.getPos().x - player.prevX) * 1);//(float) (((entity.getPos().x + (entity.getPos().x - entity.prevX) * MinecraftClient.getInstance().getTickDelta()) - player.prevX) * size);
				float posZ = (float) ((entity.getPos().z - player.prevZ) * 1);//(float) (((entity.getPos().z + (entity.getPos().z - entity.prevZ) * MinecraftClient.getInstance().getTickDelta()) - player.prevZ) * size);

				float cos = (float) Math.cos(player.headYaw * (Math.PI * 2 / 360));
				float sin = (float) Math.sin(player.headYaw * (Math.PI * 2 / 360));
				float rotY = -(posZ * cos - posX * sin);
				float rotX = -(posX * cos + posZ * sin);

				float angle = findAngle(offsetX,  offsetX+rotX, offsetY,offsetY+  rotY);
				float x = (float) ((size / 2) * Math.cos(Math.toRadians(angle))) + offsetX + size / 2;
				float y = (float) ((size / 2) * Math.sin(Math.toRadians(angle))) + offsetY + size / 2;
				RenderUtilV2.circle(matrixStack, x, y, 10, 10, 5, ColourHolder.decode("#FF2d2d"));
				*//*		matrixStack.push();
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45));
					matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(45));
					RenderUtilV2.rectangle(matrixStack, 0, 0, 10, 10, ColourHolder.decode("#2b2b2b"));
					matrixStack.pop();*//*


			}
		});*/


	}

	private void renderLetterPos(MatrixStack matrixStack, CustomFont font, float fontSize, int angle, String letter, int xMod, int yMod)
	{
		float angle2 = -direction + angle;
		float x2 = (float) ((size / 2) * Math.cos(Math.toRadians(angle2))) + offsetX + size / 2;
		float y2 = (float) ((size / 2) * Math.sin(Math.toRadians(angle2))) + offsetY + size / 2;
		FontRenderer.renderText(font, fontSize, matrixStack, letter, x2 - xMod * (FontRenderer.getWidth(font, fontSize, letter) / 2), y2 + yMod * FontRenderer.getHeight(font, fontSize) / 2);
	}

	private float findAngle(float x, float x2, float y, float y2)
	{
		return (float) (Math.atan2(y2 - y, x2 - x) * 180 / Math.PI);
	}

	@EventListener
	public void updateEvent(TickEvent.EndTickEvent e)
	{
		final MinecraftClient client = e.minecraftClient();
		final ClientPlayerEntity player = client.player;
		if (player != null)
		{
			this.direction = player.headYaw;//MathHelper.floor(player.headYaw * 256.0f / 360.0f + 0.5) & 0xFF;
			//Clear the positions
			positionIndicators.clear();
			if (client.world != null)
			{
				client.world.getEntities().forEach(entity ->
				{
					if (entity == player)
						return;

					final float posX = (float) ((entity.getPos().x - player.prevX) * 1);
					final float posZ = (float) ((entity.getPos().z - player.prevZ) * 1);
					final float cos = (float) Math.cos(player.headYaw * (Math.PI * 2 / 360));
					final float sin = (float) Math.sin(player.headYaw * (Math.PI * 2 / 360));
					final float rotY = -(posZ * cos - posX * sin);
					final float rotX = -(posX * cos + posZ * sin);
					final float angle = findAngle(offsetX, offsetX + rotX, offsetY, offsetY + rotY);
					float x = (float) ((size / 2) * Math.cos(Math.toRadians(angle))) + offsetX + size / 2;
					float y = (float) ((size / 2) * Math.sin(Math.toRadians(angle))) + offsetY + size / 2;
					positionIndicators.add(new PositionIndicator(x, y, !entity.isInvisibleTo(player)));
				});
			}
		}


	}

	private int colorAttachment;

	@EventListener
	public void renderEvent(RenderWorldEvent.Post event)
	{
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		this.colorAttachment = framebuffer.getColorAttachment();
		if (fbo1 == null) fbo1 = new Framebuffer();

		final int width = framebuffer.textureWidth;
		final int height = framebuffer.textureHeight;


		final Shader shader = APIShaders.BLUR_RECTANGLE_SHADER.getShader();
		fbo1.bind();
		shader.bind(true);
		Matrix4f matrix4f = Matrix4f.projectionMatrix((float) width, (float) (-height), 1000.0F, 3000.0F);
		RenderSystem.setProjectionMatrix(matrix4f);
		shader.setArgument("InSize", new Vec2f(width, height));
		shader.setArgument("OutSize", new Vec2f(width, height));
		GlStateManager._activeTexture(GL_TEXTURE0);
		GlStateManager._bindTexture(colorAttachment);
		shader.setArgument("Sampler0", 0);

		shader.setArgument("Radius", 120f);
		shader.setArgument("BlurDir", new Vec2f(1f, 0f));

		float f = (float) width;
		float g = (float) height;
		float h = 1.0f;//(float)this.viewportWidth / (float)this.textureWidth;
		float i = 1.0f;//(float)this.viewportHeight / (float)this.textureHeight;
		BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		bufferBuilder.vertex(0.0D, g, 0.0D).texture(0.0F, 0.0F).color(255, 255, 255, 255).next();
		bufferBuilder.vertex(f, g, 0.0D).texture(h, 0.0F).color(255, 255, 255, 255).next();
		bufferBuilder.vertex(f, 0.0D, 0.0D).texture(h, i).color(255, 255, 255, 255).next();
		bufferBuilder.vertex(0.0D, 0.0D, 0.0D).texture(0.0F, i).color(255, 255, 255, 255).next();
		bufferBuilder.end();
		BufferRenderer.postDraw(bufferBuilder);
		shader.unBind();
		fbo1.unbind();
		GLWrapper.disableGL2D();

	}


	private static class PositionIndicator
	{
		private final float x, y;
		private final boolean viable;

		public PositionIndicator(float x, float y, boolean viable)
		{
			this.x = x;
			this.y = y;

			this.viable = viable;
		}
	}
}
