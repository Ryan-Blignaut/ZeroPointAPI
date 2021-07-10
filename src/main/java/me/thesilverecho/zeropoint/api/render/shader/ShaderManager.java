package me.thesilverecho.zeropoint.api.render.shader;


import me.thesilverecho.zeropoint.api.util.ManagerBase;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class ShaderManager extends ManagerBase<Shader>
{
	public static final ShaderManager INSTANCE = new ShaderManager();

	public ShaderManager()
	{
		super("Shader manager");
	}

	public static void reload(List<Shader> shaders, ResourceManager manager)
	{
		INSTANCE.managerArray.forEach(Shader::destroy);
		INSTANCE.managerArray.clear();
		INSTANCE.classCacheMap.clear();
		loadShaders(shaders, manager);
	}

	public static void initShaders(Shader... userShaders)
	{
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener()
		{
			private final ResourceReloader reloadListener = (SynchronousResourceReloader) manager ->
			{
				ArrayList<Shader> shaders = Arrays.stream(APIShaders.values()).map(APIShaders::getShader).collect(Collectors.toCollection(ArrayList::new));
				shaders.addAll(Arrays.asList(userShaders));
				ShaderManager.reload(shaders, manager);
			};

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

	private static void loadShaders(List<Shader> shaders, ResourceManager manager)
	{
		shaders.forEach(shader -> shader.create(manager));
	}
}
