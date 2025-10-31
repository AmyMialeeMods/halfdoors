package xyz.amymialee.halfdoors.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.halfdoors.blocks.HalfDoorBlock;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin {
    @Shadow @Final public static EnumProperty<DoorHingeSide> HINGE;
    @Shadow @Final public static EnumProperty<Direction> FACING;
    @Shadow @Final public static BooleanProperty OPEN;
    @Shadow @Final private BlockSetType type;
    @Shadow @Final public static EnumProperty<DoubleBlockHalf> HALF;

    @Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
    public void halfDoors$openDoors(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState neighborState, RandomSource randomSource, CallbackInfoReturnable<BlockState> cir) {
        if (direction == Direction.UP && state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            if (neighborState.hasProperty(HINGE) && neighborState.hasProperty(OPEN) && neighborState.hasProperty(FACING) &&  state.getValue(FACING) == neighborState.getValue(FACING) && state.getValue(HINGE) == neighborState.getValue(HINGE) && state.getValue(OPEN) != neighborState.getValue(OPEN)) {
                if (this.type.canOpenByHand() || neighborState.getBlock() instanceof HalfDoorBlock block && !block.blockSetType.canOpenByHand()) {
                    cir.setReturnValue(state.setValue(OPEN, neighborState.getValue(OPEN)));
                    return;
                }
            }
        }
        if (direction == (state.getValue(HINGE) == DoorHingeSide.LEFT ? state.getValue(FACING).getClockWise() : state.getValue(FACING).getCounterClockWise())) {
            if (neighborState.hasProperty(HINGE) && neighborState.hasProperty(OPEN) && neighborState.hasProperty(FACING) && state.getValue(FACING) == neighborState.getValue(FACING) && state.getValue(HINGE) != neighborState.getValue(HINGE) && state.getValue(OPEN) != neighborState.getValue(OPEN)) {
                if (this.type.canOpenByHand() || ((neighborState.getBlock() instanceof HalfDoorBlock block && !block.blockSetType.canOpenByHand()) || (neighborState.getBlock() instanceof DoorBlock door && !door.type().canOpenByHand()))) {
                    cir.setReturnValue(state.setValue(OPEN, neighborState.getValue(OPEN)));
                }
            }
        }
    }
}