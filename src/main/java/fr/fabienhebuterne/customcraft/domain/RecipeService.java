package fr.fabienhebuterne.customcraft.domain;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.domain.config.CustomCraftConfig;
import fr.fabienhebuterne.customcraft.domain.config.RecipeConfig;
import fr.fabienhebuterne.customcraft.utils.ListUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeService {

    // TODO : Use dependency injection for customcraft instance ?
    private CustomCraft customCraft;

    public RecipeService(CustomCraft customCraft) {
        this.customCraft = customCraft;
    }

    // TODO : this is test
    //OptionItemStackConfig optionItemStackConfig = new OptionItemStackConfig();
    //optionItemStackConfig.setBlockCanBePlaced(false);
    //customcraft.addOptionItemStackConfig(new ItemStack(Material.APPLE), optionItemStackConfig);
    // -------

    public void addShapedRecipe(Player player,
                                ArrayList<ItemStack> craftCaseRecipe,
                                HashMap<Integer, ItemStack> idCraftCaseRecipse,
                                ItemStack resultCraft,
                                String craftName,
                                RecipeType recipeType) {
        ArrayList<String> grid = getGrid(craftCaseRecipe, idCraftCaseRecipse);
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(this.customCraft, craftName), resultCraft);
        shapedRecipe.shape(grid.get(0), grid.get(1), grid.get(2));
        idCraftCaseRecipse.forEach((integer, itemStack) -> {
            if (itemStack != null) {
                shapedRecipe.setIngredient(integer.toString().charAt(0), new RecipeChoice.ExactChoice(itemStack));
            }
        });
        RecipeConfig recipeConfig = setRecipeConfig(resultCraft, grid, craftName, idCraftCaseRecipse, recipeType);
        addRecipeOnServer(player, shapedRecipe, recipeConfig);
    }

    public void addShapelessRecipe(Player player,
                                   ArrayList<ItemStack> craftCaseRecipe,
                                   HashMap<Integer, ItemStack> idCraftCaseRecipse,
                                   ItemStack resultCraft,
                                   String craftName,
                                   RecipeType recipeType) {
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(new NamespacedKey(this.customCraft, craftName), resultCraft);
        craftCaseRecipe.stream()
                .filter(Objects::nonNull)
                .forEach(itemStack -> {
                    shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(itemStack));
                });

        RecipeConfig recipeConfig = setRecipeConfig(resultCraft, new ArrayList<>(), craftName, idCraftCaseRecipse, recipeType);
        addRecipeOnServer(player, shapelessRecipe, recipeConfig);
    }

    private RecipeConfig setRecipeConfig(ItemStack resultCraft,
                                         ArrayList<String> grid,
                                         String craftName,
                                         HashMap<Integer, ItemStack> idCraftCaseRecipse,
                                         RecipeType recipeType) {
        RecipeConfig recipeConfig = new RecipeConfig();
        recipeConfig.setItemToCraft(resultCraft);
        recipeConfig.setGrid(grid);
        recipeConfig.setCraftName(craftName);
        recipeConfig.setRecipeType(recipeType);

        HashMap<Integer, ItemStack> gridSequence = idCraftCaseRecipse.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, HashMap::new));

        recipeConfig.setGridSequence(gridSequence);

        return recipeConfig;
    }

    private void addRecipeOnServer(Player player, Recipe recipe, RecipeConfig recipeConfig) {
        CustomCraftConfig customcraft = this.customCraft.getCustomCraftConfig();

        if (customcraft == null) {
            customcraft = new CustomCraftConfig();
        }

        try {
            this.customCraft.getServer().addRecipe(recipe);
            customcraft.addRecipe(recipeConfig);
            this.customCraft.saveCustomCraftConfig(customcraft);
        } catch (IllegalStateException e) {
            player.sendMessage("§cErreur : Cette recette est déjà présente !");
        }
    }

    private ArrayList<String> getGrid(ArrayList<ItemStack> craftCaseRecipe, HashMap<Integer, ItemStack> idCraftCaseRecipse) {
        String completeRow = craftCaseRecipe.stream().map(itemStack -> {
            String row = "";
            if (itemStack == null) {
                row += " ";
            } else {
                row += ListUtils.getKeysByValue(idCraftCaseRecipse, itemStack).get(0);
            }
            return row;
        }).collect(Collectors.joining());

        // split each by 3 caracters
        String[] splitCompleteRow = completeRow.split("(?<=\\G...)");

        return new ArrayList<>(Arrays.asList(splitCompleteRow));
    }


    // TODO : Create other service to load all recipes
    public void loadCustomRecipe() {
        CustomCraftConfig customcraft = this.customCraft.getCustomCraftConfig();

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
