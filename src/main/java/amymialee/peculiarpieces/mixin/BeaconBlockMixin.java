package amymialee.peculiarpieces.mixin;

import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BeaconBlock.class)
public class BeaconBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void PeculiarPieces$UnbreakableBeacons(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient && player.isSneaking()) {
            Box box = Box.of(Vec3d.of(pos.add(0, 1, 0)), 1.0D, 1.0D, 1.0D);
            List<ItemEntity> list = world.getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class), box, (orb) -> !orb.getStack().getOrCreateNbt().getBoolean("Unbreakable"));
            for (ItemEntity itemEntity : list) {
                ItemStack stack = itemEntity.getStack();
                if (stack.getItem().isDamageable() && !stack.getOrCreateNbt().getBoolean("Unbreakable")) {
                    stack.getOrCreateNbt().putBoolean("Unbreakable", true);
                    itemEntity.setStack(stack);
                }
            }
            if (list.size() > 0) {
                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                if (lightningEntity != null) {
                    lightningEntity.setCosmetic(true);
                    lightningEntity.refreshPositionAfterTeleport(Vec3d.of(pos));
                    world.spawnEntity(lightningEntity);
                }
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                cir.setReturnValue(ActionResult.CONSUME);
            }
        }
    }
}