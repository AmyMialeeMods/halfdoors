package xyz.amymialee.halfdoors.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrapDoorBlock.class)
public abstract class TrapdoorBlockMixin extends HorizontalDirectionalBlock {
    @Shadow @Final public static BooleanProperty OPEN;
    @Shadow @Final public static EnumProperty<Half> HALF;
    @Shadow @Final private BlockSetType type;

    protected TrapdoorBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "updateShape", at = @At("TAIL"), cancellable = true)
    public void halfDoors$openDoors(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState neighborState, RandomSource randomSource, CallbackInfoReturnable<BlockState> cir) {
        if (direction == state.getValue(FACING)) {
            if (!neighborState.hasProperty(HALF) || state.getValue(HALF) != neighborState.getValue(HALF)) return;
            if (!neighborState.hasProperty(FACING) || state.getValue(FACING) != neighborState.getValue(FACING).getOpposite()) return;
            if (state.getValue(OPEN) != neighborState.getValue(OPEN)) {
                if (this.type.canOpenByHand() || neighborState.getBlock() instanceof TrapdoorBlockAccessor block && !block.getType().canOpenByHand()) {
                    cir.setReturnValue(state.setValue(OPEN, neighborState.getValue(OPEN)));
                }
            }
        } else if (direction.getAxis() == state.getValue(FACING).getClockWise().getAxis()) {
            if (!neighborState.hasProperty(HALF) || state.getValue(HALF) != neighborState.getValue(HALF)) return;
            if (!neighborState.hasProperty(FACING) || state.getValue(FACING) != neighborState.getValue(FACING)) return;
            if (state.getValue(OPEN) != neighborState.getValue(OPEN)) {
                if (this.type.canOpenByHand() || neighborState.getBlock() instanceof TrapdoorBlockAccessor block && !block.getType().canOpenByHand()) {
                    cir.setReturnValue(state.setValue(OPEN, neighborState.getValue(OPEN)));
                }
            }
        }
    }
}