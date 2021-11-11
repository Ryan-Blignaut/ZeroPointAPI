package me.thesilverecho.zeropoint.impl.render;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Function;

public class ItemObjectFileLoader implements ModelVariantProvider, Function<ResourceManager, ModelVariantProvider>
{
	public static final ItemObjectFileLoader INSTANCE = new ItemObjectFileLoader();

	@Override
	public ModelVariantProvider apply(ResourceManager resourceManager)
	{
		return this;
	}

	@Override
	public @Nullable UnbakedModel loadModelVariant(ModelIdentifier modelId, ModelProviderContext context) throws ModelProviderException
	{
		if (modelId.getVariant().equals("inventory"))
		{
			Identifier modelPath = new Identifier(modelId.getNamespace(), "models/item/%s.json".formatted(modelId.getPath()));
			try (Reader reader = new BufferedReader(new InputStreamReader(MinecraftClient.getInstance().getResourceManager().getResource(modelPath).getInputStream())))
			{
				final JsonObject object = JsonHelper.deserialize(reader);
//				object.has("")

			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}


		return null;
	}
}
