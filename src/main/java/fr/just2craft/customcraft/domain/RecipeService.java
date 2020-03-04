package fr.just2craft.customcraft.domain;

import fr.just2craft.customcraft.CustomCraft;
import fr.just2craft.customcraft.utils.ListUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class RecipeService {

    // TODO : Use dependency injection for customcraft instance ?
    private CustomCraft customCraft;

    private ArrayList<ItemStack> craftCaseRecipe;
    private final HashMap<Integer, ItemStack> idCraftCaseRecipse;
    private final ItemStack resultCraft;
    private final String craftName;

    public RecipeService(CustomCraft customCraft,
                         ArrayList<ItemStack> craftCaseRecipe,
                         HashMap<Integer, ItemStack> idCraftCaseRecipse,
                         ItemStack resultCraft,
                         String craftName) {
        this.customCraft = customCraft;
        this.craftCaseRecipe = craftCaseRecipe;
        this.idCraftCaseRecipse = idCraftCaseRecipse;
        this.resultCraft = resultCraft;
        this.craftName = craftName;
    }

    public void addNewRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(this.customCraft, craftName), resultCraft);

        ArrayList<String> grid = getGrid(craftCaseRecipe, idCraftCaseRecipse);

        shapedRecipe.shape(grid.get(0), grid.get(1), grid.get(2));

        idCraftCaseRecipse.forEach((integer, itemStack) -> {
            if (itemStack != null && itemStack.getData() != null) {
                shapedRecipe.setIngredient(integer.toString().charAt(0), new RecipeChoice.ExactChoice(itemStack));
            }
        });

        ShapedRecipeConfig shapedRecipeConfig = new ShapedRecipeConfig();
        shapedRecipeConfig.setItemToCraft(resultCraft);
        shapedRecipeConfig.setGrid(grid);

        HashMap<Integer, ItemStack> gridHashMap = new HashMap<>();
        idCraftCaseRecipse.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .forEach(entry -> gridHashMap.put(entry.getKey(), entry.getValue()));

        shapedRecipeConfig.setGridSequence(gridHashMap);

        // Save into config
        Config customcraft = this.customCraft.getConfig().getSerializable("customcraft", Config.class);

        if (customcraft == null) {
            customcraft = new Config();
        }

        customcraft.addShapedRecipe(shapedRecipeConfig);
        this.customCraft.getConfig().set("customcraft", customcraft);
        this.customCraft.saveConfig();

        // TODO : Catch IllegalStateException (duplicate recipe) -> vanilla for exemple
        this.customCraft.getServer().addRecipe(shapedRecipe);
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

}
