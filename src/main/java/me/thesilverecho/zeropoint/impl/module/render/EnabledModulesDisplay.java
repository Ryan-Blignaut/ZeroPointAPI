package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.Framebuffer;
import me.thesilverecho.zeropoint.api.render.RenderUtil;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import me.thesilverecho.zeropoint.api.event.events.Render2dEvent;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@ClientModule(name = "Enabled modules", active = true, keyBinding = GLFW.GLFW_KEY_R)
public class EnabledModulesDisplay extends BaseModule
{
	private float scale = 0.5f;

	public EnabledModulesDisplay(@Nullable Boolean active, @Nullable Integer key)
	{
		super(active, key);
	}

	Framebuffer framebuffer;

	@EventListener
	public void render(Render2dEvent event)
	{
		if (framebuffer == null) framebuffer = new Framebuffer();
		final AtomicInteger yPos = new AtomicInteger(0);
		framebuffer.bind();
		BaseModule.ENABLE_MODULES.values().stream().filter(BaseModule::shouldDraw).sorted(Comparator.comparing(BaseModule::getName)).forEach(baseModule ->
		{
			final String text = baseModule.getName();
			final CustomFont font = APIFonts.REGULAR.getFont();
			font.setFontScale(scale);
			final float width = font.getWidth(text);
			final MatrixStack matrixStack = event.matrixStack();
			final float x = event.scaledWidth() - width;
			final float y = yPos.getAndAdd((int) (font.getHeight()));
			font.render(matrixStack, "1", 1, 1);
			RenderUtil.rect(matrixStack, x, y, event.scaledWidth(), yPos.get(), new ColourHolder(50, 50, 50, 180));
			font.render(matrixStack, "${#7EFC20}" + text, x, y);
		});
		framebuffer.unbind();
//		RenderUtil.setShader(APIShaders.MASK_SHADER.getShader());
//		RenderUtil.setShaderTexture(framebuffer.texture);
//		RenderUtil.postProcessText(framebuffer.texture);

	}

}
