package amymialee.peculiarpieces.mixin;

import amymialee.peculiarpieces.PeculiarPieces;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "travel", at = @At("STORE"))
    public float PeculiarPieces$SlipperyShoesSlipping(float p) {
        if (((Entity) this) instanceof LivingEntity livingEntity) {
            Optional<TrinketComponent> optionalComponent = TrinketsApi.getTrinketComponent(livingEntity);
            if (optionalComponent.isPresent() && optionalComponent.get().isEquipped(PeculiarPieces.SLIPPERY_SHOES)) {
                return 1f / 0.91f;
            }
        }
        return p;
    }
}