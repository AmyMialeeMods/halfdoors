package xyz.amymialee.halfdoors;

import com.mojang.math.Quadrant;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import xyz.amymialee.halfdoors.blocks.HalfDoorBlock;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.client.data.models.BlockModelGenerators.plainModel;
import static net.minecraft.client.data.models.BlockModelGenerators.variant;

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
        protected HalfDoorsTranslations(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider lookup, TranslationBuilder builder) {
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
        public static final TextureSlot HALFDOOR = TextureSlot.create("halfdoor");
        public static final ModelTemplate TEMPLATE_HALFDOOR_LEFT = new ModelTemplate(Optional.of(HalfDoors.id("block/template_halfdoor_left")), Optional.of("_left"), HALFDOOR);
        public static final ModelTemplate TEMPLATE_HALFDOOR_RIGHT = new ModelTemplate(Optional.of(HalfDoors.id("block/template_halfdoor_right")), Optional.of("_right"), HALFDOOR);

        public HalfDoorsModels(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators generator) {
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

        private void generateHalfDoor(BlockModelGenerators generator, Block block) {
            TextureMapping texture = new TextureMapping().put(HALFDOOR, TextureMapping.getBlockTexture(block));
            ResourceLocation left = TEMPLATE_HALFDOOR_LEFT.create(block, texture, generator.modelOutput);
            ResourceLocation right = TEMPLATE_HALFDOOR_RIGHT.create(block, texture, generator.modelOutput);
            generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(fillDoorVariantMap(left, right)));
        }

        public static PropertyDispatch.C4<MultiVariant, Direction, HalfDoorBlock.HalfDoorSection, DoorHingeSide, Boolean> fillDoorVariantMap(ResourceLocation leftModel, ResourceLocation rightModel) {
            PropertyDispatch.C4<MultiVariant, Direction, HalfDoorBlock.HalfDoorSection, DoorHingeSide, Boolean> variantMap = PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, HalfDoorBlock.SECTION, BlockStateProperties.DOOR_HINGE, BlockStateProperties.OPEN);
            for (HalfDoorBlock.HalfDoorSection section : HalfDoorBlock.HalfDoorSection.values()) {
                variantMap
                        .select(Direction.EAST, section, DoorHingeSide.LEFT, false, variant(plainModel(leftModel)))
                        .select(Direction.SOUTH, section, DoorHingeSide.LEFT, false, variant(plainModel(leftModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
                        .select(Direction.WEST, section, DoorHingeSide.LEFT, false, variant(plainModel(leftModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R180)))
                        .select(Direction.NORTH, section, DoorHingeSide.LEFT, false, variant(plainModel(leftModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R270)))
                        .select(Direction.EAST, section, DoorHingeSide.RIGHT, false, variant(plainModel(rightModel)))
                        .select(Direction.SOUTH, section, DoorHingeSide.RIGHT, false, variant(plainModel(rightModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
                        .select(Direction.WEST, section, DoorHingeSide.RIGHT, false, variant(plainModel(rightModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R180)))
                        .select(Direction.NORTH, section, DoorHingeSide.RIGHT, false, variant(plainModel(rightModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R270)))
                        .select(Direction.EAST, section, DoorHingeSide.LEFT, true, variant(plainModel(rightModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
                        .select(Direction.SOUTH, section, DoorHingeSide.LEFT, true, variant(plainModel(rightModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R180)))
                        .select(Direction.WEST, section, DoorHingeSide.LEFT, true, variant(plainModel(rightModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R270)))
                        .select(Direction.NORTH, section, DoorHingeSide.LEFT, true, variant(plainModel(rightModel)))
                        .select(Direction.EAST, section, DoorHingeSide.RIGHT, true, variant(plainModel(leftModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R270)))
                        .select(Direction.SOUTH, section, DoorHingeSide.RIGHT, true, variant(plainModel(leftModel)))
                        .select(Direction.WEST, section, DoorHingeSide.RIGHT, true, variant(plainModel(leftModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
                        .select(Direction.NORTH, section, DoorHingeSide.RIGHT, true, variant(plainModel(leftModel)).with(VariantMutator.Y_ROT.withValue(Quadrant.R180)));
            }
            return variantMap;
        }

        @Override
        public void generateItemModels(ItemModelGenerators generator) {
            generator.generateFlatItem(HalfDoors.OAK_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.SPRUCE_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.BIRCH_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.JUNGLE_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.ACACIA_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.DARK_OAK_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.MANGROVE_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.CHERRY_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.BAMBOO_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.CRIMSON_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.WARPED_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
            generator.generateFlatItem(HalfDoors.IRON_HALFDOOR.asItem(), ModelTemplates.FLAT_ITEM);
        }
    }

    private static class HalfDoorsLootTables extends FabricBlockLootTableProvider {
        protected HalfDoorsLootTables(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate() {
            this.dropSelf(HalfDoors.OAK_HALFDOOR);
            this.dropSelf(HalfDoors.SPRUCE_HALFDOOR);
            this.dropSelf(HalfDoors.BIRCH_HALFDOOR);
            this.dropSelf(HalfDoors.JUNGLE_HALFDOOR);
            this.dropSelf(HalfDoors.ACACIA_HALFDOOR);
            this.dropSelf(HalfDoors.DARK_OAK_HALFDOOR);
            this.dropSelf(HalfDoors.MANGROVE_HALFDOOR);
            this.dropSelf(HalfDoors.CHERRY_HALFDOOR);
            this.dropSelf(HalfDoors.BAMBOO_HALFDOOR);
            this.dropSelf(HalfDoors.CRIMSON_HALFDOOR);
            this.dropSelf(HalfDoors.WARPED_HALFDOOR);
            this.dropSelf(HalfDoors.IRON_HALFDOOR);
            this.dropSelf(HalfDoors.IRON_FENCE_GATE);
        }
    }

    private static class HalfDoorsRecipes extends FabricRecipeProvider {
        public HalfDoorsRecipes(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
            return new RecipeProvider(registryLookup, exporter) {
                private final HolderLookup.RegistryLookup<Item> items = registryLookup.lookupOrThrow(Registries.ITEM);

                private void generateHalfDoor(ItemLike door, ItemLike halfDoor) {
                    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.REDSTONE, halfDoor, 6).define('d', door).pattern("ddd").unlockedBy("has_door", this.has(door)).save(exporter);
                }

                private void generateFenceGate(ItemLike sides, ItemLike gate, ItemLike fenceGate) {
                    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.REDSTONE, fenceGate, 1).define('s', sides).define('g', gate).pattern("sgs").pattern("sgs").unlockedBy("has_sides", this.has(sides)).unlockedBy("has_gate", this.has(gate)).save(exporter);
                }

                @Override
                public void buildRecipes() {
                    this.generateHalfDoor(Items.OAK_DOOR, HalfDoors.OAK_HALFDOOR);
                    this.generateHalfDoor(Items.SPRUCE_DOOR, HalfDoors.SPRUCE_HALFDOOR);
                    this.generateHalfDoor(Items.BIRCH_DOOR, HalfDoors.BIRCH_HALFDOOR);
                    this.generateHalfDoor(Items.JUNGLE_DOOR, HalfDoors.JUNGLE_HALFDOOR);
                    this.generateHalfDoor(Items.ACACIA_DOOR, HalfDoors.ACACIA_HALFDOOR);
                    this.generateHalfDoor(Items.DARK_OAK_DOOR, HalfDoors.DARK_OAK_HALFDOOR);
                    this.generateHalfDoor(Items.MANGROVE_DOOR, HalfDoors.MANGROVE_HALFDOOR);
                    this.generateHalfDoor(Items.CHERRY_DOOR, HalfDoors.CHERRY_HALFDOOR);
                    this.generateHalfDoor(Items.BAMBOO_DOOR, HalfDoors.BAMBOO_HALFDOOR);
                    this.generateHalfDoor(Items.CRIMSON_DOOR, HalfDoors.CRIMSON_HALFDOOR);
                    this.generateHalfDoor(Items.WARPED_DOOR, HalfDoors.WARPED_HALFDOOR);
                    this.generateHalfDoor(Items.IRON_DOOR, HalfDoors.IRON_HALFDOOR);
                    this.generateFenceGate(Items.IRON_NUGGET, Items.IRON_INGOT, HalfDoors.IRON_FENCE_GATE);
                }
            };
        }

        @Override
        public String getName() {
            return "halfdoor recipes";
        }
    }

    private static class HalfDoorsBlockTags extends FabricTagProvider.BlockTagProvider {
        public HalfDoorsBlockTags(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider arg) {
            this.valueLookupBuilder(BlockTags.WOODEN_DOORS).add(
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
            this.valueLookupBuilder(BlockTags.DOORS).add(
                    HalfDoors.IRON_HALFDOOR
            );
            this.valueLookupBuilder(BlockTags.FENCE_GATES).add(
                    HalfDoors.IRON_FENCE_GATE
            );
            this.valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE).add(
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
            this.valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(
                    HalfDoors.IRON_HALFDOOR,
                    HalfDoors.IRON_FENCE_GATE
            );
        }
    }
}