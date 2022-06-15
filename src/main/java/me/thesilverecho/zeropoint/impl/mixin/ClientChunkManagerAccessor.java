package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.impl.module.display.ICChunkGetter;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;

import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(ClientChunkManager.class)
public abstract class ClientChunkManagerAccessor implements ICChunkGetter
{
	public AtomicReferenceArray<WorldChunk> getChunks()
	{
		try
		{
			return ((ClientChunkManagerClientChunkMapAccessor) ClientChunkManager.class.getDeclaredField("chunks").get(this)).getChunks();
		} catch (IllegalAccessException | NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		return null;

	}
}
