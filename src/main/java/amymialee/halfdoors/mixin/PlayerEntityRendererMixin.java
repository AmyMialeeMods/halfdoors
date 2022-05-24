package amymialee.halfdoors.mixin;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.items.launcher.DoorLauncherItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void HalfDoors$DoorLauncherPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Halfdoors.DOOR_LAUNCHER)) {
            if (!DoorLauncherItem.isOpen(itemStack) && !player.handSwinging) {
                cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
            }
        }
    }
}
