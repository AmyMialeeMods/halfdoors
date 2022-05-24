package amymialee.halfdoors.mixin;

import amymialee.halfdoors.items.flipper.HomingArrowAccessor;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;isCritical()Z"))
    private boolean HalfDoors$OnlyOneParticle(boolean original) {
        if (((HomingArrowAccessor) this).getHomingTarget() != null) {
            return false;
        }
        return original;
    }
}
