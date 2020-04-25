package fr.fabienhebuterne.customcraft.domain.config;

import com.google.gson.annotations.JsonAdapter;
import fr.fabienhebuterne.customcraft.json.ItemStackMapKeyAdapter;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class CustomCraftConfig {

    private List<RecipeConfig> recipes = new ArrayList<>();

    @JsonAdapter(ItemStackMapKeyAdapter.class)
    private HashMap<ItemStack, OptionItemStackConfig> optionItemStack = new HashMap<>();

    public void addRecipe(RecipeConfig recipe) {
        this.recipes.add(recipe);
    }

    public void addOptionItemStack(ItemStack itemStack, OptionItemStackConfig optionItemStack) {
        this.optionItemStack.put(itemStack, optionItemStack);
    }

}
