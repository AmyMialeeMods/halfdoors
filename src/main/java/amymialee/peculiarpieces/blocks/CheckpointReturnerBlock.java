package amymialee.peculiarpieces.blocks;

import amymialee.peculiarpieces.PeculiarPieces;
import amymialee.peculiarpieces.util.CheckpointPlayerWrapper;
import amymialee.peculiarpieces.util.WarpManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class CheckpointReturnerBlock extends AbstractCheckpointBlock {
    public CheckpointReturnerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (!world.isClient() && entity instanceof PlayerEntity player && player instanceof CheckpointPlayerWrapper checkPlayer) {
            Vec3d checkpointPos = checkPlayer.getCheckpointPos();
            if (checkpointPos != null && checkpointPos.distanceTo(entity.getPos()) > 2) {
                WarpManager.queueTeleport(player, checkpointPos);
                player.sendMessage(new TranslatableText("%s.checkpoint_returned".formatted(PeculiarPieces.MOD_ID)).formatted(Formatting.GRAY), true);
            }
        }
    }
}
