package amymialee.peculiarpieces.blocks;

import amymialee.peculiarpieces.PeculiarPieces;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

@SuppressWarnings("deprecation")
public class InvisiblePressurePlate extends PressurePlateBlock {
    public InvisiblePressurePlate(ActivationRule type, Settings settings) {
        super(type, settings);
    }

    protected void playPressSound(WorldAccess world, BlockPos pos) {}

    protected void playDepressSound(WorldAccess world, BlockPos pos) {}

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        if (PeculiarPieces.visible) return BlockRenderType.MODEL;
        return BlockRenderType.INVISIBLE;
    }
}
