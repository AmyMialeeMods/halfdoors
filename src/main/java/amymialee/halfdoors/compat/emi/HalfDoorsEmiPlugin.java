package amymialee.halfdoors.compat.emi;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.recipe.DoorcuttingRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

public class HalfDoorsEmiPlugin implements EmiPlugin {
    public static final EmiStack DOORCUTTER = EmiStack.of(Halfdoors.DOORCUTTER);
    public static final EmiRecipeCategory DOORCUTTING = new EmiRecipeCategory(new Identifier("halfdoors:doorcutting"), DOORCUTTER);

    @Override
    public void register(EmiRegistry registry) {
        // Tell EMI to add a tab for your category
        registry.addCategory(DOORCUTTING);

        // Add all the workstations your category uses
        registry.addWorkstation(DOORCUTTING, DOORCUTTER);

        RecipeManager manager = registry.getRecipeManager();

        // Use vanilla's concept of your recipes and pass them to your EmiRecipe representation
        for (DoorcuttingRecipe recipe : manager.listAllOfType(Halfdoors.RECIPE_TYPE)) {
            registry.addRecipe(new EmiDoorcuttingRecipe(recipe));
        }
    }
}