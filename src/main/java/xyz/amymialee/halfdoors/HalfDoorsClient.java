package xyz.amymialee.halfdoors;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class HalfDoorsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(HalfDoors.OAK_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.SPRUCE_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.BIRCH_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.JUNGLE_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.ACACIA_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.DARK_OAK_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.MANGROVE_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.CHERRY_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.BAMBOO_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.CRIMSON_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.WARPED_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.IRON_HALFDOOR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(HalfDoors.IRON_FENCE_GATE, ChunkSectionLayer.CUTOUT);
    }
}