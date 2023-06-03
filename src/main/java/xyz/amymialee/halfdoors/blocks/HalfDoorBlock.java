package xyz.amymialee.halfdoors.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HalfDoorBlock extends Block {
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
    public static final EnumProperty<HalfDoorSection> SECTION = EnumProperty.of("section", HalfDoorSection.class);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<DoorHinge> HINGE = Properties.DOOR_HINGE;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public final BlockSetType blockSetType;

    public HalfDoorBlock(AbstractBlock.@NotNull Settings settings, @NotNull BlockSetType blockSetType) {
        super(settings.sounds(blockSetType.soundType()));
        this.blockSetType = blockSetType;
        this.setDefaultState(this.stateManager.getDefaultState().with(SECTION, HalfDoorSection.ISOLATED).with(FACING, Direction.NORTH).with(HINGE, DoorHinge.LEFT).with(POWERED, Boolean.FALSE).with(OPEN, Boolean.FALSE));
    }

    @Override
    public VoxelShape getOutlineShape(@NotNull BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        boolean closed = !state.get(OPEN);
        boolean hingeRight = state.get(HINGE) == DoorHinge.RIGHT;
        return switch (direction) {
            default -> closed ? WEST_SHAPE : (hingeRight ? SOUTH_SHAPE : NORTH_SHAPE);
            case SOUTH -> closed ? NORTH_SHAPE : (hingeRight ? WEST_SHAPE : EAST_SHAPE);
            case WEST -> closed ? EAST_SHAPE : (hingeRight ? NORTH_SHAPE : SOUTH_SHAPE);
            case NORTH -> closed ? SOUTH_SHAPE : (hingeRight ? EAST_SHAPE : WEST_SHAPE);
        };
    }

    @Override
    public BlockState getStateForNeighborUpdate(@NotNull BlockState state, @NotNull Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis() == Direction.Axis.Y) {
            if (neighborState.contains(HINGE) && state.get(FACING) == neighborState.get(FACING) && state.get(HINGE) == neighborState.get(HINGE) && state.get(OPEN) != neighborState.get(OPEN)) {
                if (this.blockSetType.canOpenByHand() || ((neighborState.getBlock() instanceof HalfDoorBlock block && !block.blockSetType.canOpenByHand()) || (neighborState.getBlock() instanceof DoorBlock door && !door.getBlockSetType().canOpenByHand()))) {
                    return state.with(OPEN, neighborState.get(OPEN)).with(SECTION, this.getSection(world, pos, state.get(HINGE), state.get(FACING), state.get(OPEN)));
                }
            }
            HalfDoorSection section = this.getSection(world, pos, state.get(HINGE), state.get(FACING), state.get(OPEN));
            if (section != state.get(SECTION)) return state.with(SECTION, section);
        }
        if (direction == (state.get(HINGE) == DoorHinge.LEFT ? state.get(FACING).rotateYClockwise() : state.get(FACING).rotateYCounterclockwise())) {
            if (neighborState.contains(HINGE) && state.get(FACING) == neighborState.get(FACING) && state.get(HINGE) != neighborState.get(HINGE) && state.get(OPEN) != neighborState.get(OPEN)) {
                if (this.blockSetType.canOpenByHand() || ((neighborState.getBlock() instanceof HalfDoorBlock block && !block.blockSetType.canOpenByHand()) || (neighborState.getBlock() instanceof DoorBlock door && !door.getBlockSetType().canOpenByHand()))) {
                    return state.with(OPEN, neighborState.get(OPEN)).with(SECTION, this.getSection(world, pos, state.get(HINGE), state.get(FACING), state.get(OPEN)));
                }
            }
        }
        return state;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, @NotNull NavigationType type) {
        return switch (type) {
            case LAND, AIR -> state.get(OPEN);
            default -> false;
        };
    }

    @Nullable @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        boolean powered = world.isReceivingRedstonePower(pos);
        DoorHinge hinge = this.getHinge(ctx);
        Direction facing = ctx.getHorizontalPlayerFacing();
        return this.getDefaultState().with(SECTION, this.getSection(world, pos, hinge, facing, false)).with(FACING, facing).with(HINGE, hinge).with(POWERED, powered).with(OPEN, powered);
    }

    private DoorHinge getHinge(@NotNull ItemPlacementContext ctx) {
        BlockView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        Direction direction = ctx.getHorizontalPlayerFacing();
        Direction counterClock = direction.rotateYCounterclockwise();
        BlockPos counterPos = pos.offset(counterClock);
        BlockState counterState = world.getBlockState(counterPos);
        Direction clock = direction.rotateYClockwise();
        BlockPos clockPos = pos.offset(clock);
        BlockState clockState = world.getBlockState(clockPos);
        int rotationBias = (counterState.isFullCube(world, counterPos) ? -1 : 0) + (clockState.isFullCube(world, clockPos) ? 1 : 0);
        boolean counterSame = counterState.isOf(this);
        boolean clockSame = clockState.isOf(this);
        if ((!counterSame || clockSame) && rotationBias <= 0) {
            if ((!clockSame || counterSame) && rotationBias == 0) {
                int offsetX = direction.getOffsetX();
                int offsetZ = direction.getOffsetZ();
                Vec3d hitPos = ctx.getHitPos();
                double sidedX = hitPos.x - (double) pos.getX();
                double sidedZ = hitPos.z - (double) pos.getZ();
                return (offsetX >= 0 || !(sidedZ < 0.5)) && (offsetX <= 0 || !(sidedZ > 0.5)) && (offsetZ >= 0 || !(sidedX > 0.5)) && (offsetZ <= 0 || !(sidedX < 0.5)) ? DoorHinge.LEFT : DoorHinge.RIGHT;
            }
            return DoorHinge.LEFT;
        }
        return DoorHinge.RIGHT;
    }

    private HalfDoorSection getSection(@NotNull BlockView world, @NotNull BlockPos pos, DoorHinge hinge, Direction facing, boolean open) {
        BlockState aboveState = world.getBlockState(pos.up());
        BlockState belowState = world.getBlockState(pos.down());
        boolean above = aboveState.getBlock() == this && hinge == aboveState.get(HINGE) && facing == aboveState.get(FACING) && open == aboveState.get(OPEN);
        boolean below = belowState.getBlock() == this && hinge == belowState.get(HINGE) && facing == belowState.get(FACING) && open == belowState.get(OPEN);
        if (above || below) {
            if (above && below) {
                return HalfDoorSection.CENTER;
            }
            return above ? HalfDoorSection.LOWER : HalfDoorSection.UPPER;
        }
        return HalfDoorSection.ISOLATED;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!this.blockSetType.canOpenByHand()) {
            return ActionResult.PASS;
        }
        state = state.cycle(OPEN);
        world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
        world.playSound(player, pos, state.get(OPEN) ? this.blockSetType.doorOpen() : this.blockSetType.doorClose(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
        world.emitGameEvent(player, state.get(OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return ActionResult.success(world.isClient);
    }

    @Override
    public void neighborUpdate(BlockState state, @NotNull World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);
        if (!this.getDefaultState().isOf(sourceBlock) && powered != state.get(POWERED)) {
            if (powered != state.get(OPEN)) {
                world.playSound(null, pos, powered ? this.blockSetType.doorOpen() : this.blockSetType.doorClose(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
                world.emitGameEvent(null, powered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }
            world.setBlockState(pos, state.with(POWERED, powered).with(OPEN, powered), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public BlockState rotate(@NotNull BlockState state, @NotNull BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return mirror == BlockMirror.NONE ? state : state.rotate(mirror.getRotation(state.get(FACING))).cycle(HINGE);
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(SECTION, FACING, OPEN, HINGE, POWERED);
    }

    public enum HalfDoorSection implements StringIdentifiable {
        ISOLATED,
        UPPER,
        CENTER,
        LOWER;

        public String toString() {
            return this.asString();
        }

        @Override
        public String asString() {
            return switch (this) {
                case ISOLATED -> "isolated";
                case UPPER -> "upper";
                case CENTER -> "center";
                case LOWER -> "lower";
            };
        }
    }
}