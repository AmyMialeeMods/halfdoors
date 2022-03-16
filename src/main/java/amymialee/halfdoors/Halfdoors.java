package amymialee.halfdoors;

import amymialee.halfdoors.blocks.HalfDoorBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.block.Blocks.*;

public class Halfdoors implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "halfdoors";

    public static final Block OAK_DOOR = registerBlock("oak_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, OAK_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block SPRUCE_DOOR = registerBlock("spruce_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, SPRUCE_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block BIRCH_DOOR = registerBlock("birch_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, BIRCH_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block JUNGLE_DOOR = registerBlock("jungle_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, JUNGLE_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block ACACIA_DOOR = registerBlock("acacia_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, ACACIA_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block DARK_OAK_DOOR = registerBlock("dark_oak_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, DARK_OAK_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block CRIMSON_DOOR = registerBlock("crimson_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.NETHER_WOOD, CRIMSON_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block WARPED_DOOR = registerBlock("warped_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.NETHER_WOOD, WARPED_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block IRON_DOOR = registerBlock("iron_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5.0f).sounds(BlockSoundGroup.METAL).nonOpaque()));

    @Override
    public void onInitialize() {}

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.OAK_DOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SPRUCE_DOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BIRCH_DOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.JUNGLE_DOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ACACIA_DOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.DARK_OAK_DOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CRIMSON_DOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.WARPED_DOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.IRON_DOOR, RenderLayer.getTranslucent());
    }

    private static Block registerBlock(String id, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, id), block);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, id), new BlockItem(block, new FabricItemSettings().group(ItemGroup.REDSTONE)));
        return block;
    }
}
