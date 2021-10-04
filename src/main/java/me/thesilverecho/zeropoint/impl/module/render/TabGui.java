package me.thesilverecho.zeropoint.impl.module.render;

import com.google.common.util.concurrent.AtomicDouble;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.KeyEvent;
import me.thesilverecho.zeropoint.api.event.events.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;

@ClientModule(name = "Tab Gui", active = true, keyBinding = GLFW.GLFW_KEY_RIGHT_ALT)
public class TabGui extends BaseModule
{
	public TabGui(@Nullable Boolean active, @Nullable Integer key)
	{
		super(active, key);
	}


	@EventListener
	public void renderEvent(Render2dEvent event)
	{
		final MatrixStack matrixStack = event.matrixStack();
		AtomicDouble yCord = new AtomicDouble(3);
		BaseModule.ENABLE_MODULES.keySet().stream().sorted(String::compareToIgnoreCase).sorted(Collections.reverseOrder()).
		                         forEach((s) ->
		                         {
			                         final CustomFont font = APIFonts.THIN.getFont().setFontScale(0.5f);
			                         final double v = yCord.get();
			                         final float andAdd = (float) yCord.getAndAdd(font.getHeight());
			                         RenderUtilV2.rectangle(matrixStack, 2, (float) v, font.getWidth(s)+3, font.getHeight() + 1, 0, new ColourHolder(50, 50, 50, 80));
			                         font.render(matrixStack, s, 3, andAdd + 1);

		                         });

	}

	@EventListener
	public void renderEvent(KeyEvent event)
	{
		final int key = event.key();

	}


}
