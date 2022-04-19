package me.thesilverecho.zeropoint.impl.module.render;


import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

@ClientModule(name = "Dir hud", active = false)
public class DirectionHud extends BaseModule
{
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


}
