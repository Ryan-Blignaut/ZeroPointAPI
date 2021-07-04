/*
package me.thesilverecho.zeropoint.api.render.shader;


import github.thesivlerecho.zeropoint.ZeroPointClient;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.EnumMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ShaderManager
{

	private static final EnumMap<github.thesivlerecho.zeropoint.render.shader.ZeroPointShader, github.thesivlerecho.zeropoint.render.shader.Shader> SHADER_MAP = new EnumMap<>(github.thesivlerecho.zeropoint.render.shader.ZeroPointShader.class);


	public static github.thesivlerecho.zeropoint.render.shader.Shader getShader(github.thesivlerecho.zeropoint.render.shader.ZeroPointShader shader)
	{
		return SHADER_MAP.get(shader);
	}

	public static <T> T getShader(Class<T> clazz, github.thesivlerecho.zeropoint.render.shader.ZeroPointShader shader)
	{
		final github.thesivlerecho.zeropoint.render.shader.Shader o = SHADER_MAP.get(shader);
		if (clazz.isInstance(o))
			return clazz.cast(o);
		else
			return null;
	}

	public static void reload(ResourceManager manager)
	{
		SHADER_MAP.values().forEach(github.thesivlerecho.zeropoint.render.shader.Shader::destroy);
		SHADER_MAP.clear();
		loadShaders(manager);
	}

	public static void initShaders()
	{
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener()
		{


			private final ResourceReloader reloadListener = (SynchronousResourceReloader) ShaderManager::reload;

			@Override
			public Identifier getFabricId()
			{
				return new Identifier(ZeroPointClient.MOD_ID, "shader_loader");
			}

			@Override
			public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor)
			{
				return reloadListener.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
			}
		});
	}

	private static void loadShaders(ResourceManager manager)
	{

		for (github.thesivlerecho.zeropoint.render.shader.ZeroPointShader value : github.thesivlerecho.zeropoint.render.shader.ZeroPointShader.values())
		{
			SHADER_MAP.put(value, value.getSupplier().get().create(manager));
		}
	}
}
*/
