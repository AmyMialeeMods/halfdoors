package amymialee.halfdoors.mixin;

import amymialee.halfdoors.util.HomingArrowAccessor;
import ladysnake.blast.common.entity.BombEntity;
import net.fabricmc.loader.api.FabricLoader;
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
public abstract class ProjectileEntityMixin extends Entity implements HomingArrowAccessor {
    public ProjectileEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract void setVelocity(double x, double y, double z, float speed, float divergence);

    @Unique
    public Entity target;

    @Override
    public Entity getHomingTarget() {
        return target;
    }

    @Override
    public void setHomingTarget(Entity target) {
        this.target = target;
    }

    @Unique
    public int bounces;

    @Override
    public int getBounces() {
        return bounces;
    }

    @Override
    public void setBounces(int bounces) {
        this.bounces = bounces;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void HalfDoors$Homing(CallbackInfo ci) {
        if (target != null && target.isAlive()) {
            float bonusMult = 1;
            if (FabricLoader.getInstance().isModLoaded("blast")) {
                if (target instanceof BombEntity bomb) {
                    bonusMult += 1;
                    if (this.distanceTo(target) < 1) {
                        bomb.setExplosionRadius(bomb.getExplosionRadius() * 2.15f);
                        bomb.explode();
                        if (((ProjectileEntity) ((Object) this)) instanceof BombEntity bomb2) {
                            bomb2.setExplosionRadius(bomb2.getExplosionRadius() * 2f);
                            bomb2.explode();
                        }
                    }
                }
            }
            Vec3d targetPos = target.getPos();
            double x = (targetPos.x - getX());
            double y = (target.getBodyY(0.5f) - getY());
            double z = (targetPos.z - getZ());
            Vec3d velocity = new Vec3d(x, y, z);
            velocity.normalize();
            setVelocity(velocity.x, velocity.y, velocity.z, 1 + ((float) getBounces() / 2) * bonusMult, 0);
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y;
            double g = vec3d.z;
            for (int i = 0; i < 4; ++i) {
                this.world.addParticle(ParticleTypes.ENCHANTED_HIT, this.getX() + e * (double) i / 4.0D, this.getY() + f * (double) i / 4.0D, this.getZ() + g * (double) i / 4.0D, -e, -f + 0.2D, -g);
            }
        } else {
            target = null;
        }
    }

    @Inject(method = "onCollision", at = @At("HEAD"))
    protected void HalfDoors$HomingEnd(HitResult hitResult, CallbackInfo ci) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            if (getHomingTarget() != null) {
                setHomingTarget(null);
                if (FabricLoader.getInstance().isModLoaded("blast")) {
                    if (((ProjectileEntity) ((Object) this)) instanceof BombEntity bomb2) {
                        bomb2.setExplosionRadius(bomb2.getExplosionRadius() * 2f);
                        bomb2.explode();
                    }
                }
                ((EntityHitResult) hitResult).getEntity().timeUntilRegen = 0;
            }
        }
    }
}