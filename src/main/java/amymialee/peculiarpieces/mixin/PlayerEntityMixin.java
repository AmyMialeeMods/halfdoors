package amymialee.peculiarpieces.mixin;

import amymialee.peculiarpieces.util.CheckpointPlayerWrapper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements CheckpointPlayerWrapper {
    @Unique Vec3d checkpointPos;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override public Vec3d getCheckpointPos() {
        return checkpointPos;
    }

    @Override public void setCheckpointPos(Vec3d vec3d) {
        checkpointPos = vec3d;
    }

    @Unique
    public double getMountedHeightOffset() {
        return ((EntityAccessor) this).getDimensions().height * 0.9f;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void PeculiarPieces$WriteCheckpoint(NbtCompound nbt, CallbackInfo ci) {
        if (checkpointPos != null && checkpointPos.distanceTo(Vec3d.ZERO) > 1) {
            nbt.put("pp:checkpos", NbtHelper.fromBlockPos(new BlockPos(checkpointPos)));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void PeculiarPieces$ReadCheckpoint(NbtCompound nbt, CallbackInfo ci) {
        Vec3d vec3d = Vec3d.ofBottomCenter(NbtHelper.toBlockPos(nbt.getCompound("pp:checkpos")));
        if (vec3d.distanceTo(Vec3d.ZERO) > 1) {
            checkpointPos = vec3d;
        }
    }
}
