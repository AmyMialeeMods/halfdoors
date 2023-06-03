package xyz.amymialee.halfdoors.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.WoodType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class IronFenceGateBlock extends FenceGateBlock {
    public IronFenceGateBlock(Settings settings) {
        super(settings, WoodType.OAK);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.PASS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean bl = world.isReceivingRedstonePower(pos);
            if (state.get(POWERED) != bl) {
                world.setBlockState(pos, state.with(POWERED, bl).with(OPEN, bl), Block.NOTIFY_LISTENERS);
                if (state.get(OPEN) != bl) {
                    world.playSound(null, pos, bl ? BlockSetType.IRON.doorOpen() : BlockSetType.IRON.doorClose(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
                    world.emitGameEvent(null, bl ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                }
            }
        }
    }
}