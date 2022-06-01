package amymialee.halfdoors;

import amymialee.halfdoors.client.DoorSawEntityModel;
import amymialee.halfdoors.client.DoorSawEntityRenderer;
import amymialee.halfdoors.client.TinyDoorEntityModel;
import amymialee.halfdoors.client.TinyDoorEntityRenderer;
import amymialee.halfdoors.items.DoorLauncherItem;
import amymialee.halfdoors.screens.DoorcutterScreen;
import amymialee.halfdoors.screens.LauncherScreen;
import amymialee.halfdoors.util.DoorControls;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class HalfdoorsClient implements ClientModInitializer {
    public static final EntityModelLayer DOOR_SAW_LAYER = new EntityModelLayer(Halfdoors.id("door_saw_layer"), "main");
    public static final EntityModelLayer TINY_DOOR_LAYER = new EntityModelLayer(Halfdoors.id("tiny_door_layer"), "main");

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
        ScreenRegistry.register(Halfdoors.DOOR_CUTTER_SCREEN_HANDLER, DoorcutterScreen::new);
        ScreenRegistry.register(Halfdoors.LAUNCHER_SCREEN_HANDLER, LauncherScreen::new);

        EntityModelLayerRegistry.registerModelLayer(DOOR_SAW_LAYER, DoorSawEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(Halfdoors.DOORBLADE_ENTITY, DoorSawEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(TINY_DOOR_LAYER, TinyDoorEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(Halfdoors.TINY_DOOR_ENTITY, TinyDoorEntityRenderer::new);

        KeyBindingHelper.registerKeyBinding(DoorControls.DOOR_FLIP);

        FabricLoader.getInstance().getModContainer(Halfdoors.MOD_ID).ifPresent(modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(Halfdoors.id("flatdoorcutters"), modContainer, ResourcePackActivationType.NORMAL));
    }

    static {
        FabricModelPredicateProviderRegistry.register(Halfdoors.DOOR_LAUNCHER, new Identifier("open"), (stack, world, entity, number) -> DoorLauncherItem.isOpen(stack) ? 1 : 0);
        FabricModelPredicateProviderRegistry.register(Halfdoors.GOLD_DOOR_NUGGET, new Identifier("empty"), (stack, world, entity, number) -> stack.getOrCreateNbt().getBoolean("hd:empty") ? 1 : 0);
    }
}
