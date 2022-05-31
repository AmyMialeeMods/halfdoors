package amymialee.peculiarpieces;

import amymialee.peculiarpieces.items.TransportPearlItem;
import amymialee.peculiarpieces.screens.WarpScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.MathHelper;

public class PeculiarPiecesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarPieces.CHECKPOINT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarPieces.CHECKPOINT_REMOVER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarPieces.CHECKPOINT_RETURNER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarPieces.INVISIBLE_PLATE_HEAVY, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarPieces.INVISIBLE_PLATE_LIGHT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarPieces.INVISIBLE_PLAYER_PLATE, RenderLayer.getTranslucent());
        ScreenRegistry.register(PeculiarPieces.WARP_SCREEN_HANDLER, WarpScreen::new);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> MathHelper.hsvToRgb(((float)(TransportPearlItem.getSlot(stack) + 1) / 8), 1.0F, 1.0F), PeculiarPieces.TRANS_PEARL);
    }
}
