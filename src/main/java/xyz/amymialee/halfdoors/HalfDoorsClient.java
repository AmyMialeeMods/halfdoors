package xyz.amymialee.halfdoors;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class HalfDoorsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (var block : HalfDoors.ALL_BLOCKS) {
            BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.CUTOUT);
        }
    }
}