package xyz.amymialee.halfdoors.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;

public class IronFenceGateBlock extends FenceGateBlock {
    public IronFenceGateBlock(Properties settings) {
        super(WoodType.OAK, settings);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, Orientation orientation, boolean notify) {
        if (!world.isClientSide()) {
            boolean bl = world.hasNeighborSignal(pos);
            if (state.getValue(POWERED) != bl) {
                world.setBlock(pos, state.setValue(POWERED, bl).setValue(OPEN, bl), Block.UPDATE_CLIENTS);
                if (state.getValue(OPEN) != bl) {
                    world.playSound(null, pos, bl ? BlockSetType.IRON.doorOpen() : BlockSetType.IRON.doorClose(), SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
                    world.gameEvent(null, bl ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                }
            }
        }
    }
}