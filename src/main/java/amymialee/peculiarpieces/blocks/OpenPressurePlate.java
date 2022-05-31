package amymialee.peculiarpieces.blocks;

import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

@SuppressWarnings("deprecation")
public class OpenPressurePlate extends PressurePlateBlock {
    private final boolean invis;
    private final int weight;

    public OpenPressurePlate(boolean invis, int weight, Settings settings) {
        super(ActivationRule.EVERYTHING, settings);
        this.invis = invis;
        this.weight = weight;
    }

    protected void playPressSound(WorldAccess world, BlockPos pos) {
        if (!invis) {
            switch (weight) {
                case 0 -> world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.8f);
                case 1 -> world.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.6f);
                case 2 -> world.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.2f);
            }
        }
    }

    protected void playDepressSound(WorldAccess world, BlockPos pos) {
        if (!invis) {
            switch (weight) {
                case 0 -> world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.7f);
                case 1 -> world.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.5f);
                case 2 -> world.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.1f);
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        if (VisibleBarriers.isVisible() || !invis) return BlockRenderType.MODEL;
        return BlockRenderType.INVISIBLE;
    }

    protected int getRedstoneOutput(World world, BlockPos pos) {
        Box box = BOX.offset(pos);
        List<? extends Entity> list;
        switch(this.weight) {
            case 0:
                list = world.getOtherEntities(null, box);
                break;
            case 1:
                list = world.getNonSpectatingEntities(LivingEntity.class, box);
                break;
            case 2:
                list = world.getNonSpectatingEntities(PlayerEntity.class, box);
                break;
            default:
                return 0;
        }
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (!entity.canAvoidTraps()) {
                    return 15;
                }
            }
        }
        return 0;
    }
}
