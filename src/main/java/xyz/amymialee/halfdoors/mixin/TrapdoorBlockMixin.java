package xyz.amymialee.halfdoors.mixin;

import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.state.property.BooleanProperty;
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

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin extends HorizontalFacingBlock {
    @Shadow @Final public static BooleanProperty OPEN;
    @Shadow @Final public static EnumProperty<BlockHalf> HALF;
    @Shadow @Final private BlockSetType blockSetType;

    protected TrapdoorBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getStateForNeighborUpdate", at = @At("TAIL"), cancellable = true)
    public void halfDoors$openDoors(BlockState state, @NotNull Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (direction == state.get(FACING)) {
            if (!neighborState.contains(HALF) || state.get(HALF) != neighborState.get(HALF)) return;
            if (!neighborState.contains(FACING) || state.get(FACING) != neighborState.get(FACING).getOpposite()) return;
            if (state.get(OPEN) != neighborState.get(OPEN)) {
                if (this.blockSetType.canOpenByHand() || neighborState.getBlock() instanceof TrapdoorBlockAccessor block && !block.getBlockSetType().canOpenByHand()) {
                    cir.setReturnValue(state.with(OPEN, neighborState.get(OPEN)));
                }
            }
        } else if (direction.getAxis() == state.get(FACING).rotateYClockwise().getAxis()) {
            if (!neighborState.contains(HALF) || state.get(HALF) != neighborState.get(HALF)) return;
            if (!neighborState.contains(FACING) || state.get(FACING) != neighborState.get(FACING)) return;
            if (state.get(OPEN) != neighborState.get(OPEN)) {
                if (this.blockSetType.canOpenByHand() || neighborState.getBlock() instanceof TrapdoorBlockAccessor block && !block.getBlockSetType().canOpenByHand()) {
                    cir.setReturnValue(state.with(OPEN, neighborState.get(OPEN)));
                }
            }
        }
    }
}