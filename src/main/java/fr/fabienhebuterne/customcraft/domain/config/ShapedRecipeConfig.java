package fr.fabienhebuterne.customcraft.domain.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapedRecipeConfig implements ConfigurationSerializable {

    private String craftName;
    private ItemStack itemToCraft;
    private List<String> grid;
    private HashMap<Integer, ItemStack> gridSequence;

    public String getCraftName() {
        return craftName;
    }

    public void setCraftName(String craftName) {
        this.craftName = craftName;
    }

    public ItemStack getItemToCraft() {
        return itemToCraft;
    }

    public void setItemToCraft(ItemStack itemToCraft) {
        this.itemToCraft = itemToCraft;
    }

    public List<String> getGrid() {
        return grid;
    }

    public void setGrid(List<String> grid) {
        this.grid = grid;
    }

    public HashMap<Integer, ItemStack> getGridSequence() {
        return gridSequence;
    }

    public void setGridSequence(HashMap<Integer, ItemStack> gridSequence) {
        this.gridSequence = gridSequence;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("craftName", craftName);
        map.put("itemToCraft", itemToCraft);
        map.put("grid", grid);
        map.put("gridSequence", gridSequence);
        return map;
    }

    public static ShapedRecipeConfig deserialize(Map<String, Object> map) {
        ShapedRecipeConfig shapedRecipeConfig = new ShapedRecipeConfig();
        shapedRecipeConfig.craftName = (String) map.get("craftName");
        shapedRecipeConfig.itemToCraft = (ItemStack) map.get("itemToCraft");
        shapedRecipeConfig.grid = (List<String>) map.get("grid");
        shapedRecipeConfig.gridSequence = (HashMap<Integer, ItemStack>) map.get("gridSequence");
        return shapedRecipeConfig;
    }

    @Override
    public String toString() {
        return "ShapedRecipeConfig{" +
                "craftName=" + craftName +
                ", itemToCraft=" + itemToCraft +
                ", grid=" + grid +
                ", gridSequence=" + gridSequence +
                '}';
    }

}