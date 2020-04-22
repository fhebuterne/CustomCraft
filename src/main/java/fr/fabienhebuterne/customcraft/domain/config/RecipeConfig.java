package fr.fabienhebuterne.customcraft.domain.config;

import fr.fabienhebuterne.customcraft.domain.recipe.RecipeType;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RecipeConfig implements ConfigurationSerializable {

    private String craftName;
    private ItemStack itemToCraft;
    private List<String> grid;
    private HashMap<Integer, ItemStack> gridSequence;
    private RecipeType recipeType;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("craftName", craftName);
        map.put("itemToCraft", itemToCraft);
        map.put("grid", grid);
        map.put("gridSequence", gridSequence);
        map.put("recipeType", recipeType);
        return map;
    }

    public static RecipeConfig deserialize(Map<String, Object> map) {
        RecipeConfig shapedRecipeConfig = new RecipeConfig();
        shapedRecipeConfig.craftName = (String) map.get("craftName");
        shapedRecipeConfig.itemToCraft = (ItemStack) map.get("itemToCraft");
        shapedRecipeConfig.grid = (List<String>) map.get("grid");
        shapedRecipeConfig.gridSequence = (HashMap<Integer, ItemStack>) map.get("gridSequence");
        shapedRecipeConfig.recipeType = RecipeType.valueOf((String) map.get("recipeType"));
        return shapedRecipeConfig;
    }

}
