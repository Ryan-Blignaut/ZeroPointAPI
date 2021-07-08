package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FontManager
{
	public static void initFonts(CustomFont... userFonts)
	{
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener()
		{
			private final ResourceReloader reloadListener = (SynchronousResourceReloader) manager ->
			{
				//load the user fonts
				for (CustomFont userFont : userFonts)
					userFont.init(manager);
				//load API fonts
				for (APIFonts value : APIFonts.values())
					value.getFont().init(manager);
			};

			@Override
			public Identifier getFabricId()
			{
				return new Identifier(ZeroPointClient.MOD_ID, "font_loader");
			}

			@Override
			public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor)
			{
				return reloadListener.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
			}
		});

	}

}
