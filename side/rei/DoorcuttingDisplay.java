package amymialee.halfdoors.compat.rei;

import amymialee.halfdoors.recipe.DoorcuttingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DoorcuttingDisplay extends BasicDisplay {
    public DoorcuttingDisplay(DoorcuttingRecipe recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())), Optional.ofNullable(recipe.getId()));
    }

    public DoorcuttingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return HalfDoorsREIPlugin.DOORCUTTING;
    }

    public static BasicDisplay.Serializer<DoorcuttingDisplay> serializer() {
        return BasicDisplay.Serializer.ofSimple(DoorcuttingDisplay::new);
    }
}