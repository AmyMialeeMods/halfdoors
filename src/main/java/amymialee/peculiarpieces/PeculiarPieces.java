package amymialee.peculiarpieces;

import amymialee.peculiarpieces.blockentities.WarpBlockEntity;
import amymialee.peculiarpieces.blocks.CheckpointBlock;
import amymialee.peculiarpieces.blocks.CheckpointRemoverBlock;
import amymialee.peculiarpieces.blocks.CheckpointReturnerBlock;
import amymialee.peculiarpieces.blocks.InvisiblePressurePlate;
import amymialee.peculiarpieces.blocks.WarpBlock;
import amymialee.peculiarpieces.items.BlazingGlidersItem;
import amymialee.peculiarpieces.items.CheckpointPearlItem;
import amymialee.peculiarpieces.items.ConsumablePositionPearlItem;
import amymialee.peculiarpieces.items.FlightRingItem;
import amymialee.peculiarpieces.items.MountingStickItem;
import amymialee.peculiarpieces.items.PositionPearlItem;
import amymialee.peculiarpieces.items.SlipperyShoesItem;
import amymialee.peculiarpieces.items.TransportPearlItem;
import amymialee.peculiarpieces.screens.WarpScreenHandler;
import amymialee.peculiarpieces.util.WarpManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "unused"})
public class PeculiarPieces implements ModInitializer {
    public static final String MOD_ID = "peculiarpieces";
    public static final Random RANDOM = new Random();
    public static final ArrayList<Item> MOD_ITEMS = new ArrayList<>();
    public static final ItemGroup PIECES_GROUP = FabricItemGroupBuilder.create(id("peculiarpieces_group")).icon(PeculiarPieces::getRecipeKindIcon).build();
    public static boolean visible = false;

    public static final Block CHECKPOINT = registerBlock("checkpoint", new BlockItem(new CheckpointBlock(FabricBlockSettings.of(Material.STRUCTURE_VOID).noCollision()), new FabricItemSettings().rarity(Rarity.EPIC).group(PIECES_GROUP)));
    public static final Block CHECKPOINT_REMOVER = registerBlock("checkpoint_remover", new BlockItem(new CheckpointRemoverBlock(FabricBlockSettings.of(Material.STRUCTURE_VOID).noCollision()), new FabricItemSettings().rarity(Rarity.EPIC).group(PIECES_GROUP)));
    public static final Block CHECKPOINT_RETURNER = registerBlock("checkpoint_returner", new BlockItem(new CheckpointReturnerBlock(FabricBlockSettings.of(Material.STRUCTURE_VOID).noCollision()), new FabricItemSettings().rarity(Rarity.EPIC).group(PIECES_GROUP)));
    public static final Item CHECKPOINT_PEARL = registerItem("checkpoint_pearl", new CheckpointPearlItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(PIECES_GROUP)));

    public static final ScreenHandlerType<WarpScreenHandler> WARP_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, "warp_block", new ScreenHandlerType<>(WarpScreenHandler::new));
    public static final Block WARP_BLOCK = registerBlock("warp_block", new WarpBlock(FabricBlockSettings.copy(Blocks.LODESTONE)));
    public static BlockEntityType<WarpBlockEntity> WARP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "warp_block", FabricBlockEntityTypeBuilder.create(WarpBlockEntity::new, WARP_BLOCK).build(null));

    public static final Item TRANS_PEARL = registerItem("transport_pearl", new TransportPearlItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(PIECES_GROUP)));
    public static final Item POS_PEARL = registerItem("position_pearl", new PositionPearlItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(PIECES_GROUP)));
    public static final Item CONSUMABLE_POS_PEARL = registerItem("consumable_position_pearl", new ConsumablePositionPearlItem(new FabricItemSettings().rarity(Rarity.UNCOMMON).group(PIECES_GROUP)));
    public static final Item MOUNTING_STICK = registerItem("mounting_stick", new MountingStickItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(PIECES_GROUP)));
    public static final TagKey<EntityType<?>> MOUNT_BLACKLIST = TagKey.of(Registry.ENTITY_TYPE_KEY, id("mount_blacklist"));
    public static final Item FLIGHT_RING = registerItem("flight_ring", new FlightRingItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(PIECES_GROUP)));

    public static final Block INVISIBLE_PLATE_LIGHT = registerBlock("invisible_plate_light", new InvisiblePressurePlate(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.of(Material.WOOD, Blocks.TINTED_GLASS.getDefaultMapColor()).noCollision().strength(0.5F).sounds(BlockSoundGroup.GLASS)));
    public static final Block INVISIBLE_PLATE_HEAVY = registerBlock("invisible_plate_heavy", new InvisiblePressurePlate(PressurePlateBlock.ActivationRule.MOBS, FabricBlockSettings.of(Material.STONE, Blocks.TINTED_GLASS.getDefaultMapColor()).noCollision().strength(0.5F).sounds(BlockSoundGroup.GLASS)));

    public static final Block SLIPPERY_STONE = registerBlock("slippery_stone", new Block(FabricBlockSettings.of(Material.STONE).requiresTool().strength(1.25F, 4.0F).slipperiness(1f / 0.91f)));
    public static final Item SLIPPERY_SHOES = registerItem("slippery_shoes", new SlipperyShoesItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(PIECES_GROUP)));
    public static final Item BLAZING_GLIDERS = registerItem("blazing_gliders", new BlazingGlidersItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(PIECES_GROUP)));

    public static final TagKey<Block> WARP_BINDABLE = TagKey.of(Registry.BLOCK_KEY, id("warp_bindable"));

    @Override
    public void onInitialize() {
        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> WarpManager.tick());
    }

    private static Block registerBlock(String name, Block block) {
        return registerBlock(name, new BlockItem(block, new FabricItemSettings().group(PIECES_GROUP)));
    }

    private static Block registerBlock(String name, BlockItem block) {
        Registry.register(Registry.BLOCK, id(name), block.getBlock());
        registerItem(name, block);
        return block.getBlock();
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
