package amymialee.halfdoors.mixin;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.items.DoorFlipperItem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void HalfDoors$RenderSlot(float tickDelta, MatrixStack matrices, CallbackInfo ci, PlayerEntity playerEntity, ItemStack itemStack, Arm arm, int i, int j, int k, int l) {
        ItemStack flipper = DoorFlipperItem.getFlipper(playerEntity);
        ItemStack alt = playerEntity.getOffHandStack();
        if (flipper != null) {
            if (arm == Arm.LEFT) {
                this.drawTexture(matrices, i - 91 - 29 + (!alt.isEmpty() ? -30 : 0), this.scaledHeight - 23, 24, 22, 29, 24);
            } else {
                this.drawTexture(matrices, i + 91 + (!alt.isEmpty() ? 30 : 0), this.scaledHeight - 23, 53, 22, 29, 24);
            }
        }
    }

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void HalfDoors$RenderAmmo(float tickDelta, MatrixStack matrices, CallbackInfo ci, PlayerEntity playerEntity, ItemStack itemStack) {
        ItemStack flipper = DoorFlipperItem.getFlipper(playerEntity);
        ItemStack alt = playerEntity.getOffHandStack();
        int i = this.scaledWidth / 2;
        int n = this.scaledHeight - 16 - 3;
        if (flipper != null) {
            if (playerEntity.getMainArm().getOpposite() == Arm.LEFT) {
                this.renderHotbarItem(i - 91 - 26 + (!alt.isEmpty() ? -30 : 0), n, tickDelta, playerEntity, ammoStack(flipper), 0);
            } else {
                this.renderHotbarItem(i + 91 + 10 + (!alt.isEmpty() ? 30 : 0), n, tickDelta, playerEntity, ammoStack(flipper), 0);
            }
        }
    }

    private ItemStack ammoStack(ItemStack stack) {
        int count = DoorFlipperItem.readAmmo(stack);
        ItemStack ammo = new ItemStack(Halfdoors.GOLD_DOOR_NUGGET);
        if (count > 0) {
            ammo.setCount(count);
        } else {
            ammo.getOrCreateNbt().putBoolean("hd:empty", true);
        }
        ammo.getOrCreateNbt().putInt("hd:recharge", stack.getDamage());
        return ammo;
    }
}
