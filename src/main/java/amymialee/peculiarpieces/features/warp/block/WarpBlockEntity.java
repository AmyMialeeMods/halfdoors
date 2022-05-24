package amymialee.peculiarpieces.features.warp.block;

import amymialee.peculiarpieces.PeculiarPieces;
import amymialee.peculiarpieces.features.checkpoint.CheckpointPlayerWrapper;
import amymialee.peculiarpieces.features.warp.PositionPearlItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WarpBlockEntity extends LootableContainerBlockEntity {
    private DefaultedList<ItemStack> inventory;

    public WarpBlockEntity(BlockPos pos, BlockState state) {
        super(PeculiarPieces.WARP_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    public void onEntityCollided(Entity entity) {
        ItemStack stack = inventory.get(0);
        if (stack.isOf(PeculiarPieces.POS_PEARL) || stack.isOf(PeculiarPieces.CONSUMABLE_POS_PEARL)) {
            NbtCompound compound = stack.getOrCreateNbt();
            if (compound.contains("pp:stone")) {
                BlockPos pos = PositionPearlItem.readStone(stack);
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, true);
                } else {
                    entity.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                }
            }
        } else if (stack.isOf(PeculiarPieces.CHECKPOINT_PEARL)) {
            if (entity instanceof PlayerEntity player && player instanceof CheckpointPlayerWrapper checkPlayer) {
                Vec3d checkpointPos = checkPlayer.getCheckpointPos();
                if (checkpointPos != null) {
                    player.teleport(checkpointPos.getX(), checkpointPos.getY(), checkpointPos.getZ(), true);
                    player.sendMessage(new TranslatableText("%s.checkpoint_returned".formatted(PeculiarPieces.MOD_ID)).formatted(Formatting.GRAY), true);
                }
            }
        }
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    protected Text getContainerName() {
        return new TranslatableText("peculiarpieces.container.warp_block");
    }

    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new WarpScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return this.inventory.size();
    }
}
