package fr.just2craft.customcraft.domain;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config implements ConfigurationSerializable {

    private List<ShapedRecipeConfig> shapedRecipes = new ArrayList<>();

    public List<ShapedRecipeConfig> getShapedRecipes() {
        return shapedRecipes;
    }

    public void setShapedRecipes(List<ShapedRecipeConfig> shapedRecipes) {
        this.shapedRecipes = shapedRecipes;
    }

    public void addShapedRecipe(ShapedRecipeConfig shapedRecipe) {
        this.shapedRecipes.add(shapedRecipe);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("shapedRecipes", shapedRecipes);
        return map;
    }

    public static Config deserialize(Map<String, Object> map) {
        Config config = new Config();
        config.shapedRecipes = (List<ShapedRecipeConfig>) map.get("shapedRecipes");
        return config;
    }

    @Override
    public String toString() {
        return "Config{" +
                "shapedRecipes='" + shapedRecipes + '\'' +
                '}';
    }

}
