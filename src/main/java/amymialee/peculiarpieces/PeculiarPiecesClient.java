package amymialee.peculiarpieces;

import amymialee.peculiarpieces.blocks.InvisibleBlock;
import amymialee.peculiarpieces.features.warp.TransportPearlItem;
import amymialee.peculiarpieces.features.warp.block.WarpScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class PeculiarPiecesClient implements ClientModInitializer {
    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarPieces.CHECKPOINT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarPieces.CHECKPOINT_REMOVER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarPieces.CHECKPOINT_RETURNER, RenderLayer.getTranslucent());
        HandledScreens.register(PeculiarPieces.WARP_SCREEN_HANDLER, WarpScreen::new);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> MathHelper.hsvToRgb(((float)(TransportPearlItem.getSlot(stack) + 1) / 8), 1.0F, 1.0F), PeculiarPieces.TRANS_PEARL);

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.%s.visibility".formatted(PeculiarPieces.MOD_ID),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.%s".formatted(PeculiarPieces.MOD_ID)
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                InvisibleBlock.visible = !InvisibleBlock.visible;
                client.worldRenderer.reload();
            }
        });
    }
}
