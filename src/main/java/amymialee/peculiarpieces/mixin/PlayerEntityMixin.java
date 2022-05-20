package amymialee.peculiarpieces.mixin;

import amymialee.peculiarpieces.features.checkpoint.CheckpointPlayerWrapper;
import amymialee.peculiarpieces.util.PeculiarHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
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

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (checkpointPos != null) {
            nbt.put("pp:checkpos", PeculiarHelper.fromVec3d(checkpointPos));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        checkpointPos = PeculiarHelper.toVec3d(nbt.getCompound("pp:checkpos"));
    }

    @Unique
    public double getMountedHeightOffset() {
        return ((EntityAccessor) this).getDimensions().height * 0.9f;
    }
}
