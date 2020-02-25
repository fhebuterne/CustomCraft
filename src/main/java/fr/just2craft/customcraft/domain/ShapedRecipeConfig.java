package fr.just2craft.customcraft.domain;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapedRecipeConfig implements ConfigurationSerializable {

    private ItemStack itemToCraft;
    private List<String> grid;
    private HashMap<Character, String> gridSequence;

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

    public HashMap<Character, String> getGridSequence() {
        return gridSequence;
    }

    public void setGridSequence(HashMap<Character, String> gridSequence) {
        this.gridSequence = gridSequence;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("itemToCraft", itemToCraft);
        map.put("grid", grid);
        map.put("gridSequence", gridSequence);
        return map;
    }

    @Override
    public String toString() {
        return "ShapedRecipeConfig{" +
                "itemToCraft=" + itemToCraft +
                ", grid=" + grid +
                ", gridSequence=" + gridSequence +
                '}';
    }

}
