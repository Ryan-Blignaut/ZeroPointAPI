package me.thesilverecho.zeropoint.api.ui;

import me.thesilverecho.zeropoint.api.ui.widgets.IntractableComponent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class APIScreen extends Screen
{
	private final ArrayList<IntractableComponent> components = new ArrayList<>();

	protected void addComp(IntractableComponent component)
	{
		components.add(component);
	}

	protected APIScreen(Text title)
	{
		super(title);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY)
	{
		super.mouseMoved(mouseX, mouseY);
		components.forEach(component -> component.mouseMoved(mouseX, mouseY));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		super.render(matrices, mouseX, mouseY, delta);
		components.forEach(component -> component.renderComp(matrices, mouseX, mouseY, delta));
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		components.forEach(intractableComponent -> intractableComponent.mouseClicked(mouseX, mouseY, button));
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button)
	{
		components.forEach(intractableComponent -> intractableComponent.mouseReleased(mouseX, mouseY, button));
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
	{
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount)
	{
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers)
	{
		components.forEach(intractableComponent -> intractableComponent.keyReleased(keyCode, scanCode, modifiers));
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		components.forEach(intractableComponent -> intractableComponent.keyPressed(keyCode, scanCode, modifiers));
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char chr, int modifiers)
	{
		components.forEach(intractableComponent -> intractableComponent.charTyped(chr, modifiers));
		return super.charTyped(chr, modifiers);
	}
}
