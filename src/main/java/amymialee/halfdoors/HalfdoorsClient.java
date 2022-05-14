package amymialee.halfdoors;

import amymialee.halfdoors.client.DoorSawEntityModel;
import amymialee.halfdoors.client.DoorSawEntityRenderer;
import amymialee.halfdoors.client.DoorcutterScreen;
import amymialee.halfdoors.inventory.LauncherScreen;
import amymialee.halfdoors.items.DoorLauncherItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class HalfdoorsClient implements ClientModInitializer {
    public static final EntityModelLayer DOOR_SAW_LAYER = new EntityModelLayer(Halfdoors.id("door_saw_layer"), "main");

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.IRON_FENCE_GATE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.IRON_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.OAK_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.SPRUCE_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.BIRCH_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.JUNGLE_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.ACACIA_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.DARK_OAK_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.CRIMSON_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.WARPED_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Halfdoors.DOORCUTTER, RenderLayer.getCutout());
        HandledScreens.register(Halfdoors.DOOR_CUTTER_SCREEN_HANDLER, DoorcutterScreen::new);
        HandledScreens.register(Halfdoors.LAUNCHER_SCREEN_HANDLER, LauncherScreen::new);

        EntityModelLayerRegistry.registerModelLayer(DOOR_SAW_LAYER, DoorSawEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(Halfdoors.DOORBLADE_ENTITY, DoorSawEntityRenderer::new);

        FabricLoader.getInstance().getModContainer(Halfdoors.MOD_ID).ifPresent(modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(Halfdoors.id("flatdoorcutters"), modContainer, ResourcePackActivationType.NORMAL));
    }

    static {
        ModelPredicateProviderRegistry.register(Halfdoors.DOOR_LAUNCHER, new Identifier("open"), (stack, world, entity, number) -> DoorLauncherItem.isOpen(stack) ? 1 : 0);
    }
}
