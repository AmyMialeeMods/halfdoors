package amymialee.peculiarpieces.mixin;

import amymialee.peculiarpieces.PeculiarPieces;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow @Final protected ServerPlayerEntity player;

    @Inject(method = "setGameMode", at = @At("TAIL"))
    protected void PeculiarPieces$FlightRingGameModeChange(GameMode gameMode, GameMode previousGameMode, CallbackInfo ci) {
        Optional<TrinketComponent> optionalComponent = TrinketsApi.getTrinketComponent(player);
        if (optionalComponent.isPresent() && optionalComponent.get().isEquipped(PeculiarPieces.FLIGHT_RING)) {
            player.getAbilities().allowFlying = true;
        }
    }
}
