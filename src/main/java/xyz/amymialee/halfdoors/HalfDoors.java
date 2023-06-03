package xyz.amymialee.halfdoors;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xyz.amymialee.halfdoors.blocks.HalfDoorBlock;
import xyz.amymialee.halfdoors.blocks.IronFenceGateBlock;

public class HalfDoors implements ModInitializer {
    public static final String MOD_ID = "halfdoors";
    public static final Block OAK_HALFDOOR = registerBlock("oak_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR), BlockSetType.OAK));
    public static final Block SPRUCE_HALFDOOR = registerBlock("spruce_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_DOOR), BlockSetType.SPRUCE));
    public static final Block BIRCH_HALFDOOR = registerBlock("birch_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_DOOR), BlockSetType.BIRCH));
    public static final Block JUNGLE_HALFDOOR = registerBlock("jungle_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.JUNGLE_DOOR), BlockSetType.JUNGLE));
    public static final Block ACACIA_HALFDOOR = registerBlock("acacia_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.ACACIA_DOOR), BlockSetType.ACACIA));
    public static final Block DARK_OAK_HALFDOOR = registerBlock("dark_oak_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.DARK_OAK_DOOR), BlockSetType.DARK_OAK));
    public static final Block MANGROVE_HALFDOOR = registerBlock("mangrove_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.MANGROVE_DOOR), BlockSetType.MANGROVE));
    public static final Block CHERRY_HALFDOOR = registerBlock("cherry_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.CHERRY_DOOR), BlockSetType.CHERRY));
    public static final Block BAMBOO_HALFDOOR = registerBlock("bamboo_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.BAMBOO_DOOR), BlockSetType.BAMBOO));
    public static final Block CRIMSON_HALFDOOR = registerBlock("crimson_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.CRIMSON_DOOR), BlockSetType.CRIMSON));
    public static final Block WARPED_HALFDOOR = registerBlock("warped_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.WARPED_DOOR), BlockSetType.WARPED));
    public static final Block IRON_HALFDOOR = registerBlock("iron_halfdoor", new HalfDoorBlock(FabricBlockSettings.copyOf(Blocks.IRON_DOOR), BlockSetType.IRON));
    public static final Block IRON_FENCE_GATE = registerBlock("iron_fence_gate", new IronFenceGateBlock(FabricBlockSettings.copyOf(Blocks.IRON_BARS)));

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> {
            content.addAfter(Items.OAK_DOOR, OAK_HALFDOOR);
            content.addAfter(Items.SPRUCE_DOOR, SPRUCE_HALFDOOR);
            content.addAfter(Items.BIRCH_DOOR, BIRCH_HALFDOOR);
            content.addAfter(Items.JUNGLE_DOOR, JUNGLE_HALFDOOR);
            content.addAfter(Items.ACACIA_DOOR, ACACIA_HALFDOOR);
            content.addAfter(Items.DARK_OAK_DOOR, DARK_OAK_HALFDOOR);
            content.addAfter(Items.MANGROVE_DOOR, MANGROVE_HALFDOOR);
            content.addAfter(Items.CHERRY_DOOR, CHERRY_HALFDOOR);
            content.addAfter(Items.BAMBOO_DOOR, BAMBOO_HALFDOOR);
            content.addAfter(Items.CRIMSON_DOOR, CRIMSON_HALFDOOR);
            content.addAfter(Items.WARPED_DOOR, WARPED_HALFDOOR);
            content.addAfter(Items.IRON_DOOR, IRON_HALFDOOR);
            content.addAfter(Items.IRON_BARS, IRON_FENCE_GATE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
            content.addAfter(Items.IRON_DOOR, OAK_HALFDOOR);
            content.addAfter(OAK_HALFDOOR, IRON_HALFDOOR);
            content.addAfter(Items.OAK_FENCE_GATE, IRON_FENCE_GATE);
        });
    }

    private static Block registerBlock(String name, Block block) {
        Registry.register(Registries.BLOCK, id(name), block);
        Registry.register(Registries.ITEM, id(name), new BlockItem(block, new FabricItemSettings()));
        return block;
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}