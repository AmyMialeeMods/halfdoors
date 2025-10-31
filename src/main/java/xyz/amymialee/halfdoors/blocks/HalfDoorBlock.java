package xyz.amymialee.halfdoors.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HalfDoorBlock extends Block {
    protected static final VoxelShape NORTH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EAST_SHAPE = Block.box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
    public static final EnumProperty<HalfDoorSection> SECTION = EnumProperty.create("section", HalfDoorSection.class);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public final BlockSetType blockSetType;

    public HalfDoorBlock(BlockBehaviour.@NotNull Properties settings, @NotNull BlockSetType blockSetType) {
        super(settings.sound(blockSetType.soundType()));
        this.blockSetType = blockSetType;
        this.registerDefaultState(this.stateDefinition.any().setValue(SECTION, HalfDoorSection.ISOLATED).setValue(FACING, Direction.NORTH).setValue(HINGE, DoorHingeSide.LEFT).setValue(POWERED, Boolean.FALSE).setValue(OPEN, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean closed = !state.getValue(OPEN);
        boolean hingeRight = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        return switch (direction) {
            default -> closed ? WEST_SHAPE : (hingeRight ? SOUTH_SHAPE : NORTH_SHAPE);
            case SOUTH -> closed ? NORTH_SHAPE : (hingeRight ? WEST_SHAPE : EAST_SHAPE);
            case WEST -> closed ? EAST_SHAPE : (hingeRight ? NORTH_SHAPE : SOUTH_SHAPE);
            case NORTH -> closed ? SOUTH_SHAPE : (hingeRight ? EAST_SHAPE : WEST_SHAPE);
        };
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos blockPos2, BlockState neighborState, RandomSource randomSource) {
        if (direction.getAxis() == Direction.Axis.Y) {
            if (neighborState.hasProperty(HINGE) && state.getValue(FACING) == neighborState.getValue(FACING) && state.getValue(HINGE) == neighborState.getValue(HINGE) && state.getValue(OPEN) != neighborState.getValue(OPEN)) {
                if (this.blockSetType.canOpenByHand() || ((neighborState.getBlock() instanceof HalfDoorBlock block && !block.blockSetType.canOpenByHand()) || (neighborState.getBlock() instanceof DoorBlock door && !door.type().canOpenByHand()))) {
                    return state.setValue(OPEN, neighborState.getValue(OPEN)).setValue(SECTION, this.getSection(world, pos, state.getValue(HINGE), state.getValue(FACING), state.getValue(OPEN)));
                }
            }
            HalfDoorSection section = this.getSection(world, pos, state.getValue(HINGE), state.getValue(FACING), state.getValue(OPEN));
            if (section != state.getValue(SECTION)) return state.setValue(SECTION, section);
        }
        if (direction == (state.getValue(HINGE) == DoorHingeSide.LEFT ? state.getValue(FACING).getClockWise() : state.getValue(FACING).getCounterClockWise())) {
            if (neighborState.hasProperty(HINGE) && state.getValue(FACING) == neighborState.getValue(FACING) && state.getValue(HINGE) != neighborState.getValue(HINGE) && state.getValue(OPEN) != neighborState.getValue(OPEN)) {
                if (this.blockSetType.canOpenByHand() || ((neighborState.getBlock() instanceof HalfDoorBlock block && !block.blockSetType.canOpenByHand()) || (neighborState.getBlock() instanceof DoorBlock door && !door.type().canOpenByHand()))) {
                    return state.setValue(OPEN, neighborState.getValue(OPEN)).setValue(SECTION, this.getSection(world, pos, state.getValue(HINGE), state.getValue(FACING), state.getValue(OPEN)));
                }
            }
        }
        return state;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return switch (type) {
            case LAND, AIR -> state.getValue(OPEN);
            default -> false;
        };
    }

    @Nullable @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        boolean powered = world.hasNeighborSignal(pos);
        DoorHingeSide hinge = this.getHinge(ctx);
        Direction facing = ctx.getHorizontalDirection();
        return this.defaultBlockState().setValue(SECTION, this.getSection(world, pos, hinge, facing, false)).setValue(FACING, facing).setValue(HINGE, hinge).setValue(POWERED, powered).setValue(OPEN, powered);
    }

    private DoorHingeSide getHinge(@NotNull BlockPlaceContext ctx) {
        BlockGetter world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Direction direction = ctx.getHorizontalDirection();
        Direction counterClock = direction.getCounterClockWise();
        BlockPos counterPos = pos.relative(counterClock);
        BlockState counterState = world.getBlockState(counterPos);
        Direction clock = direction.getClockWise();
        BlockPos clockPos = pos.relative(clock);
        BlockState clockState = world.getBlockState(clockPos);
        int rotationBias = (counterState.isCollisionShapeFullBlock(world, counterPos) ? -1 : 0) + (clockState.isCollisionShapeFullBlock(world, clockPos) ? 1 : 0);
        boolean counterSame = counterState.is(this);
        boolean clockSame = clockState.is(this);
        if ((!counterSame || clockSame) && rotationBias <= 0) {
            if ((!clockSame || counterSame) && rotationBias == 0) {
                int offsetX = direction.getStepX();
                int offsetZ = direction.getStepZ();
                Vec3 hitPos = ctx.getClickLocation();
                double sidedX = hitPos.x - (double) pos.getX();
                double sidedZ = hitPos.z - (double) pos.getZ();
                return (offsetX >= 0 || !(sidedZ < 0.5)) && (offsetX <= 0 || !(sidedZ > 0.5)) && (offsetZ >= 0 || !(sidedX > 0.5)) && (offsetZ <= 0 || !(sidedX < 0.5)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            }
            return DoorHingeSide.LEFT;
        }
        return DoorHingeSide.RIGHT;
    }

    private HalfDoorSection getSection(@NotNull BlockGetter world, @NotNull BlockPos pos, DoorHingeSide hinge, Direction facing, boolean open) {
        BlockState aboveState = world.getBlockState(pos.above());
        BlockState belowState = world.getBlockState(pos.below());
        boolean above = aboveState.getBlock() == this && hinge == aboveState.getValue(HINGE) && facing == aboveState.getValue(FACING) && open == aboveState.getValue(OPEN);
        boolean below = belowState.getBlock() == this && hinge == belowState.getValue(HINGE) && facing == belowState.getValue(FACING) && open == belowState.getValue(OPEN);
        if (above || below) {
            if (above && below) {
                return HalfDoorSection.CENTER;
            }
            return above ? HalfDoorSection.LOWER : HalfDoorSection.UPPER;
        }
        return HalfDoorSection.ISOLATED;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!this.blockSetType.canOpenByHand()) {
            return InteractionResult.PASS;
        }
        state = state.cycle(OPEN);
        world.setBlock(pos, state, Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE);
        world.playSound(player, pos, state.getValue(OPEN) ? this.blockSetType.doorOpen() : this.blockSetType.doorClose(), SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
        world.gameEvent(player, state.getValue(OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public void neighborChanged(BlockState state, @NotNull Level world, BlockPos pos, Block sourceBlock, Orientation orientation, boolean notify) {
        boolean powered = world.hasNeighborSignal(pos);
        if (!this.defaultBlockState().is(sourceBlock) && powered != state.getValue(POWERED)) {
            if (powered != state.getValue(OPEN)) {
                world.playSound(null, pos, powered ? this.blockSetType.doorOpen() : this.blockSetType.doorClose(), SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
                world.gameEvent(null, powered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }
            world.setBlock(pos, state.setValue(POWERED, powered).setValue(OPEN, powered), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return mirror == Mirror.NONE ? state : state.rotate(mirror.getRotation(state.getValue(FACING))).cycle(HINGE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(SECTION, FACING, OPEN, HINGE, POWERED);
    }

    public enum HalfDoorSection implements StringRepresentable {
        ISOLATED,
        UPPER,
        CENTER,
        LOWER;

        public String toString() {
            return this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return switch (this) {
                case ISOLATED -> "isolated";
                case UPPER -> "upper";
                case CENTER -> "center";
                case LOWER -> "lower";
            };
        }
    }
}