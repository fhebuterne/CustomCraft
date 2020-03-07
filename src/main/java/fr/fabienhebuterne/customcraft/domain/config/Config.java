package fr.fabienhebuterne.customcraft.domain.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config implements ConfigurationSerializable {

    private List<ShapedRecipeConfig> shapedRecipes = new ArrayList<>();
    private HashMap<ItemStack, OptionItemStackConfig> optionItemStackConfig = new HashMap<>();

    public List<ShapedRecipeConfig> getShapedRecipes() {
        return shapedRecipes;
    }

    public void setShapedRecipes(List<ShapedRecipeConfig> shapedRecipes) {
        this.shapedRecipes = shapedRecipes;
    }

    public void addShapedRecipe(ShapedRecipeConfig shapedRecipe) {
        this.shapedRecipes.add(shapedRecipe);
    }

    public HashMap<ItemStack, OptionItemStackConfig> getOptionItemStackConfig() {
        return optionItemStackConfig;
    }

    public void setOptionItemStackConfig(HashMap<ItemStack, OptionItemStackConfig> optionItemStackConfig) {
        this.optionItemStackConfig = optionItemStackConfig;
    }

    public void addOptionItemStackConfig(ItemStack itemStack, OptionItemStackConfig optionItemStackConfig) {
        this.optionItemStackConfig.put(itemStack, optionItemStackConfig);
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("shapedRecipes", shapedRecipes);
        map.put("optionItemStackConfig", optionItemStackConfig);
        return map;
    }

    public static Config deserialize(Map<String, Object> map) {
        Config config = new Config();
        config.shapedRecipes = (List<ShapedRecipeConfig>) map.get("shapedRecipes");
        config.optionItemStackConfig = (HashMap<ItemStack, OptionItemStackConfig>) map.get("optionItemStackConfig");
        return config;
    }

    @Override
    public String toString() {
        return "Config{" +
                "shapedRecipes='" + shapedRecipes + '\'' +
                ", optionItemStackConfig='" + optionItemStackConfig + '\'' +
                '}';
    }

}
