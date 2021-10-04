package me.thesilverecho.zeropoint.impl.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T>
{

	protected HandledScreenMixin(Text title)
	{
		super(title);
	}

	@Shadow private boolean doubleClicking;

	@Shadow
	@Nullable
	protected abstract Slot getSlotAt(double x, double y);

	@Shadow
	protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

	@Shadow @Nullable protected Slot focusedSlot;

	@Inject(method = "mouseDragged", at = @At("TAIL"))
	private void onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> info)
	{
		if (button != GLFW_MOUSE_BUTTON_LEFT || doubleClicking)
			return;
		final Slot slot = getSlotAt(mouseX, mouseY);
		if (slot != null && slot.hasStack() && hasShiftDown())
			onMouseClick(slot, slot.id, button, SlotActionType.QUICK_MOVE);
	}

	@Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
	private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir)
	{
		final ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return;
		if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && focusedSlot != null && !focusedSlot.getStack().isEmpty() && player.currentScreenHandler.getCursorStack().isEmpty())
		{
			ItemStack itemStack = focusedSlot.getStack();


		}
	}

}
