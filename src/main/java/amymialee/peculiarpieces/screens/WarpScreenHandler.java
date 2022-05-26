package amymialee.peculiarpieces.screens;

import amymialee.peculiarpieces.PeculiarPieces;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class WarpScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public WarpScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1));
    }

    public WarpScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(PeculiarPieces.WARP_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.addSlot(new Slot(inventory, 0, 80, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return ((stack.isOf(PeculiarPieces.POS_PEARL) || (playerInventory.player.getAbilities().creativeMode && stack.isOf(PeculiarPieces.CONSUMABLE_POS_PEARL))) && stack.getOrCreateNbt().contains("pp:stone")) || stack.isOf(PeculiarPieces.CHECKPOINT_PEARL);
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

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
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

    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }
}
