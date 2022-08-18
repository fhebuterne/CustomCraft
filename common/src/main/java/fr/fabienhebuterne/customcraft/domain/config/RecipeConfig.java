package fr.fabienhebuterne.customcraft.domain.config;

import com.google.gson.annotations.JsonAdapter;
import fr.fabienhebuterne.customcraft.domain.recipe.RecipeType;
import fr.fabienhebuterne.customcraft.json.ItemStackAdapter;
import fr.fabienhebuterne.customcraft.json.ItemStackMapValueAdapter;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@Data
public class RecipeConfig {

    private String craftName;
    private List<String> grid;
    private RecipeType recipeType;

    @JsonAdapter(ItemStackAdapter.class)
    private ItemStack itemToCraft;

    @JsonAdapter(ItemStackMapValueAdapter.class)
    private HashMap<Integer, ItemStack> gridSequence;

}
