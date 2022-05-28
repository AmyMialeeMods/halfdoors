package amymialee.peculiarpieces.items;

import amymialee.peculiarpieces.PeculiarPieces;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class MountingStickItem extends Item {
    public MountingStickItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity livingEntity, Hand hand) {
        if (!livingEntity.getType().isIn(PeculiarPieces.MOUNT_BLACKLIST) && !user.getItemCooldownManager().isCoolingDown(this)) {
            if (user.getFirstPassenger() != null) {
                if (!livingEntity.hasPassengers()) {
                    user.getFirstPassenger().startRiding(livingEntity, true);
                }
            } else {
                if (!user.hasPassengers()) {
                    livingEntity.startRiding(user, true);
                }
            }
            user.getItemCooldownManager().set(this, 4);
        }
        return super.useOnEntity(stack, user, livingEntity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity user = context.getPlayer();
        if (user != null) {
            if (!user.getItemCooldownManager().isCoolingDown(this)) {
                Entity passenger = user.getFirstPassenger();
                if (passenger != null) {
                    passenger.dismountVehicle();
                    Vec3d hitPos = context.getHitPos();
                    passenger.setPos(hitPos.x, hitPos.y, hitPos.z);
                    passenger.resetPosition();
                    passenger.setPosition(hitPos);
                }
                user.getItemCooldownManager().set(this, 4);
            }
        }
        return super.useOnBlock(context);
    }
}