package amymialee.halfdoors.items;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.entities.TinyDoorEntity;
import amymialee.halfdoors.util.DoorControls;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DoorFlipperItem extends TrinketItem implements Wearable {
    public static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior(){
        @Override
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            return dispenseTrinket(pointer, stack) ? stack : super.dispenseSilently(pointer, stack);
        }
    };

    public DoorFlipperItem(Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (equipItem(user, stack)) {
            return TypedActionResult.success(stack, world.isClient());
        }
        return super.use(world, user, hand);
    }

    public void toss(ServerPlayerEntity user, ItemStack stack) {
        World world = user.world;
        if (readAmmo(stack) > 0) {
            if (!world.isClient) {
                TinyDoorEntity tiny = new TinyDoorEntity(world, user);
                tiny.setVelocity(user, Math.max(user.getPitch() - 30, -90), user.getYaw(), 0.0F, 1F, 0.0F);
                Vec3d vel = user.getVelocity();
                vel.multiply(0, 0.6f, 0);
                tiny.setVelocity(vel.getX() + tiny.getVelocity().getX() * 0.3, Math.max(0, vel.getY()) + tiny.getVelocity().getY() * 0.4, vel.getZ() + tiny.getVelocity().getZ() * 0.3);
                world.spawnEntity(tiny);
            }
            world.playSound(null, user.getX(), user.getY(), user.getZ(), Halfdoors.DOOR_FLIP, SoundCategory.PLAYERS, 0.3F, 0.4f + (world.getRandom().nextFloat()));
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.getItemCooldownManager().set(Halfdoors.GOLD_DOOR_NUGGET, 6);
            user.getItemCooldownManager().set(this, 6);
            user.swingHand(Hand.OFF_HAND);
            writeAmmo(stack, stack.getOrCreateNbt().getInt("ammo") - 1);
        }
    }

    public static ItemStack getFlipper(PlayerEntity player) {
        Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);
        if (optional.isPresent() && optional.get().isEquipped(Halfdoors.DOOR_FLIPPER)) {
            for (Pair<SlotReference, ItemStack> pair : optional.get().getEquipped(Halfdoors.DOOR_FLIPPER)) {
                return pair.getRight();
            }
        }
        return null;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!(entity instanceof PlayerEntity player) || !player.world.isClient()) {
            int damage = stack.getDamage();
            if (damage > 0) {
                damage--;
                int ammo = readAmmo(stack);
                if (damage <= 0 && ammo < 4) {
                    writeAmmo(stack, ammo);
                    stack.setDamage(100);
                } else {
                    stack.setDamage(damage);
                }
            }
        }
        super.tick(stack, slot, entity);
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
        return stack.getOrCreateNbt().getInt("ammo") + (stack.getDamage() == 0 ? 1 : 0);
    }

    public static void writeAmmo(ItemStack stack, int ammo) {
        stack.getOrCreateNbt().putInt("ammo", ammo);
    }

    public static boolean dispenseTrinket(BlockPointer pointer, ItemStack stack) {
        BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
        List<PlayerEntity> list = pointer.getWorld().getEntitiesByClass(PlayerEntity.class, new Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR.and(new EntityPredicates.Equipable(stack)));
        if (list.isEmpty()) {
            return false;
        }
        PlayerEntity user = list.get(0);
        return equipItem(user, stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        List<Text> text = DoorControls.DOOR_FLIP.getBoundKeyLocalizedText().getWithStyle(Style.EMPTY.withColor(Formatting.BLUE));
        if (!text.isEmpty()) {
            tooltip.add(new TranslatableText("item.halfdoors.door_launcher.description", text.get(0)).formatted(Formatting.GRAY));
        }
    }
}