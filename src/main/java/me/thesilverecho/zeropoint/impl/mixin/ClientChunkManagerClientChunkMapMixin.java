package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(targets = {"net.minecraft.client.world.ClientChunkManager$ClientChunkMap"})
public class ClientChunkManagerClientChunkMapMixin
{


	@Shadow @Final private AtomicReferenceArray<WorldChunk> chunks;

	//	@Shadow private volatile ClientChunkManager.ClientChunkMap chunks;


}
