package amymialee.halfdoors.compat.rei;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.recipe.DoorcuttingRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class HalfDoorsREIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<DoorcuttingDisplay> DOORCUTTING = CategoryIdentifier.of(Halfdoors.MOD_ID, "doorcutting");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.addWorkstations(DOORCUTTING, EntryStacks.of(Halfdoors.DOORCUTTER));
        registry.add(new DoorcuttingCategory());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(DoorcuttingRecipe.class, DoorcuttingDisplay::new);
    }
}