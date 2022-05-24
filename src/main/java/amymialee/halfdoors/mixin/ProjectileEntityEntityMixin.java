package amymialee.halfdoors.mixin;

import amymialee.halfdoors.items.flipper.HomingArrowAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityEntityMixin extends Entity implements HomingArrowAccessor {
    public ProjectileEntityEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract void setVelocity(double x, double y, double z, float speed, float divergence);

    @Unique
    Entity target;

    @Override
    public Entity getHomingTarget() {
        return target;
    }

    @Override
    public void setHomingTarget(Entity target) {
        this.target = target;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void HalfDoors$Homing(CallbackInfo ci) {
        if (target != null && target.isAlive()) {
            Vec3d targetPos = target.getPos();
            double x = (targetPos.x - getX());
            double y = (target.getBodyY(0.5f) - getY());
            double z = (targetPos.z - getZ());
            Vec3d velocity = new Vec3d(x, y, z);
            velocity.normalize();
            setVelocity(velocity.x, velocity.y, velocity.z, 2f, 0);

        } else {
            target = null;
        }
        Vec3d vec3d = this.getVelocity();
        double e = vec3d.x;
        double f = vec3d.y;
        double g = vec3d.z;
        for(int i = 0; i < 4; ++i) {
            this.world.addParticle(ParticleTypes.ELECTRIC_SPARK, this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, -e, -f + 0.2D, -g);
        }
    }

    @Inject(method = "onCollision", at = @At("HEAD"))
    protected void HalfDoors$HomingEnd(HitResult hitResult, CallbackInfo ci) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            setHomingTarget(null);
            ((EntityHitResult) hitResult).getEntity().timeUntilRegen = 0;
        }
    }
}
