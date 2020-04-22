package fr.fabienhebuterne.customcraft.domain.config;

import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CustomCraftConfig implements ConfigurationSerializable {

    private List<RecipeConfig> recipes = new ArrayList<>();
    private HashMap<ItemStack, OptionItemStackConfig> optionItemStack = new HashMap<>();

    public void addRecipe(RecipeConfig recipe) {
        this.recipes.add(recipe);
    }

    public void addOptionItemStack(ItemStack itemStack, OptionItemStackConfig optionItemStack) {
        this.optionItemStack.put(itemStack, optionItemStack);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("recipes", recipes);
        map.put("optionItemStack", optionItemStack);
        return map;
    }

    public static CustomCraftConfig deserialize(Map<String, Object> map) {
        CustomCraftConfig config = new CustomCraftConfig();
        config.recipes = (List<RecipeConfig>) map.get("recipes");
        config.optionItemStack = (HashMap<ItemStack, OptionItemStackConfig>) map.get("optionItemStack");
        return config;
    }

}
