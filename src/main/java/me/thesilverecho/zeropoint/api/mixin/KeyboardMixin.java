package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.KeyEvent;
import me.thesilverecho.zeropoint.api.event.events.ScreenCharEvent;
import me.thesilverecho.zeropoint.api.event.events.ScreenKeyboardEvent;
import me.thesilverecho.zeropoint.api.util.Keybind;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin
{
	@Inject(method = "onKey", at = @At("HEAD"))
	public void onKeyEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci)
	{
		EventManager.call(new KeyEvent(key, action, modifiers));
		Stream<Keybind> keybinds = Keybind.REGISTERED_KEYBINDS.stream().filter(bind -> bind.getKeyCode() != GLFW.GLFW_KEY_UNKNOWN && bind.getKeyCode() == key);
		switch (action)
		{
			case GLFW.GLFW_PRESS -> keybinds.forEach(Keybind::onPress);
			case GLFW.GLFW_RELEASE -> keybinds.forEach(Keybind::onRelease);
		}
	}


	@Inject(method = "onKey", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", shift = At.Shift.BEFORE))
	public void onScreenKeyEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci)
	{
		EventManager.call(new ScreenKeyboardEvent(key, scancode, modifiers));
	}

	@Inject(method = "onChar", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"))
	public void onScreenKeyEvent(long window, int codePoint, int modifiers, CallbackInfo ci)
	{
		EventManager.call(new ScreenCharEvent((char) codePoint, modifiers));
	}

}
