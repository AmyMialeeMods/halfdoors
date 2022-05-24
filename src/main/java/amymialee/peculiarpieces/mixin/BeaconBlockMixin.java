package amymialee.peculiarpieces.mixin;

import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlock.class)
public class BeaconBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"))
    public void PeculiarPieces$UnbreakableBeacons(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient && player.isSneaking() && stack.isDamageable()) {
            stack.getOrCreateNbt().putBoolean("Unbreakable", true);
            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
            if (lightningEntity != null) {
                lightningEntity.setCosmetic(true);
                lightningEntity.refreshPositionAfterTeleport(Vec3d.of(pos));
                world.spawnEntity(lightningEntity);
            }
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
        }
    }
}