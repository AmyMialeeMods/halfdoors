package xyz.amymialee.halfdoors;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import xyz.amymialee.halfdoors.blocks.HalfDoorBlock;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class HalfDoorsDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(HalfDoorsTranslations::new);
        pack.addProvider(HalfDoorsModels::new);
        pack.addProvider(HalfDoorsLootTables::new);
        pack.addProvider(HalfDoorsRecipes::new);
        pack.addProvider(HalfDoorsBlockTags::new);
    }

    private static class HalfDoorsTranslations extends FabricLanguageProvider {
        protected HalfDoorsTranslations(FabricDataOutput dataOutput) {
            super(dataOutput);
        }

        @Override
        public void generateTranslations(TranslationBuilder builder) {
            builder.add("itemGroup.%s.%s_group".formatted(HalfDoors.MOD_ID, HalfDoors.MOD_ID), "Halfdoors");
            builder.add(HalfDoors.OAK_HALFDOOR, "Oak Halfdoor");
            builder.add(HalfDoors.SPRUCE_HALFDOOR, "Spruce Halfdoor");
            builder.add(HalfDoors.BIRCH_HALFDOOR, "Birch Halfdoor");
            builder.add(HalfDoors.JUNGLE_HALFDOOR, "Jungle Halfdoor");
            builder.add(HalfDoors.ACACIA_HALFDOOR, "Acacia Halfdoor");
            builder.add(HalfDoors.DARK_OAK_HALFDOOR, "Dark Oak Halfdoor");
            builder.add(HalfDoors.MANGROVE_HALFDOOR, "Mangrove Halfdoor");
            builder.add(HalfDoors.CHERRY_HALFDOOR, "Cherry Halfdoor");
            builder.add(HalfDoors.BAMBOO_HALFDOOR, "Bamboo Halfdoor");
            builder.add(HalfDoors.CRIMSON_HALFDOOR, "Crimson Halfdoor");
            builder.add(HalfDoors.WARPED_HALFDOOR, "Warped Halfdoor");
            builder.add(HalfDoors.IRON_HALFDOOR, "Iron Halfdoor");
            builder.add(HalfDoors.IRON_FENCE_GATE, "Iron Fence Gate");
        }
    }

    private static class HalfDoorsModels extends FabricModelProvider {
        public static final TextureKey HALFDOOR = TextureKey.of("halfdoor");
        public static final Model TEMPLATE_HALFDOOR_LEFT = new Model(Optional.of(HalfDoors.id("block/template_halfdoor_left")), Optional.of("_left"), HALFDOOR);
        public static final Model TEMPLATE_HALFDOOR_RIGHT = new Model(Optional.of(HalfDoors.id("block/template_halfdoor_right")), Optional.of("_right"), HALFDOOR);

        public HalfDoorsModels(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator generator) {
            this.generateHalfDoor(generator, HalfDoors.OAK_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.SPRUCE_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.BIRCH_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.JUNGLE_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.ACACIA_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.DARK_OAK_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.MANGROVE_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.CHERRY_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.BAMBOO_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.CRIMSON_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.WARPED_HALFDOOR);
            this.generateHalfDoor(generator, HalfDoors.IRON_HALFDOOR);
        }

        private void generateHalfDoor(BlockStateModelGenerator generator, Block block) {
            TextureMap texture = new TextureMap().put(HALFDOOR, TextureMap.getId(block));
            Identifier left = TEMPLATE_HALFDOOR_LEFT.upload(block, texture, generator.modelCollector);
            Identifier right = TEMPLATE_HALFDOOR_RIGHT.upload(block, texture, generator.modelCollector);
            generator.excludeFromSimpleItemModelGeneration(block);
            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(fillDoorVariantMap(left, right)));
        }

        public static BlockStateVariantMap.QuadrupleProperty<Direction, HalfDoorBlock.HalfDoorSection, DoorHinge, Boolean> fillDoorVariantMap(Identifier leftModel, Identifier rightModel) {
            BlockStateVariantMap.QuadrupleProperty<Direction, HalfDoorBlock.HalfDoorSection, DoorHinge, Boolean> variantMap = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, HalfDoorBlock.SECTION, Properties.DOOR_HINGE, Properties.OPEN);
            for (HalfDoorBlock.HalfDoorSection section : HalfDoorBlock.HalfDoorSection.values()) {
                variantMap.register(Direction.EAST, section, DoorHinge.LEFT, false, BlockStateVariant.create().put(VariantSettings.MODEL, leftModel))
                        .register(Direction.SOUTH, section, DoorHinge.LEFT, false, BlockStateVariant.create().put(VariantSettings.MODEL, leftModel).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.WEST, section, DoorHinge.LEFT, false, BlockStateVariant.create().put(VariantSettings.MODEL, leftModel).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                        .register(Direction.NORTH, section, DoorHinge.LEFT, false, BlockStateVariant.create().put(VariantSettings.MODEL, leftModel).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                        .register(Direction.EAST, section, DoorHinge.RIGHT, false, BlockStateVariant.create().put(VariantSettings.MODEL, rightModel))
                        .register(Direction.SOUTH, section, DoorHinge.RIGHT, false, BlockStateVariant.create().put(VariantSettings.MODEL, rightModel).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.WEST, section, DoorHinge.RIGHT, false, BlockStateVariant.create().put(VariantSettings.MODEL, rightModel).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                        .register(Direction.NORTH, section, DoorHinge.RIGHT, false, BlockStateVariant.create().put(VariantSettings.MODEL, rightModel).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                        .register(Direction.EAST, section, DoorHinge.LEFT, true, BlockStateVariant.create().put(VariantSettings.MODEL, rightModel).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.SOUTH, section, DoorHinge.LEFT, true, BlockStateVariant.create().put(VariantSettings.MODEL, rightModel).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                        .register(Direction.WEST, section, DoorHinge.LEFT, true, BlockStateVariant.create().put(VariantSettings.MODEL, rightModel).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                        .register(Direction.NORTH, section, DoorHinge.LEFT, true, BlockStateVariant.create().put(VariantSettings.MODEL, rightModel))
                        .register(Direction.EAST, section, DoorHinge.RIGHT, true, BlockStateVariant.create().put(VariantSettings.MODEL, leftModel).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                        .register(Direction.SOUTH, section, DoorHinge.RIGHT, true, BlockStateVariant.create().put(VariantSettings.MODEL, leftModel))
                        .register(Direction.WEST, section, DoorHinge.RIGHT, true, BlockStateVariant.create().put(VariantSettings.MODEL, leftModel).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.NORTH, section, DoorHinge.RIGHT, true, BlockStateVariant.create().put(VariantSettings.MODEL, leftModel).put(VariantSettings.Y, VariantSettings.Rotation.R180));
            }
            return variantMap;
        }

        @Override
        public void generateItemModels(ItemModelGenerator generator) {
            generator.register(HalfDoors.OAK_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.SPRUCE_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.BIRCH_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.JUNGLE_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.ACACIA_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.DARK_OAK_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.MANGROVE_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.CHERRY_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.BAMBOO_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.CRIMSON_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.WARPED_HALFDOOR.asItem(), Models.GENERATED);
            generator.register(HalfDoors.IRON_HALFDOOR.asItem(), Models.GENERATED);
        }
    }

    private static class HalfDoorsLootTables extends FabricBlockLootTableProvider {
        protected HalfDoorsLootTables(FabricDataOutput dataOutput) {
            super(dataOutput);
        }

        @Override
        public void generate() {
            this.addDrop(HalfDoors.OAK_HALFDOOR);
            this.addDrop(HalfDoors.SPRUCE_HALFDOOR);
            this.addDrop(HalfDoors.BIRCH_HALFDOOR);
            this.addDrop(HalfDoors.JUNGLE_HALFDOOR);
            this.addDrop(HalfDoors.ACACIA_HALFDOOR);
            this.addDrop(HalfDoors.DARK_OAK_HALFDOOR);
            this.addDrop(HalfDoors.MANGROVE_HALFDOOR);
            this.addDrop(HalfDoors.CHERRY_HALFDOOR);
            this.addDrop(HalfDoors.BAMBOO_HALFDOOR);
            this.addDrop(HalfDoors.CRIMSON_HALFDOOR);
            this.addDrop(HalfDoors.WARPED_HALFDOOR);
            this.addDrop(HalfDoors.IRON_HALFDOOR);
            this.addDrop(HalfDoors.IRON_FENCE_GATE);
        }
    }

    private static class HalfDoorsRecipes extends FabricRecipeProvider {
        public HalfDoorsRecipes(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate(Consumer<RecipeJsonProvider> exporter) {
            this.generateHalfDoor(exporter, Items.OAK_DOOR, HalfDoors.OAK_HALFDOOR);
            this.generateHalfDoor(exporter, Items.SPRUCE_DOOR, HalfDoors.SPRUCE_HALFDOOR);
            this.generateHalfDoor(exporter, Items.BIRCH_DOOR, HalfDoors.BIRCH_HALFDOOR);
            this.generateHalfDoor(exporter, Items.JUNGLE_DOOR, HalfDoors.JUNGLE_HALFDOOR);
            this.generateHalfDoor(exporter, Items.ACACIA_DOOR, HalfDoors.ACACIA_HALFDOOR);
            this.generateHalfDoor(exporter, Items.DARK_OAK_DOOR, HalfDoors.DARK_OAK_HALFDOOR);
            this.generateHalfDoor(exporter, Items.MANGROVE_DOOR, HalfDoors.MANGROVE_HALFDOOR);
            this.generateHalfDoor(exporter, Items.CHERRY_DOOR, HalfDoors.CHERRY_HALFDOOR);
            this.generateHalfDoor(exporter, Items.BAMBOO_DOOR, HalfDoors.BAMBOO_HALFDOOR);
            this.generateHalfDoor(exporter, Items.CRIMSON_DOOR, HalfDoors.CRIMSON_HALFDOOR);
            this.generateHalfDoor(exporter, Items.WARPED_DOOR, HalfDoors.WARPED_HALFDOOR);
            this.generateHalfDoor(exporter, Items.IRON_DOOR, HalfDoors.IRON_HALFDOOR);
            this.generateFenceGate(exporter, Items.IRON_NUGGET, Items.IRON_INGOT, HalfDoors.IRON_FENCE_GATE);
        }

        private void generateHalfDoor(Consumer<RecipeJsonProvider> exporter, ItemConvertible door, ItemConvertible halfDoor) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, halfDoor, 6).input('d', door).pattern("ddd").criterion("has_door", conditionsFromItem(door)).offerTo(exporter);
        }

        private void generateFenceGate(Consumer<RecipeJsonProvider> exporter, ItemConvertible sides, ItemConvertible gate, ItemConvertible fenceGate) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, fenceGate, 1).input('s', sides).input('g', gate).pattern("sgs").pattern("sgs").criterion("has_sides", conditionsFromItem(sides)).criterion("has_gate", conditionsFromItem(gate)).offerTo(exporter);
        }
    }

    private static class HalfDoorsBlockTags extends FabricTagProvider.BlockTagProvider {
        public HalfDoorsBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            this.getOrCreateTagBuilder(BlockTags.WOODEN_DOORS).add(
                    HalfDoors.OAK_HALFDOOR,
                    HalfDoors.SPRUCE_HALFDOOR,
                    HalfDoors.BIRCH_HALFDOOR,
                    HalfDoors.JUNGLE_HALFDOOR,
                    HalfDoors.ACACIA_HALFDOOR,
                    HalfDoors.DARK_OAK_HALFDOOR,
                    HalfDoors.MANGROVE_HALFDOOR,
                    HalfDoors.CHERRY_HALFDOOR,
                    HalfDoors.BAMBOO_HALFDOOR,
                    HalfDoors.CRIMSON_HALFDOOR,
                    HalfDoors.WARPED_HALFDOOR
            );
            this.getOrCreateTagBuilder(BlockTags.DOORS).add(
                    HalfDoors.IRON_HALFDOOR
            );
            this.getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(
                    HalfDoors.IRON_FENCE_GATE
            );
            this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(
                    HalfDoors.OAK_HALFDOOR,
                    HalfDoors.SPRUCE_HALFDOOR,
                    HalfDoors.BIRCH_HALFDOOR,
                    HalfDoors.JUNGLE_HALFDOOR,
                    HalfDoors.ACACIA_HALFDOOR,
                    HalfDoors.DARK_OAK_HALFDOOR,
                    HalfDoors.MANGROVE_HALFDOOR,
                    HalfDoors.CHERRY_HALFDOOR,
                    HalfDoors.BAMBOO_HALFDOOR,
                    HalfDoors.CRIMSON_HALFDOOR,
                    HalfDoors.WARPED_HALFDOOR
            );
            this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(
                    HalfDoors.IRON_HALFDOOR,
                    HalfDoors.IRON_FENCE_GATE
            );
        }
    }
}