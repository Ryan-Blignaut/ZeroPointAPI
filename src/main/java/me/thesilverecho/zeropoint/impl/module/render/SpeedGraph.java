package me.thesilverecho.zeropoint.impl.module.render;


import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.DrawMode;
import me.thesilverecho.zeropoint.api.render.Mesh;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.impl.module.render2.BlurBackground;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

@ClientModule(name = "Speed graph", keyBinding = GLFW.GLFW_KEY_M)
public class SpeedGraph extends BaseModule
{
	private final ArrayList<Float> speedList = new ArrayList<>();
	private float lastSpeed = -1;

	@ConfigSetting
	private final ColourHolder colour = ColourHolder.FULL;
	@ConfigSetting
	private final float smoothModifier = 0.5f, offsetX = 32, offsetY = 132, scale = 3;

	private Mesh mesh;

	@EventListener
	public void render(Render2dEvent.Pre event)
	{

		if (mesh == null) mesh = new Mesh(DrawMode.Lines, Mesh.Attrib.Vec3, Mesh.Attrib.Color);
		mesh.begin();
		float maxHeight = -1;

		for (int i = 0; i < speedList.size() - 1; i++)
		{
			mesh.line(
					mesh.vec3(offsetX + i, event.scaledHeight() - offsetY - speedList.get(i) * 10 * scale, 0).color(colour).next(),
					mesh.vec3(offsetX + i + 1, event.scaledHeight() - offsetY - speedList.get(i + 1) * 10 * scale, 0).color(colour).next()
			);
			if (maxHeight < speedList.get(i) + speedList.get(i))
				maxHeight = speedList.get(i + 1) + speedList.get(i) * scale;
		}

		mesh.end();
		FontRenderer.renderText(APIFonts.THIN.getFont(), event.matrixStack(), maxHeight + "", 32, 32);
		float finalMaxHeight = maxHeight;
//		BlurBackground.renderToBlur(() -> RenderUtilV2.rectangle(event.matrixStack(), offsetX, event.scaledHeight() - offsetY - finalMaxHeight, 300, finalMaxHeight, new ColourHolder(10, 10, 10, 90)));

		BlurBackground.renderToBlur(() ->
		{
			final Shader shader = APIShaders.RECTANGLE_SHADER.getShader();
			shader.bind();
			mesh.render();
			shader.unBind();
		});

	}

	@EventListener
	public void onTick(TickEvent.StartTickEvent event)
	{

		if (MC.player == null) return;
		float z2 = (float) MC.player.getZ();
		float z1 = (float) MC.player.prevZ;
		float x2 = (float) MC.player.getX();
		float x1 = (float) MC.player.prevX;
		float speed = (float) Math.sqrt((z2 - z1) * (z2 - z1) + (x2 - x1) * (x2 - x1));
		if (speed < 0) speed = -speed;
		speed = (lastSpeed * 0.9f + speed * 0.1f) * smoothModifier + speed * (1f - smoothModifier);
		lastSpeed = speed;
		speedList.add(speed);
		while (speedList.size() > 300) speedList.remove(0);

	}
}
