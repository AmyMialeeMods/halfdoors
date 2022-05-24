package amymialee.halfdoors.items.flipper;

import amymialee.halfdoors.Halfdoors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DoorFlipperItem extends Item {
    public DoorFlipperItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (readAmmo(stack) > 0) {
            if (!world.isClient) {
                TinyDoorEntity tiny = new TinyDoorEntity(world, user);
                tiny.setVelocity(user, Math.max(user.getPitch() + -30, -90), user.getYaw(), 0.0F, 1F, 0.0F);
                tiny.setVelocity(user.getVelocity().getX() + tiny.getVelocity().getX() * 0.3, user.getVelocity().getY() + tiny.getVelocity().getY() * 0.4, user.getVelocity().getZ() + tiny.getVelocity().getZ() * 0.3);
                world.spawnEntity(tiny);
            }
            world.playSound(null, user.getX(), user.getY(), user.getZ(), Halfdoors.DOOR_FLIP, SoundCategory.PLAYERS, 0.6F, 0.4f + (world.getRandom().nextFloat()));
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.getItemCooldownManager().set(this, 8);
            writeAmmo(stack, readAmmo(stack) - 1);
        }
        return TypedActionResult.consume(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stack.getDamage() > 0) {
            stack.setDamage(stack.getDamage() - 1);
        } else if (readAmmo(stack) < 3) {
            writeAmmo(stack, readAmmo(stack) + 1);
            stack.setDamage(100);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.getDamage() > 0;
    }

    public int getItemBarColor(ItemStack stack) {
        int ammo = readAmmo(stack);
        float f = Math.max(0.0F, (ammo) / 3f);
        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    public static int readAmmo(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("ammo");
    }

    public static void writeAmmo(ItemStack stack, int ammo) {
        stack.getOrCreateNbt().putInt("ammo", ammo);
    }
}