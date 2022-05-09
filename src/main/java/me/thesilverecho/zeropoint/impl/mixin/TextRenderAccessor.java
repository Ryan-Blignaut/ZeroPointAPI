package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextRenderer.class)
public interface TextRenderAccessor
{
	@Accessor("fontHeight")
	@Mutable
	void setFontHeight(int fontHeight);

}
