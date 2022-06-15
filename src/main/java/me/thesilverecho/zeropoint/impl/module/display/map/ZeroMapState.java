package me.thesilverecho.zeropoint.impl.module.display.map;

import me.thesilverecho.zeropoint.api.render.texture.Texture2D;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class ZeroMapState
{

	public static final int size = 16;
	private static final int scale = 2;
	public final RegistryKey<World> dimension;

	private Texture2D image;

	private final int x, y;
	private int centerX;
	private int centerZ;

	public ZeroMapState(RegistryKey<World> dimension, int x, int y)
	{
		this.dimension = dimension;
		this.x = x;
		this.y = y;
	}


	public void updateMapData(WorldChunk chunk, ClientWorld worldIn)
	{
		int x = chunk.getPos().x * 16;
		int z = chunk.getPos().z * 16;


		if (!chunk.isEmpty())
		{
			if (image == null)
				image = new Texture2D(size, size, Texture2D.Format.RGBA);
			ByteBuffer byteBuffer = MemoryUtil.memAlloc(size * size * 4);

			for (int x1 = 0; x1 < 16 / scale; x1++)
			{
				for (int z1 = 0; z1 < 16 / scale; z1++)
				{
					byteBuffer.putInt(getMapColorAtPos(x + x1 * scale, z + z1 * scale, chunk, worldIn));
				}
			}
			byteBuffer.rewind();

//			SAVE THE IMAGE
//			image.uploadData(size, size, byteBuffer);

			image.bindTexture();
			GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, size, size, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
			STBImageWrite.stbi_write_png("map/" + chunk.toString() + ".png", size, size, 4, byteBuffer, size);

//			image.setChangedImage();
		}


	}

	private int getMapColorAtPos(int x, int z, WorldChunk chunk, ClientWorld worldIn)
	{
//		if (worldIn.getRegistryKey() != dimension)
//		{
//			return;
//		}

		int i = 1 << scale;
		int cX = this.centerX;
		int cY = this.centerZ;


		int maxH = 319;
		int minH = chunk.getBottomY();
		final BlockPos.Mutable blockPos = new BlockPos.Mutable(x, maxH, z);
		BlockState blockstate = chunk.getBlockState(blockPos);


		//Start at top of world and scan down.
		for (int h = maxH; h > minH - 1; h--)
		{
			final BlockPos.Mutable newPos = blockPos.setY(h);
			final BlockState state = chunk.getBlockState(newPos);
			if (!state.isAir())
			{
				blockstate = state;
				break;
			}
		}
		final MapColor mapColor1 = blockstate.getMapColor(worldIn, blockPos);
		int mapColor = APIColour.decode(mapColor1.color + "").getAsInt();
		if (blockstate.isAir())
		{
			mapColor = new APIColour(0, 255, 0, 255).getAsInt();
		}

//		ZeroPointApiLogger.error("Map colour: " + mapColor);
		return mapColor;
	}


}
