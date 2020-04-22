package fr.fabienhebuterne.customcraft.domain.recipe;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.domain.config.CustomCraftConfig;
import fr.fabienhebuterne.customcraft.domain.config.RecipeConfig;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.List;
import java.util.Objects;

public class RecipeLoadService {

    // TODO : Use dependency injection for customcraft instance ?
    private CustomCraft customCraft;

    public RecipeLoadService(CustomCraft customCraft) {
        this.customCraft = customCraft;
    }

    public void loadCustomRecipe() {
        CustomCraftConfig customcraft = this.customCraft.getCustomCraftConfig().getSerializable();

        if (customcraft == null) {
            return;
        }

        customcraft.getRecipes()
                .stream()
                .filter(Objects::nonNull)
                .forEach(recipeConfig -> {
                    if (recipeConfig.getRecipeType() == RecipeType.SHAPED_RECIPE) {
                        loadShapedRecipe(recipeConfig);
                    }

                    if (recipeConfig.getRecipeType() == RecipeType.SHAPELESS_RECIPE) {
                        loadShapelessRecipe(recipeConfig);
                    }
                });
    }

    private void loadShapedRecipe(RecipeConfig recipeConfig) {
        ShapedRecipe shapedRecipe = new ShapedRecipe(
                new NamespacedKey(this.customCraft, recipeConfig.getCraftName()),
                recipeConfig.getItemToCraft()
        );
        List<String> grid = recipeConfig.getGrid();
        shapedRecipe.shape(grid.get(0), grid.get(1), grid.get(2));
        recipeConfig.getGridSequence().forEach(
                (integer, itemStack) -> shapedRecipe.setIngredient(
                        integer.toString().charAt(0),
                        new RecipeChoice.ExactChoice(itemStack)
                )
        );
        this.customCraft.getServer().addRecipe(shapedRecipe);
    }

    private void loadShapelessRecipe(RecipeConfig recipeConfig) {
        ShapelessRecipe shapedRecipe = new ShapelessRecipe(
                new NamespacedKey(this.customCraft, recipeConfig.getCraftName()),
                recipeConfig.getItemToCraft()
        );
        recipeConfig.getGridSequence().forEach(
                (integer, itemStack) -> shapedRecipe.addIngredient(
                        new RecipeChoice.ExactChoice(itemStack)
                )
        );
        this.customCraft.getServer().addRecipe(shapedRecipe);
    }

}
