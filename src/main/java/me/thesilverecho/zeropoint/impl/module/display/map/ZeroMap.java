package me.thesilverecho.zeropoint.impl.module.display.map;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.texture.Texture2D;
import me.thesilverecho.zeropoint.impl.module.display.ICChunkGetter;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;


@ClientModule(name = "Zero map", description = "Displays a mini map", keyBinding = GLFW.GLFW_KEY_LEFT)
public class ZeroMap extends BaseModule
{
	private boolean run = false;
	private ArrayList<Texture2D> chunks = new ArrayList<>();


	HashMap<Chunk, ZeroMapState> map = new HashMap<>();


	@EventListener

	public void render(Render2dEvent.Pre event)
	{

		if (MC.world != null)
		{
		/*	if (!run)
			{
				run = true;
				updateMap();
			}*/
			updateMap();
		}


	}


	Texture2D test;

	private void updateMap()
	{
		final AtomicReferenceArray<WorldChunk> chunks1 = ((ICChunkGetter) MC.world.getChunkManager()).getChunks();
		for (int chunkIndex = 0; chunkIndex < chunks1.length(); chunkIndex++)
		{
			final WorldChunk worldChunk = chunks1.get(chunkIndex);
		/*	if (worldChunk == null)
				ZeroPointApiLogger.error("Map chunk is empty");
			else
				ZeroPointApiLogger.error("Map chunk is good to go");*/
			if (worldChunk != null)
				map.computeIfAbsent(worldChunk, chunk ->
				{
					final ZeroMapState zeroMapState = new ZeroMapState(MC.world.getRegistryKey(), worldChunk.getPos().x, worldChunk.getPos().z);
					zeroMapState.updateMapData(worldChunk, MC.world);
					return zeroMapState;
				});

//			ZeroPointApiLogger.error(map.size());

		}
	}
}
