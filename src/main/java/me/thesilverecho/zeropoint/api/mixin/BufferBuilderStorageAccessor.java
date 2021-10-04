package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.render.layer.ModRenderLayer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.SortedMap;

@Mixin(BufferBuilderStorage.class)
public abstract class BufferBuilderStorageAccessor
{

	@Shadow
	@Final
	private SortedMap<RenderLayer, BufferBuilder> entityBuilders;

	@Inject(method = "<init>", at = @At("TAIL"))
	protected void add(CallbackInfo ci)
	{
		ModRenderLayer.ALL_LAYERS.forEach(modRenderLayer -> entityBuilders.put(modRenderLayer, new BufferBuilder(modRenderLayer.getExpectedBufferSize())));
	}

}
