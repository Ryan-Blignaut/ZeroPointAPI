package me.thesilverecho.zeropoint.impl.module.render;


import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "speed graph", active = true, keyBinding = GLFW.GLFW_KEY_M)
public class SpeedGraph extends BaseModule
{
	static double speed = 0;

	@EventListener
	public void render(Render2dEvent.Pre event)
	{
//		APIFonts.REGULAR.getFont().setFontScale(0.4f).render(event.matrixStack(), Double.toString(speed), 30, 30);

//		APIFonts.REGULAR.getFont().setFontScale(0.4f).render(matrixStack, String.valueOf(MinecraftClient.getInstance().getTickDelta() % 10 == 0), 30, 30);

	}

	@EventListener
	public void onTick(TickEvent.StartTickEvent event)
	{
		final ClientPlayerEntity player = event.client().player;
		if (player == null) return;
		final double tickDelta = 1000.0 / MinecraftClient.getInstance().getLastFrameDuration();
		final double xDiff = player.getX() - player.prevX;
		final double zDiff = player.getZ() - player.prevZ;
		speed = Math.hypot(xDiff, zDiff) * tickDelta;
	}

}
