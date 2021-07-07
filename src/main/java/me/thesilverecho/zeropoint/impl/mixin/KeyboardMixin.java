package me.thesilverecho.zeropoint.impl.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.util.Keybind;
import me.thesilverecho.zeropoint.impl.event.KeyEvent;
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
	@Inject(method = "onKey", at = @At("HEAD") /*= @At(value = "INVOKE", target = "Lnet/minecraft/client/util/InputUtil;isKeyPressed(JI)Z", ordinal = 4)*/)
	public void onKeyEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci)
	{
		EventManager.call(new KeyEvent(key, action, modifiers));
		Stream<Keybind> keybinds = Keybind.REGISTERED_KEYBINDS.stream().filter(bind -> bind.code() != GLFW.GLFW_KEY_UNKNOWN && bind.code() == key);
		switch (action)
		{
			case GLFW.GLFW_PRESS -> keybinds.forEach(Keybind::onPress);
			case GLFW.GLFW_RELEASE -> keybinds.forEach(Keybind::onRelease);
		}
	}
}
