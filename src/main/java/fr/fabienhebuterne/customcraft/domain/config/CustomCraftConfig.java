package fr.fabienhebuterne.customcraft.domain.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCraftConfig implements ConfigurationSerializable {

    private List<RecipeConfig> recipes = new ArrayList<>();
    private HashMap<ItemStack, OptionItemStackConfig> optionItemStack = new HashMap<>();

    public List<RecipeConfig> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeConfig> recipes) {
        this.recipes = recipes;
    }

    public void addRecipe(RecipeConfig recipe) {
        this.recipes.add(recipe);
    }

    public HashMap<ItemStack, OptionItemStackConfig> getOptionItemStack() {
        return optionItemStack;
    }

    public void setOptionItemStack(HashMap<ItemStack, OptionItemStackConfig> optionItemStack) {
        this.optionItemStack = optionItemStack;
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

    @Override
    public String toString() {
        return "Config{" +
                "recipes='" + recipes + '\'' +
                ", optionItemStack='" + optionItemStack + '\'' +
                '}';
    }

}
