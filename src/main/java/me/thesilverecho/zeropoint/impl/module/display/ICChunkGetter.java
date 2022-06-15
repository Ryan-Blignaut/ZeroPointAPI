package me.thesilverecho.zeropoint.impl.module.display;

import net.minecraft.world.chunk.WorldChunk;

import java.util.concurrent.atomic.AtomicReferenceArray;

public interface ICChunkGetter
{
	public AtomicReferenceArray<WorldChunk> getChunks();
}
