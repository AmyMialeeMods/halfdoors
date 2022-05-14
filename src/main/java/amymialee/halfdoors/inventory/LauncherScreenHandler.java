package amymialee.halfdoors.inventory;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.items.DoorLauncherItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class LauncherScreenHandler extends ScreenHandler {
    private final ItemStack launcher;
    public final Inventory launcherInv;

    public LauncherScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack launcher) {
        super(Halfdoors.LAUNCHER_SCREEN_HANDLER, syncId);
        this.launcher = launcher;
        if (!playerInventory.player.world.isClient) {
            launcherInv = getInventory(launcher);
        } else {
            launcherInv = new SimpleInventory(1);
        }
        this.addSlot(new Slot(launcherInv, 0, 80, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Halfdoors.IRON_HALFDOOR.asItem());
            }
        });
        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, j * 18 + 51));
            }
        }
        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 109));
        }
    }

    public static SimpleInventory getInventory(ItemStack stack) {
        SimpleInventory inventory = new SimpleInventory(stack) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Halfdoors.IRON_HALFDOOR.asItem());
            }
        };
        if (stack.getOrCreateNbt().contains("ammo")) {
            inventory.setStack(0, ItemStack.fromNbt(stack.getOrCreateNbt().getCompound("ammo")));
        } else {
            inventory.setStack(0, new ItemStack(Items.AIR));
        }
        return inventory;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        ItemStack main = player.getMainHandStack();
        ItemStack side = player.getOffHandStack();
        return !main.isEmpty() && main == launcher || !side.isEmpty() && side == launcher;
    }

    @Override
    public void sendContentUpdates() {
        DoorLauncherItem.writeInventory(launcher, launcherInv.getStack(0));
        super.sendContentUpdates();
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack stack = slot.getStack();
            copy = stack.copy();
            if (index == 0) {
                if (!this.insertItem(stack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(stack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return copy;
    }
}
