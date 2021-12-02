package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.BlockOutlineEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
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
					minX /*+ event.x()*/ - 0.005,
					minY /*+ event.y()*/ - 0.005,
					minZ /*+ event.z()*/ - 0.005,
					maxX /*+ event.x()*/ + 0.005,
					maxY /*+ event.y()*/ + 0.005,
					maxZ /*+ event.z()*/ + 0.005));
		}
	}


	public static void renderBoundingBox(MatrixStack matrixStack, BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2)
	{


		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
		drawBox1(matrix4f, buffer, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, new ColourHolder(200, 0, 0, 100), new ColourHolder(0, 110, 0, 100));
		buffer.end();
		BufferRenderer.draw(buffer);
	}

	public static void drawBox1(Matrix4f matrix4f, BufferBuilder buffer, float x1, float y1, float z1, float x2, float y2, float z2, ColourHolder colour, ColourHolder colour1)
	{

		buffer.vertex(matrix4f, x1, y1, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y1, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y1, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y1, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y2, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y2, z2).color(colour1.red(), colour1.green(), colour1.blue(), colour1.alpha()).next();

		buffer.vertex(matrix4f, x1, y2, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y1, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y2, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y1, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y1, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y1, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();

		buffer.vertex(matrix4f, x2, y2, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y2, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y2, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y1, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y2, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y1, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();

		buffer.vertex(matrix4f, x1, y1, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y1, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y1, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y1, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y1, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y2, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();

		buffer.vertex(matrix4f, x1, y2, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x1, y2, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y2, z1).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y2, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y2, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
		buffer.vertex(matrix4f, x2, y2, z2).color(colour.red(), colour.green(), colour.blue(), colour.alpha()).next();
	}


}
