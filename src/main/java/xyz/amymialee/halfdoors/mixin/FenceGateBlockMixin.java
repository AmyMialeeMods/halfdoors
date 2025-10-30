package xyz.amymialee.halfdoors.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.halfdoors.blocks.IronFenceGateBlock;

@Mixin(FenceGateBlock.class)
public abstract class FenceGateBlockMixin extends HorizontalDirectionalBlock {
    @Shadow @Final public static BooleanProperty OPEN;

    protected FenceGateBlockMixin(Properties settings) {
        super(settings);
    }

    @SuppressWarnings("ParameterCanBeLocal")
    @Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
    public void halfDoors$openDoors(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState neighborState, RandomSource randomSource, CallbackInfoReturnable<BlockState> cir) {
        state = cir.getReturnValue();
        if (direction.getAxis() == Direction.Axis.Y && neighborState.getBlock() instanceof FenceGateBlock) {
            boolean thisIron = state.getBlock() instanceof IronFenceGateBlock;
            boolean neighborIron = neighborState.getBlock() instanceof IronFenceGateBlock;
            if (state.getValue(FACING) == neighborState.getValue(FACING) && state.getValue(OPEN) != neighborState.getValue(OPEN)) {
                if (!thisIron || neighborIron) {
                    cir.setReturnValue(state.setValue(OPEN, neighborState.getValue(OPEN)));
                }
            }
        }
    }
}