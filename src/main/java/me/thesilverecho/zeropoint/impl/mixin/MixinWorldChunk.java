package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldChunk.class)
public class MixinWorldChunk
{

	/*@Inject(method = "addBlockEntity", at = @At("HEAD"))
	public void addBlockEntity(BlockEntity blockEntity, CallbackInfo ci)
	{
		BlockPos blockPos = blockEntity.getPos();
		if (Test.map.containsKey(blockPos))
		{
			Test.map.replace(blockPos, blockEntity);
		} else
		{
			Test.map.put(blockPos, blockEntity);
		}
	}

	@Inject(method = "removeBlockEntity(Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
	public void removeBlockEntity(BlockPos blockPos, CallbackInfo ci)
	{
		Test.map.remove(blockPos);
	}*/
}
