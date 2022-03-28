package me.thesilverecho.zeropoint.impl.module.render;


import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import me.thesilverecho.zeropoint.impl.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ClientModule(name = "Dir hud", active = true)
public class DirectionHud extends BaseModule
{
	private float animationX = 0;

	@ConfigSetting
	private final String hudCol = "#323232";
	@ConfigSetting
	private final String fpsCol = "rainbow";

	@EventListener
	public void render(Render2dEvent.Pre event)
	{
		final ClientPlayerEntity player = MinecraftClient.getInstance().player;
		final double pitch = Math.toRadians(MathHelper.clamp(player.getPitch() + 30, -90, 90));
		final double yaw = Math.toRadians(MathHelper.wrapDegrees(player.getYaw()));

		for (Direction direction : Direction.values())
		{
			if (direction == (Direction.UP) || direction == (Direction.DOWN))
				continue;

			String axis = direction.name();

			final CustomFont font = APIFonts.REGULAR.getFont();
			FontRenderer.renderText(font, 0.5f, event.matrixStack(), axis,
					(float) (120 + getX(direction, yaw)) - (FontRenderer.getWidth(font, 0.5f, axis) / 2),
					(float) ((120 + getY(direction, yaw, pitch)) - (FontRenderer.getHeight(font, 0.5f) / 2)));
		}

	}

	private double getX(Direction direction, double yaw)
	{
		return Math.sin(getPos(direction, yaw)) * 1 * 40;
	}

	private double getY(Direction direction, double yaw, double pitch)
	{
		return Math.cos(getPos(direction, yaw)) * Math.sin(pitch) * 1 * 40;
	}

	private double getPos(Direction direction, double yaw)
	{
		return yaw + direction.ordinal() * Math.PI / 2;
	}

	private String ping = "", fps = "", pos = "", date = "", time = "";

	@EventListener
	public void onTick(TickEvent.StartTickEvent event)
	{
		final MinecraftClient client = event.client();
		final ClientPlayerEntity player = client.player;
		if (player != null)
		{
			if (client.getNetworkHandler() != null)
			{
				final PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
				ping = "Ping: ${#388E3C} " + (playerListEntry != null ? Integer.toString(playerListEntry.getLatency()) : "0");
			}
			fps = "FPS: ${" + fpsCol + "}" + ((MinecraftClientAccessor) client).getCurrentFps();
			final Vec3d playerPos = player.getPos();
			pos = "X: " + (int) playerPos.getX() + " Y: " + (int) playerPos.getY() + " Z: " + (int) playerPos.getZ();
			final LocalDateTime now = LocalDateTime.now();
			time = DateTimeFormatter.ofPattern("HH:mm").format(now);
			date = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(now);
		}
	}

	Framebuffer barFBO;


}
