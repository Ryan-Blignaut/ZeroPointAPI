package me.thesilverecho.zeropoint.impl.render;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ObjFileLoader implements ModelResourceProvider, Function<ResourceManager, ModelResourceProvider>
{
	public static final ObjFileLoader INSTANCE = new ObjFileLoader();

	@Override
	public ModelResourceProvider apply(ResourceManager resourceManager)
	{
		return this;
	}

	@Override
	public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) throws ModelProviderException
	{
		return null;
	}
}
