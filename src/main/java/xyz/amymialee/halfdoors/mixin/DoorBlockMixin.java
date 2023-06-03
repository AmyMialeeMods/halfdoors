package xyz.amymialee.halfdoors.mixin;

import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.halfdoors.blocks.HalfDoorBlock;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {
    @Shadow @Final public static EnumProperty<DoorHinge> HINGE;
    @Shadow @Final public static DirectionProperty FACING;
    @Shadow @Final public static BooleanProperty OPEN;
    @Shadow @Final private BlockSetType blockSetType;
    @Shadow @Final public static EnumProperty<DoubleBlockHalf> HALF;

    @Inject(method = "getStateForNeighborUpdate", at = @At("HEAD"), cancellable = true)
    public void halfDoors$openDoors(BlockState state, @NotNull Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (direction == Direction.UP && state.get(HALF) == DoubleBlockHalf.UPPER) {
            if (neighborState.contains(HINGE) && state.get(FACING) == neighborState.get(FACING) && state.get(HINGE) == neighborState.get(HINGE) && state.get(OPEN) != neighborState.get(OPEN)) {
                if (this.blockSetType.canOpenByHand() || neighborState.getBlock() instanceof HalfDoorBlock block && !block.blockSetType.canOpenByHand()) {
                    cir.setReturnValue(state.with(OPEN, neighborState.get(OPEN)));
                    return;
                }
            }
        }
        if (direction == (state.get(HINGE) == DoorHinge.LEFT ? state.get(FACING).rotateYClockwise() : state.get(FACING).rotateYCounterclockwise())) {
            if (neighborState.contains(HINGE) && state.get(FACING) == neighborState.get(FACING) && state.get(HINGE) != neighborState.get(HINGE) && state.get(OPEN) != neighborState.get(OPEN)) {
                if (this.blockSetType.canOpenByHand() || ((neighborState.getBlock() instanceof HalfDoorBlock block && !block.blockSetType.canOpenByHand()) || (neighborState.getBlock() instanceof DoorBlock door && !door.getBlockSetType().canOpenByHand()))) {
                    cir.setReturnValue(state.with(OPEN, neighborState.get(OPEN)));
                }
            }
        }
    }
}