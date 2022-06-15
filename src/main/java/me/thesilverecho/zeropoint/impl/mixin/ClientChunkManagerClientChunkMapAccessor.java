package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(targets = {"net.minecraft.client.world.ClientChunkManager$ClientChunkMap"})
public interface ClientChunkManagerClientChunkMapAccessor
{
	@Accessor("chunks")
	AtomicReferenceArray<WorldChunk> getChunks();
}
