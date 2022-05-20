package amymialee.peculiarpieces.features.checkpoint;

import amymialee.peculiarpieces.PeculiarPieces;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CheckpointPearlItem extends Item {
    public CheckpointPearlItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof CheckpointPlayerWrapper checkPlayer) {
            Vec3d checkpointPos = checkPlayer.getCheckpointPos();
            if (checkpointPos != null) {
                user.teleport(checkpointPos.getX(), checkpointPos.getY(), checkpointPos.getZ(), true);
                user.getItemCooldownManager().set(this, 1);
                user.sendMessage(new TranslatableText("%s.checkpoint_returned".formatted(PeculiarPieces.MOD_ID)).formatted(Formatting.GRAY), true);
                return TypedActionResult.success(user.getStackInHand(hand));
            }
        }
        return super.use(world, user, hand);
    }
}
