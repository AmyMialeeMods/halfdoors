package amymialee.halfdoors.items.launcher;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.inventory.LauncherScreenHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class DoorLauncherItem extends RangedWeaponItem implements Vanishable {
    public static final Predicate<ItemStack> DOOR_AMMO = (stack) -> stack.isOf(Halfdoors.IRON_HALFDOOR.asItem());

    public DoorLauncherItem(Item.Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (hasShulkerUpgrade(stack) && user.isSneaking()) {
            stack.getOrCreateNbt().putBoolean("hd:open", !isOpen(stack));
        } else {
            if (!isOpen(stack)) {
                ItemStack ammo = getAmmo(user, stack);
                if (!ammo.isEmpty()) {
                    if (!world.isClient) {
                        DoorbladeEntity doorSaw = new DoorbladeEntity(world, user, stack);
                        doorSaw.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3.0F, 0.0F);
                        doorSaw.setPierceLevel((byte) 127);
                        doorSaw.setDamage(hasNetheriteUpgrade(stack) ? 6 : 4);
                        doorSaw.setSound(Halfdoors.DOORBLADE_HIT_GROUND);
                        if (user.getAbilities().creativeMode) {
                            doorSaw.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                        }
                        world.spawnEntity(doorSaw);
                    }
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), Halfdoors.DOOR_LAUNCHER_FIRE, SoundCategory.PLAYERS, 0.6F, 10.0F + (world.getRandom().nextFloat() * 4.4F));
                    if (!user.getAbilities().creativeMode) {
                        if (hasStoredAmmo(stack)) {
                            ItemStack newAmmo = readInventory(stack);
                            newAmmo.decrement(1);
                            writeInventory(stack, newAmmo);
                        } else {
                            ammo.decrement(1);
                        }
                    }
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    user.getItemCooldownManager().set(this, hasNetheriteUpgrade(stack) ? 6 : 10);
                    return TypedActionResult.consume(stack);
                }
            } else {
                if (!world.isClient) {
                    openMenu((ServerPlayerEntity) user, new NamedScreenHandlerFactory() {
                        @Override
                        public Text getDisplayName() {
                            return stack.getName();
                        }

                        @Override
                        public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                            return new LauncherScreenHandler(syncId, inv, stack);
                        }
                    });
                }
                return TypedActionResult.success(user.getStackInHand(hand));
            }
        }
        return TypedActionResult.fail(stack);
    }

    public static boolean hasStoredAmmo(ItemStack stack) {
        if (hasShulkerUpgrade(stack)) {
            if (stack.getOrCreateNbt().contains("ammo")) {
                return !readInventory(stack).isEmpty();
            }
        }
        return false;
    }

    public static ItemStack getAmmo(PlayerEntity player, ItemStack stack) {
        if (hasStoredAmmo(stack)) {
            return readInventory(stack);
        }
        return player.getArrowType(stack);
    }

    public static ItemStack readInventory(ItemStack stack) {
        return ItemStack.fromNbt(stack.getOrCreateNbt().getCompound("ammo"));
    }

    public static void writeInventory(ItemStack stack, ItemStack ammo) {
        stack.getOrCreateNbt().put("ammo", ammo.writeNbt(new NbtCompound()));
    }

    public void openMenu(ServerPlayerEntity player, NamedScreenHandlerFactory menu) {
        var menuProvider = new NamedScreenHandlerFactory() {
            @Nullable
            @Override
            public ScreenHandler createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
                return menu.createMenu(id, inventory, player);
            }
            @Override
            public Text getDisplayName() {
                return menu.getDisplayName();
            }
        };
        player.openHandledScreen(menuProvider);
    }

    public static boolean hasShulkerUpgrade(ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean("hd:shulker");
    }

    public static boolean hasNetheriteUpgrade(ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean("hd:netherite");
    }

    public static boolean hasTridentUpgrade(ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean("hd:trident");
    }

    public static boolean isOpen(ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean("hd:open");
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return hasShulkerUpgrade(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        ItemStack ammo = readInventory(stack);
        return 13 - Math.round(13.0F - (float)ammo.getCount() * 13.0F / (float)ammo.getMaxCount());
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return super.getItemBarColor(stack);//TODO: Dark gray to Red (like smithing table)
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if(hasShulkerUpgrade(stack)) {
            tooltip.add(new TranslatableText("modifier.%s.shulker".formatted(Halfdoors.MOD_ID)).formatted(Formatting.GRAY));
        }
        if(hasNetheriteUpgrade(stack)) {
            tooltip.add(new TranslatableText("modifier.%s.netherite".formatted(Halfdoors.MOD_ID)).formatted(Formatting.GRAY));
        }
        if(hasTridentUpgrade(stack)) {
            tooltip.add(new TranslatableText("modifier.%s.trident".formatted(Halfdoors.MOD_ID)).formatted(Formatting.GRAY));
        }
    }

    public Predicate<ItemStack> getProjectiles() {
        return DOOR_AMMO;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    public int getRange() {
        return 32;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        super.appendStacks(group, stacks);
        if (this.isIn(group)) {
            ItemStack stack = new ItemStack(this);
            NbtCompound compound = stack.getOrCreateNbt();
            compound.putBoolean("hd:shulker", true);
            compound.putBoolean("hd:trident", true);
            compound.putBoolean("hd:netherite", true);
            stacks.add(stack);
        }
    }
}
