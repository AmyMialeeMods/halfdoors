package amymialee.halfdoors;

import amymialee.halfdoors.blocks.DoorcutterBlock;
import amymialee.halfdoors.blocks.HalfDoorBlock;
import amymialee.halfdoors.blocks.IronFenceGateBlock;
import amymialee.halfdoors.screens.DoorcutterScreenHandler;
import amymialee.halfdoors.screens.LauncherScreenHandler;
import amymialee.halfdoors.items.DoorFlipperItem;
import amymialee.halfdoors.entities.TinyDoorEntity;
import amymialee.halfdoors.items.DoorLauncherItem;
import amymialee.halfdoors.entities.DoorbladeEntity;
import amymialee.halfdoors.recipe.DoorSmithingRecipe;
import amymialee.halfdoors.recipe.DoorcuttingRecipe;
import amymialee.halfdoors.util.DoorControls;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@SuppressWarnings("unused")
public class Halfdoors implements ModInitializer {
    public static final String MOD_ID = "halfdoors";
    public static final Random RANDOM = new Random();
    public static final ArrayList<Item> MOD_ITEMS = new ArrayList<>();
    public static final ItemGroup DOOR_GROUP = FabricItemGroupBuilder.create(id("halfdoor_group")).icon(Halfdoors::getRecipeKindIcon).build();

    public static final Block IRON_HALFDOOR = registerBlock("iron_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5.0f).sounds(BlockSoundGroup.METAL).nonOpaque()));
    public static final Block OAK_HALFDOOR = registerBlock("oak_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, Blocks.OAK_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block SPRUCE_HALFDOOR = registerBlock("spruce_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, Blocks.SPRUCE_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block BIRCH_HALFDOOR = registerBlock("birch_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, Blocks.BIRCH_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block JUNGLE_HALFDOOR = registerBlock("jungle_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, Blocks.JUNGLE_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block ACACIA_HALFDOOR = registerBlock("acacia_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, Blocks.ACACIA_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block DARK_OAK_HALFDOOR = registerBlock("dark_oak_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.WOOD, Blocks.DARK_OAK_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block CRIMSON_HALFDOOR = registerBlock("crimson_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.NETHER_WOOD, Blocks.CRIMSON_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block WARPED_HALFDOOR = registerBlock("warped_halfdoor", new HalfDoorBlock(FabricBlockSettings.of(Material.NETHER_WOOD, Blocks.WARPED_PLANKS.getDefaultMapColor()).strength(3.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block DOORCUTTER = registerBlock("doorcutter", new DoorcutterBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.5f).sounds(BlockSoundGroup.STONE).nonOpaque()));
    public static final Block IRON_FENCE_GATE = registerBlock("iron_fence_gate", new IronFenceGateBlock(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5.0f).sounds(BlockSoundGroup.METAL).nonOpaque()));

    public static ScreenHandlerType<DoorcutterScreenHandler> DOOR_CUTTER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, MOD_ID, new ScreenHandlerType<>(DoorcutterScreenHandler::new));
    public static RecipeSerializer<DoorcuttingRecipe> DOOR_CUTTING_RECIPE = RecipeSerializer.register(id("doorcutting").toString(), new DoorcuttingRecipe.OpenSerializer<>(DoorcuttingRecipe::new));
    public static RecipeSerializer<DoorSmithingRecipe> DOOR_SMITHING_RECIPE = RecipeSerializer.register(id("smithing").toString(), new DoorSmithingRecipe.OpenSerializer());
    public static final RecipeType<DoorcuttingRecipe> RECIPE_TYPE = RecipeType.register(Halfdoors.id(Halfdoors.MOD_ID).toString());

    public static final Item DOOR_LAUNCHER = registerItem("door_launcher", new DoorLauncherItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(DOOR_GROUP)));
    public static final EntityType<DoorbladeEntity> DOORBLADE_ENTITY = Registry.register(Registry.ENTITY_TYPE, id("door_saw"), FabricEntityTypeBuilder.<DoorbladeEntity>create(SpawnGroup.MISC, DoorbladeEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final ScreenHandlerType<LauncherScreenHandler> LAUNCHER_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, "single_slot", new ScreenHandlerType<>((a, b) -> new LauncherScreenHandler(a, b, DOOR_LAUNCHER.getDefaultStack().copy())));
    public static SoundEvent DOORBLADE_HIT_GROUND = Registry.register(Registry.SOUND_EVENT, id("item.doorblade.hit_ground"), new SoundEvent(id("item.doorblade.hit_ground")));
    public static SoundEvent DOOR_LAUNCHER_FIRE = Registry.register(Registry.SOUND_EVENT, id("item.doorlauncher.fire"), new SoundEvent(id("item.doorlauncher.fire")));

    public static final Item DOOR_FLIPPER = registerItem("door_flipper", new DoorFlipperItem(new FabricItemSettings().maxCount(1).maxDamage(100).rarity(Rarity.RARE).group(DOOR_GROUP)));
    public static final EntityType<TinyDoorEntity> TINY_DOOR_ENTITY = Registry.register(Registry.ENTITY_TYPE, id("tiny_door"), FabricEntityTypeBuilder.<TinyDoorEntity>create(SpawnGroup.MISC, TinyDoorEntity::new).dimensions(EntityDimensions.fixed(0.1F, 0.1F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static SoundEvent DOOR_FLIP = Registry.register(Registry.SOUND_EVENT, id("item.doorflipper.flip"), new SoundEvent(id("item.doorflipper.flip")));
    public static final Item GOLD_DOOR_NUGGET = registerItem("gold_door_nugget", new Item(new FabricItemSettings().group(DOOR_GROUP)));

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(DoorControls.FLIP, (server, playerEntity, playNetworkHandler, packetByteBuf, packetSender) -> {
            if (playerEntity.getItemCooldownManager().isCoolingDown(DOOR_FLIPPER)) {
                return;
            }
            Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(playerEntity);
            if (optional.isPresent() && optional.get().isEquipped(Halfdoors.DOOR_FLIPPER)) {
                for (Pair<SlotReference, ItemStack> pair : optional.get().getEquipped(Halfdoors.DOOR_FLIPPER)) {
                    if (DoorFlipperItem.readAmmo(pair.getRight()) > 0) {
                        ((DoorFlipperItem) DOOR_FLIPPER).toss(playerEntity, pair.getRight());
                        return;
                    }
                }
            }
        });
    }

    private static Block registerBlock(String name, Block block) {
        Registry.register(Registry.BLOCK, id(name), block);
        registerItem(name, new BlockItem(block, new FabricItemSettings().group(DOOR_GROUP)));
        return block;
    }

    public static Item registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, id(name), item);
        MOD_ITEMS.add(item);
        return item;
    }

    public static ItemStack getRecipeKindIcon() {
        return MOD_ITEMS.get(RANDOM.nextInt(MOD_ITEMS.size() - 1)).getDefaultStack();
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
