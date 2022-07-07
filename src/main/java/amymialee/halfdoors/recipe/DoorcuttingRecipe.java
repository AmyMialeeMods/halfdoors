package amymialee.halfdoors.recipe;

import amymialee.halfdoors.Halfdoors;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DoorcuttingRecipe extends CuttingRecipe {
    public DoorcuttingRecipe(Identifier id, String group, Ingredient input, ItemStack output) {
        super(Halfdoors.RECIPE_TYPE, Halfdoors.DOOR_CUTTING_RECIPE, id, group, input, output);
    }

    public boolean matches(Inventory inv, World world) {
        return this.input.test(inv.getStack(0));
    }

    public static class OpenSerializer<T extends CuttingRecipe> extends Serializer<T> {
        public OpenSerializer(Serializer.RecipeFactory<T> recipeFactory) {
            super(recipeFactory);
        }
    }
}