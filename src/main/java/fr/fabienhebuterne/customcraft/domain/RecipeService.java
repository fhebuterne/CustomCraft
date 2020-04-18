package fr.fabienhebuterne.customcraft.domain;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.domain.config.CustomCraftConfig;
import fr.fabienhebuterne.customcraft.domain.config.OptionItemStackConfig;
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

    public void addShapedRecipe(Player player,
                                HashMap<Integer, ItemStack> idCraftCaseRecipse,
                                PrepareCustomCraft prepareCustomCraft) {
        ArrayList<String> grid = getGrid(prepareCustomCraft.getCraftCaseOrderRecipe(), idCraftCaseRecipse);
        ShapedRecipe shapedRecipe = new ShapedRecipe(
                new NamespacedKey(this.customCraft, prepareCustomCraft.getCraftName()),
                prepareCustomCraft.getCraftResult()
        );

        shapedRecipe.shape(grid.get(0), grid.get(1), grid.get(2));
        idCraftCaseRecipse.forEach((integer, itemStack) -> {
            if (itemStack != null) {
                shapedRecipe.setIngredient(integer.toString().charAt(0), new RecipeChoice.ExactChoice(itemStack));
            }
        });

        RecipeConfig recipeConfig = setRecipeConfig(
                prepareCustomCraft,
                idCraftCaseRecipse,
                grid
        );

        addRecipeOnServer(player, shapedRecipe, recipeConfig, prepareCustomCraft.getOptionItemStackConfig());
    }

    public void addShapelessRecipe(Player player,
                                   HashMap<Integer, ItemStack> idCraftCaseRecipse,
                                   PrepareCustomCraft prepareCustomCraft) {
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(
                new NamespacedKey(this.customCraft, prepareCustomCraft.getCraftName()),
                prepareCustomCraft.getCraftResult()
        );

        prepareCustomCraft.getCraftCaseOrderRecipe().stream()
                .filter(Objects::nonNull)
                .forEach(itemStack -> {
                    shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(itemStack));
                });

        RecipeConfig recipeConfig = setRecipeConfig(
                prepareCustomCraft,
                idCraftCaseRecipse,
                new ArrayList<>()
        );

        addRecipeOnServer(player, shapelessRecipe, recipeConfig, prepareCustomCraft.getOptionItemStackConfig());
    }

    private RecipeConfig setRecipeConfig(PrepareCustomCraft prepareCustomCraft,
                                         HashMap<Integer, ItemStack> idCraftCaseRecipse,
                                         ArrayList<String> grid) {
        RecipeConfig recipeConfig = new RecipeConfig();
        recipeConfig.setItemToCraft(prepareCustomCraft.getCraftResult());
        recipeConfig.setGrid(grid);
        recipeConfig.setCraftName(prepareCustomCraft.getCraftName());
        recipeConfig.setRecipeType(prepareCustomCraft.getRecipeType());

        HashMap<Integer, ItemStack> gridSequence = idCraftCaseRecipse.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, HashMap::new));

        recipeConfig.setGridSequence(gridSequence);

        return recipeConfig;
    }

    private void addRecipeOnServer(Player player, Recipe recipe, RecipeConfig recipeConfig, OptionItemStackConfig optionItemStackConfig) {
        CustomCraftConfig customcraft = this.customCraft.getCustomCraftConfig().getSerializable();

        if (customcraft == null) {
            customcraft = new CustomCraftConfig();
        }

        try {
            this.customCraft.getServer().addRecipe(recipe);
            customcraft.addRecipe(recipeConfig);

            HashMap<ItemStack, OptionItemStackConfig> optionItemStackHashMap = customcraft.getOptionItemStack();
            optionItemStackHashMap.put(recipeConfig.getItemToCraft(), optionItemStackConfig);
            customcraft.setOptionItemStack(optionItemStackHashMap);

            this.customCraft.getCustomCraftConfig().save(customcraft);
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
