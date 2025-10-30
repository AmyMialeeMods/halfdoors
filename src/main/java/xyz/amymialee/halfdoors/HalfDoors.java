package xyz.amymialee.halfdoors;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopperBlocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import xyz.amymialee.halfdoors.blocks.HalfDoorBlock;
import xyz.amymialee.halfdoors.blocks.IronFenceGateBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HalfDoors implements ModInitializer {
    public static final String MOD_ID = "halfdoors";
    public static final CreativeModeTab HALFDOORS_GROUP = FabricItemGroup.builder().title(Component.translatable("itemGroup.%s.%s_group".formatted(MOD_ID, MOD_ID))).icon(HalfDoors::getRecipeKindIcon).build();
    public static final List<Block> ALL_BLOCKS = new ArrayList<>();
    public static final List<Block> ALL_HALFDOORS = new ArrayList<>();
    public static final Block OAK_HALFDOOR = registerBlock("oak_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR), p -> new HalfDoorBlock(p, BlockSetType.OAK));
    public static final Block SPRUCE_HALFDOOR = registerBlock("spruce_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_DOOR), p -> new HalfDoorBlock(p, BlockSetType.SPRUCE));
    public static final Block BIRCH_HALFDOOR = registerBlock("birch_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_DOOR), p -> new HalfDoorBlock(p, BlockSetType.BIRCH));
    public static final Block JUNGLE_HALFDOOR = registerBlock("jungle_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_DOOR), p -> new HalfDoorBlock(p, BlockSetType.JUNGLE));
    public static final Block ACACIA_HALFDOOR = registerBlock("acacia_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_DOOR), p -> new HalfDoorBlock(p, BlockSetType.ACACIA));
    public static final Block DARK_OAK_HALFDOOR = registerBlock("dark_oak_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_DOOR), p -> new HalfDoorBlock(p, BlockSetType.DARK_OAK));
    public static final Block MANGROVE_HALFDOOR = registerBlock("mangrove_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_DOOR), p -> new HalfDoorBlock(p, BlockSetType.MANGROVE));
    public static final Block CHERRY_HALFDOOR = registerBlock("cherry_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_DOOR), p -> new HalfDoorBlock(p, BlockSetType.CHERRY));
    public static final Block BAMBOO_HALFDOOR = registerBlock("bamboo_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_DOOR), p -> new HalfDoorBlock(p, BlockSetType.BAMBOO));
    public static final Block CRIMSON_HALFDOOR = registerBlock("crimson_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_DOOR), p -> new HalfDoorBlock(p, BlockSetType.CRIMSON));
    public static final Block WARPED_HALFDOOR = registerBlock("warped_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_DOOR), p -> new HalfDoorBlock(p, BlockSetType.WARPED));
    public static final Block IRON_HALFDOOR = registerBlock("iron_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR), p -> new HalfDoorBlock(p, BlockSetType.IRON));
    public static final Block PALE_OAK_HALFDOOR = registerBlock("pale_oak_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_DOOR), p -> new HalfDoorBlock(p, BlockSetType.PALE_OAK));
    public static final Block COPPER_HALFDOOR = registerBlock("copper_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_DOOR), p -> new HalfDoorBlock(p, BlockSetType.COPPER));
    public static final Block EXPOSED_COPPER_HALFDOOR = registerBlock("exposed_copper_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.EXPOSED_COPPER_DOOR), p -> new HalfDoorBlock(p, BlockSetType.COPPER));
    public static final Block WEATHERED_COPPER_HALFDOOR = registerBlock("weathered_copper_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.WEATHERED_COPPER_DOOR), p -> new HalfDoorBlock(p, BlockSetType.COPPER));
    public static final Block OXIDIZED_COPPER_HALFDOOR = registerBlock("oxidized_copper_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.OXIDIZED_COPPER_DOOR), p -> new HalfDoorBlock(p, BlockSetType.COPPER));
    public static final Block WAXED_COPPER_HALFDOOR = registerBlock("waxed_copper_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_COPPER_DOOR), p -> new HalfDoorBlock(p, BlockSetType.COPPER));
    public static final Block WAXED_EXPOSED_COPPER_HALFDOOR = registerBlock("waxed_exposed_copper_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_EXPOSED_COPPER_DOOR), p -> new HalfDoorBlock(p, BlockSetType.COPPER));
    public static final Block WAXED_WEATHERED_COPPER_HALFDOOR = registerBlock("waxed_weathered_copper_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_WEATHERED_COPPER_DOOR), p -> new HalfDoorBlock(p, BlockSetType.COPPER));
    public static final Block WAXED_OXIDIZED_COPPER_HALFDOOR = registerBlock("waxed_oxidized_copper_halfdoor", BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_OXIDIZED_COPPER_DOOR), p -> new HalfDoorBlock(p, BlockSetType.COPPER));
    public static final Block IRON_FENCE_GATE = registerBlock("iron_fence_gate", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.METAL), IronFenceGateBlock::new);

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id(MOD_ID), HALFDOORS_GROUP);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(content -> {
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
            content.addAfter(Items.PALE_OAK_DOOR, PALE_OAK_HALFDOOR);
            content.addAfter(Items.COPPER_DOOR, COPPER_HALFDOOR);
            content.addAfter(Items.EXPOSED_COPPER_DOOR, EXPOSED_COPPER_HALFDOOR);
            content.addAfter(Items.WEATHERED_COPPER_DOOR, WEATHERED_COPPER_HALFDOOR);
            content.addAfter(Items.OXIDIZED_COPPER_DOOR, OXIDIZED_COPPER_HALFDOOR);
            content.addAfter(Items.WAXED_COPPER_DOOR, WAXED_COPPER_HALFDOOR);
            content.addAfter(Items.WAXED_EXPOSED_COPPER_DOOR, WAXED_EXPOSED_COPPER_HALFDOOR);
            content.addAfter(Items.WAXED_WEATHERED_COPPER_DOOR, WAXED_WEATHERED_COPPER_HALFDOOR);
            content.addAfter(Items.WAXED_OXIDIZED_COPPER_DOOR, WAXED_OXIDIZED_COPPER_HALFDOOR);
            content.addAfter(Items.IRON_BARS, IRON_FENCE_GATE);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(content -> {
            content.addAfter(Items.IRON_DOOR, OAK_HALFDOOR);
            content.addAfter(OAK_HALFDOOR, IRON_HALFDOOR);
            content.addAfter(Items.OAK_FENCE_GATE, IRON_FENCE_GATE);
        });
        BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(HALFDOORS_GROUP).ifPresent(key -> ItemGroupEvents.modifyEntriesEvent(key).register(content -> {
            for (var block : ALL_BLOCKS) {
                content.accept(block);
            }
        }));
        OxidizableBlocksRegistry.registerCopperBlockSet(new WeatheringCopperBlocks(
                COPPER_HALFDOOR,
                EXPOSED_COPPER_HALFDOOR,
                WEATHERED_COPPER_HALFDOOR,
                OXIDIZED_COPPER_HALFDOOR,
                WAXED_COPPER_HALFDOOR,
                WAXED_EXPOSED_COPPER_HALFDOOR,
                WAXED_WEATHERED_COPPER_HALFDOOR,
                WAXED_OXIDIZED_COPPER_HALFDOOR
        ));
    }

    private static Block registerBlock(String name, BlockBehaviour.Properties properties, Function<BlockBehaviour.Properties, Block> factory) {
        var id = ResourceKey.create(Registries.BLOCK, id(name));
        var block = Blocks.register(id, factory, properties);
        Items.registerBlock(block);
        if (block instanceof HalfDoorBlock) {
            ALL_HALFDOORS.add(block);
        }
        ALL_BLOCKS.add(block);
        return block;
    }

    public static ItemStack getRecipeKindIcon() {
        return OAK_HALFDOOR.asItem().getDefaultInstance();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}