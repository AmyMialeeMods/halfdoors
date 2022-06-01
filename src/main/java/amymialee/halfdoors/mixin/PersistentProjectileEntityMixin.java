package amymialee.halfdoors.mixin;

import amymialee.halfdoors.util.HomingArrowAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntityMixin implements HomingArrowAccessor {
    public PersistentProjectileEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
    private ParticleEffect HalfDoors$OnlyOneParticle(ParticleEffect parameters) {
        if (getHomingTarget() != null) {
            return ParticleTypes.ENCHANTED_HIT;
        }
        return parameters;
    }
}
