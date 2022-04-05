package amymialee.halfdoors;

import amymialee.halfdoors.blocks.DoorcutterBlock;
import amymialee.halfdoors.blocks.HalfDoorBlock;
import amymialee.halfdoors.blocks.IronFenceGateBlock;
import amymialee.halfdoors.client.DoorcutterScreen;
import amymialee.halfdoors.client.DoorcutterScreenHandler;
import amymialee.halfdoors.recipe.DoorcuttingRecipe;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.Blocks.*;

public class Halfdoors implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "halfdoors";
    public static final Random RANDOM = new Random();
    public static final ArrayList<Block> MOD_BLOCKS = new ArrayList<>();

    public static final ItemGroup DOOR_GROUP = FabricItemGroupBuilder.create(id("halfdoor_group"))
            .icon(Halfdoors::getRecipeKindIcon).build();

    public static final Block IRON_HALFDOOR = registerBlock("iron_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5.0f).sounds(BlockSoundGroup.METAL).nonOpaque()));
    public static final Block OAK_HALFDOOR = registerBlock("oak_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, OAK_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block SPRUCE_HALFDOOR = registerBlock("spruce_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, SPRUCE_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block BIRCH_HALFDOOR = registerBlock("birch_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, BIRCH_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block JUNGLE_HALFDOOR = registerBlock("jungle_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, JUNGLE_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block ACACIA_HALFDOOR = registerBlock("acacia_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, ACACIA_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block DARK_OAK_HALFDOOR = registerBlock("dark_oak_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, DARK_OAK_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block CRIMSON_HALFDOOR = registerBlock("crimson_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.NETHER_WOOD, CRIMSON_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block WARPED_HALFDOOR = registerBlock("warped_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.NETHER_WOOD, WARPED_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block DOORCUTTER = registerBlock("doorcutter", new DoorcutterBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.5f).sounds(BlockSoundGroup.STONE).nonOpaque()));
    public static final Block IRON_FENCE_GATE = registerBlock("iron_fence_gate", new IronFenceGateBlock(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5.0f).sounds(BlockSoundGroup.METAL).nonOpaque()));

    public static ScreenHandlerType<DoorcutterScreenHandler> DOOR_CUTTER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, MOD_ID, new ScreenHandlerType<>(DoorcutterScreenHandler::new));
    public static RecipeSerializer<DoorcuttingRecipe> DOOR_CUTTING_RECIPE = RecipeSerializer.register(id("doorcutting").toString(), new DoorcuttingRecipe.OpenSerializer<>(DoorcuttingRecipe::new));
    public static final RecipeType<DoorcuttingRecipe> RECIPE_TYPE = RecipeType.register(Halfdoors.id(Halfdoors.MOD_ID).toString());

    @Override
    public void onInitialize() {}

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(IRON_FENCE_GATE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(IRON_HALFDOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(OAK_HALFDOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SPRUCE_HALFDOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BIRCH_HALFDOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(JUNGLE_HALFDOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ACACIA_HALFDOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DARK_OAK_HALFDOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(CRIMSON_HALFDOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(WARPED_HALFDOOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DOORCUTTER, RenderLayer.getCutout());
        HandledScreens.register(DOOR_CUTTER_SCREEN_HANDLER, DoorcutterScreen::new);
    }

    private static Block registerBlock(String name, Block block) {
        Registry.register(Registry.BLOCK, id(name), block);
        Registry.register(Registry.ITEM, id(name), new BlockItem(block, new FabricItemSettings().group(DOOR_GROUP)));
        MOD_BLOCKS.add(block);
        return block;
    }

    public static ItemStack getRecipeKindIcon() {
        return Halfdoors.MOD_BLOCKS.get(RANDOM.nextInt(Halfdoors.MOD_BLOCKS.size() - 1)).asItem().getDefaultStack();
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
