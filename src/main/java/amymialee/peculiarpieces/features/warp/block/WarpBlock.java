package amymialee.peculiarpieces.features.warp.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class WarpBlock extends BlockWithEntity {
    public static final DirectionProperty FACING;
    public static final BooleanProperty ENABLED;

    public WarpBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(ENABLED, false));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing()).with(ENABLED, false);
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WarpBlockEntity(pos, state);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WarpBlockEntity warpBlockEntity) {
                warpBlockEntity.setCustomName(itemStack.getName());
            }
        }
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            this.updateEnabled(world, pos, state);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WarpBlockEntity warpBlockEntity) {
                if (player.isSneaking() && player.getAbilities().allowModifyWorld) {
                    player.openHandledScreen(warpBlockEntity);
                } else {
                    warpBlockEntity.onEntityCollided(player);
                }
            }
            return ActionResult.CONSUME;
        }
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        this.updateEnabled(world, pos, state);
    }

    private void updateEnabled(World world, BlockPos pos, BlockState state) {
        boolean bl = !world.isReceivingRedstonePower(pos);
        if (bl != state.get(ENABLED)) {
            world.setBlockState(pos, state.with(ENABLED, bl), Block.NO_REDRAW);
        }
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WarpBlockEntity) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WarpBlockEntity warpBlockEntity) {
                warpBlockEntity.onEntityCollided(entity);
            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
        ENABLED = Properties.ENABLED;
    }
}
