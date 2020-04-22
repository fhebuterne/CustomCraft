package fr.fabienhebuterne.customcraft.domain.inventory;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.domain.PrepareCustomCraft;
import fr.fabienhebuterne.customcraft.domain.recipe.RecipeType;
import fr.fabienhebuterne.customcraft.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static fr.fabienhebuterne.customcraft.CustomCraft.PLUGIN_NAME;

public class InventoryInitService {

    public final static ArrayList<Integer> CRAFT_CASES = new ArrayList<>(Arrays.asList(1, 2, 3, 10, 11, 12, 19, 20, 21));
    public final static Integer RESULT_CRAFT_CASE = 15;
    public final static Integer QUIT_INVENTORY_CASE = 8;
    public final static Integer OPTIONS_INVENTORY_CASE = 17;
    public final static Integer VALID_INVENTORY_CASE = 26;

    private CustomCraft customCraft;

    public InventoryInitService(CustomCraft customCraft) {
        this.customCraft = customCraft;
    }

    public Inventory chooseCraftTypeInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(
                player,
                9,
                PLUGIN_NAME + " - " + "Recipe type"
        );

        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        IntStream.range(2, 9)
                .forEach(value -> inventory.setItem(value, itemStack));

        Arrays.stream(RecipeType.values()).forEach(recipeType -> {
            inventory.setItem(recipeType.getInvIndex(), recipeType.getItemStack());
        });

        return inventory;
    }

    public Inventory createCraftShapedOrShapelessRecipeInventory(Player player, PrepareCustomCraft prepareCustomCraft) {
        Inventory inventory = Bukkit.createInventory(
                player,
                InventoryType.CHEST,
                PLUGIN_NAME + " - " + prepareCustomCraft.getRecipeType().getNameType()
        );

        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        IntStream.range(0, InventoryType.CHEST.getDefaultSize())
                .filter(value -> !CRAFT_CASES.contains(value) && RESULT_CRAFT_CASE != value)
                .forEach(value -> inventory.setItem(value, itemStack));

        IntStream.range(0, prepareCustomCraft.getCraftCaseOrderRecipe().size())
                .forEach(index -> {
                    inventory.setItem(CRAFT_CASES.get(index), prepareCustomCraft.getCraftCaseOrderRecipe().get(index));
                });

        if (prepareCustomCraft.getCraftResult() != null) {
            inventory.setItem(RESULT_CRAFT_CASE, prepareCustomCraft.getCraftResult());
        }

        setValidatorsInventory(inventory);

        return inventory;
    }

    public Inventory optionsInventory(Player player, PrepareCustomCraft prepareCustomCraft) {
        Inventory inventory = Bukkit.createInventory(
                player,
                9,
                PLUGIN_NAME + " - " + "Settings"
        );

        // TODO : Using options with ENUM and link with each recipe type
        ItemStackUtils toggleBlockPlace = new ItemStackUtils(Material.DIRT);
        toggleBlockPlace.setName(customCraft.getTranslationConfig().getToggleBlockPlace());
        toggleBlockPlace.setOptionLore(prepareCustomCraft.getOptionItemStackConfig().isBlockCanBePlaced());
        inventory.setItem(0, toggleBlockPlace);

        ItemStackUtils toggleHighlight = new ItemStackUtils(Material.SANDSTONE);
        toggleHighlight.setName(customCraft.getTranslationConfig().getToggleHighlight());
        toggleHighlight.setOptionLore(prepareCustomCraft.getHighlightItem());
        toggleHighlight.addEnchantment(this.customCraft.customCraftEnchantment, 1);
        inventory.setItem(1, toggleHighlight);

        ItemStackUtils leaveInventory = new ItemStackUtils(Material.RED_GLAZED_TERRACOTTA);
        leaveInventory.setName(customCraft.getTranslationConfig().getBackToPreviousMenu());
        inventory.setItem(QUIT_INVENTORY_CASE, leaveInventory);

        return inventory;
    }

    private void setValidatorsInventory(Inventory inventory) {
        ItemStackUtils leaveInventory = new ItemStackUtils(Material.RED_GLAZED_TERRACOTTA);
        leaveInventory.setName(customCraft.getTranslationConfig().getCancel());
        inventory.setItem(QUIT_INVENTORY_CASE, leaveInventory);

        ItemStackUtils settingsInventory = new ItemStackUtils(Material.YELLOW_GLAZED_TERRACOTTA);
        settingsInventory.setName(customCraft.getTranslationConfig().getSettings());
        inventory.setItem(OPTIONS_INVENTORY_CASE, settingsInventory);

        ItemStackUtils validInventory = new ItemStackUtils(Material.GREEN_GLAZED_TERRACOTTA);
        validInventory.setName(customCraft.getTranslationConfig().getValidate());
        inventory.setItem(VALID_INVENTORY_CASE, validInventory);
    }

}
