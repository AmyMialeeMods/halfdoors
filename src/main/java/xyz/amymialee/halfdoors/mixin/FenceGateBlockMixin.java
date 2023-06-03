package xyz.amymialee.halfdoors.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.state.property.BooleanProperty;
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
import xyz.amymialee.halfdoors.blocks.IronFenceGateBlock;

@Mixin(FenceGateBlock.class)
public class FenceGateBlockMixin extends HorizontalFacingBlock {
    @Shadow @Final public static BooleanProperty OPEN;

    protected FenceGateBlockMixin(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("ParameterCanBeLocal")
    @Inject(method = "getStateForNeighborUpdate", at = @At("RETURN"), cancellable = true)
    public void halfDoors$openDoors(BlockState state, @NotNull Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        state = cir.getReturnValue();
        if (direction.getAxis() == Direction.Axis.Y && neighborState.getBlock() instanceof FenceGateBlock) {
            boolean thisIron = state.getBlock() instanceof IronFenceGateBlock;
            boolean neighborIron = neighborState.getBlock() instanceof IronFenceGateBlock;
            if (state.get(FACING) == neighborState.get(FACING) && state.get(OPEN) != neighborState.get(OPEN)) {
                if (!thisIron || neighborIron) {
                    cir.setReturnValue(state.with(OPEN, neighborState.get(OPEN)));
                }
            }
        }
    }
}