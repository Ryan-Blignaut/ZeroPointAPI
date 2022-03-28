package me.thesilverecho.zeropoint.impl.module.render3;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderTileEntityEvent;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.DrawMode;
import me.thesilverecho.zeropoint.api.render.Mesh;
import me.thesilverecho.zeropoint.api.render.MeshVertexConsumerProvider;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ConcurrentMap;

@ClientModule(name = "Test render shader", active = true, keyBinding = GLFW.GLFW_KEY_RIGHT_ALT)
public class Test extends BaseModule
{
	public static final ConcurrentMap<BlockPos, BlockEntity> map = Maps.newConcurrentMap();
	private Mesh mesh;
	private Framebuffer framebuffer;
	private MeshVertexConsumerProvider provider;

	@EventListener
	public void renderTileStart(RenderTileEntityEvent.startRenderTile event)
	{
		if (framebuffer == null)
		{
			framebuffer = new Framebuffer();
			mesh = new Mesh(DrawMode.Triangles, Mesh.Attrib.Vec3, Mesh.Attrib.Color);
			provider = new MeshVertexConsumerProvider(mesh);
		}
		mesh.begin();
	}

	@EventListener
	public void renderTileEnd(RenderTileEntityEvent.endRenderTile event)
	{
		mesh.end();

		final Shader shader = APIShaders.RECTANGLE_SHADER.getShader();
//		framebuffer.bind();
		/*ScoreBoardHud.blurFBO.bind();
		RenderSystem.depthFunc(GL11.GL_ALWAYS);
		shader.bind();
		mesh.render();
		shader.unBind();
		RenderSystem.depthFunc(GL11.GL_LESS);
		ScoreBoardHud.blurFBO.unbind();*/
		shader.bind();
		mesh.render();
		shader.unBind();
//		framebuffer.unbind();

	}

	@EventListener
	public void renderTile(RenderTileEntityEvent.renderTile event)
	{
		event.ci().cancel();
		final BlockEntity be = event.blockEntity();
		final MatrixStack ms = event.matrices();
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		event.renderer().render(be, event.tickDelta(), ms, provider, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
	}


	@EventListener
	public void renderTest(/*Render2dEvent.World*/RenderWorldEvent.Post event)
	{
	/*	if (this.framebuffer == null) return;
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
		RenderUtilV2.setShaderUniform("TextureSize", new Vec2f(framebuffer.textureWidth, framebuffer.textureHeight));
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", 10f);
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);
		this.framebuffer.clear();*/

	}

/*	@EventListener
	public void renderTile(RenderTileEntityEvent.renderTile event)
	{
		final BlockEntity be = event.blockEntity();
		final MatrixStack ms = event.matrices();
		if (framebuffer == null)
		{
			framebuffer = new Framebuffer();
		}
		RenderSystem.teardownOverlayColor();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
//		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		final BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		ms.push();
		renderTileEntity(be, ms);
		ms.pop();
		bufferBuilder.end();
		RenderSystem.disableDepthTest();
		framebuffer.bind();

		BufferRenderer.draw(bufferBuilder);


		event.renderer().render(be, event.tickDelta(), ms, event.vertexConsumers(), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
		framebuffer.unbind();
		*//*final Shader shader = APIShaders.RECTANGLE_SHADER.getShader();
		shader.setShaderUniform("Radius", new Vec2f(0, 0));
		shader.setShaderUniform("Rectangle", new Vector4f(0, 0, 0, 0));

		shader.bind();
		BufferRenderer.postDraw(bufferBuilder);
		shader.unBind();*//*


	}*/


	/*@EventListener
	public void renderTest(Render2dEvent.World event)
	{
		if (this.framebuffer == null) return;

		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
		RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
		RenderUtilV2.setShaderUniform("TextureSize", new Vec2f(framebuffer.textureWidth, framebuffer.textureHeight));
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", 10f);
		RenderUtilV2.setTextureId(this.framebuffer.texture.getID());
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);
		this.framebuffer.clear();

	}

	private void renderTileEntity(BlockEntity blockEntity, MatrixStack ms)
	{
		BlockState blockState = blockEntity.getCachedState();
		for (int i = 0; i < Direction.values().length; i++)
		{
//			MinecraftClient.getInstance().getBlockRenderManager().getModel(blockState).getQuads(blockState, Direction.values()[i], new Random());
//			List<BakedQuad> list = model.getQuads(state, DIRECTIONS[i], RANDOM);
//			if (!list.isEmpty()) renderQuads(list, offsetX, offsetY, offsetZ, consumer);
		}


//		framebuffer.bind();

		blockState.getOutlineShape(MinecraftClient.getInstance().world, blockEntity.getPos()).getBoundingBoxes().forEach(bb ->
		{
			Matrix4f matrix4f = ms.peek().getPositionMatrix();
			Color color1 = Color.CYAN;


			Box box = bb.expand(0.002);
			float minX = (float) box.minX;
			float minY = (float) box.minY;
			float minZ = (float) box.minZ;
			float maxX = (float) box.maxX;
			float maxY = (float) box.maxY;
			float maxZ = (float) box.maxZ;
//			BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
			final BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().getBuffer();

			bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

			bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

			bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

			bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

			bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

			bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		});
//		framebuffer.unbind();
	}

	public Vec3d getRenderPosition(double x, double y, double z)
	{
		double minX = x - MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().x;
		double minY = y - MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().y;
		double minZ = z - MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().z;
		return new Vec3d(minX, minY, minZ);
	}
*/

	/*
	@EventListener
	public void renderTest(RenderWorldEvent.RenderV3 event)
	{


					*/
/*if (framebuffer == null)
					{
						return;
			//			framebuffer = new Framebuffer();
					}
					RenderSystem.depthFunc(519);
					framebuffer.clear();
					framebuffer.bind();
					RenderSystem.teardownOverlayColor();
					RenderSystem.setShader(GameRenderer::getPositionColorShader);
					RenderSystem.setShaderColor(1, 1, 1, 1);

					BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
					bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
					map.values().forEach(blockEntity -> renderTileEntity(blockEntity, event.matrix()));
					bufferBuilder.end();
					BufferRenderer.draw(bufferBuilder);

					RenderSystem.disableBlend();
					RenderSystem.disableDepthTest();
					RenderSystem.enableTexture();
					RenderSystem.resetTextureMatrix();
					RenderSystem.depthMask(false);
				*//*
 */
/*	final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
					RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
					RenderUtilV2.setShaderUniform("TextureSize", new Vec2f(framebuffer.textureWidth, framebuffer.textureHeight));
					RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
					RenderUtilV2.setShaderUniform("Radius", 10f);
					final int colorAttachment = framebuffer.getColorAttachment();
					RenderUtilV2.setTextureId(colorAttachment);
					RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);*//*
 */
/*
			//		ShaderHelper.INSTANCE.storageShader.render(Wrapper.INSTANCE.getMinecraft().getTickDelta());

					RenderSystem.enableTexture();
					RenderSystem.depthMask(true);
					this.framebuffer.unbind();
			//		Wrapper.INSTANCE.getMinecraft().getFramebuffer().beginWrite(true);*//*

	}
*/

}
