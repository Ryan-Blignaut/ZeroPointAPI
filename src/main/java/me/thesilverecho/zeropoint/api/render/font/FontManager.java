package me.thesilverecho.zeropoint.api.render.font;

public class FontManager
{
	public static void initFonts(CustomFont... userFonts)
	{

	/*	DefaultResourcePack defaultResourcePack = MinecraftClient.getInstance().getResourcePackProvider().getPack();
		for (CustomFont userFont : userFonts)
		{
			InputStream inputStream = null;
			final Identifier identifier = userFont.getIdentifier();
			try
			{
				inputStream = defaultResourcePack.open(ResourceType.CLIENT_RESOURCES, identifier);
			} catch (IOException e)
			{
				ZeroPointApiLogger.error("Error finding font to load" + identifier, e);
			}
			userFont.initV2(inputStream);
		}*/

//		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener()
//		{
//			private final ResourceReloader reloadListener = (SynchronousResourceReloader) manager ->
//			{
//				//load the user fonts
//				for (CustomFont userFont : userFonts)
//					userFont.init(manager);
//				//load API fonts
//				for (APIFonts value : APIFonts.values())
//					value.getFont().init(manager);
//			};
//
//			@Override
//			public Identifier getFabricId()
//			{
//				return new Identifier(ZeroPointClient.MOD_ID, "font_loader");
//			}
//
//			@Override
//			public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor)
//			{
//				return reloadListener.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
//			}
//		});

	}

}
