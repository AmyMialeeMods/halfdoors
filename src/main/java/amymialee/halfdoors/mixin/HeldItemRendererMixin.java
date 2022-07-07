package amymialee.halfdoors.mixin;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.items.DoorLauncherItem;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Inject(method = "isChargedCrossbow", at = @At("HEAD"), cancellable = true)
    private static void HalfDoors$ChargedDoorLauncherHeld(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isOf(Halfdoors.DOOR_LAUNCHER)) {
            cir.setReturnValue(!DoorLauncherItem.isOpen(stack));
        }
    }
}