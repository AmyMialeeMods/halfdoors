package amymialee.halfdoors.compat.emi;

import amymialee.halfdoors.recipe.DoorcuttingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;

import java.util.List;

public class EmiDoorcuttingRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiIngredient input;
    private final EmiStack output;

    public EmiDoorcuttingRecipe(DoorcuttingRecipe recipe) {
        this.id = recipe.getId();
        this.input = EmiIngredient.of(recipe.getIngredients().get(0));
        this.output = EmiStack.of(recipe.getOutput());
    }

    public EmiRecipeCategory getCategory() {
        return HalfDoorsEmiPlugin.DOORCUTTING;
    }

    public Identifier getId() {
        return this.id;
    }

    public List<EmiIngredient> getInputs() {
        return List.of(this.input);
    }

    public List<EmiStack> getOutputs() {
        return List.of(this.output);
    }

    public int getDisplayWidth() {
        return 76;
    }

    public int getDisplayHeight() {
        return 18;
    }

    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgets.addSlot(this.input, 0, 0);
        widgets.addSlot(this.output, 58, 0).recipeContext(this);
    }
}
