package xyz.amymialee.halfdoors;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class HalfDoorsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.OAK_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.SPRUCE_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.BIRCH_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.JUNGLE_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.ACACIA_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.DARK_OAK_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.MANGROVE_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.CHERRY_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.BAMBOO_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.CRIMSON_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.WARPED_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.IRON_HALFDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HalfDoors.IRON_FENCE_GATE, RenderLayer.getCutout());
    }
}