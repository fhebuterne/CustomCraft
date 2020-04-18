package fr.fabienhebuterne.customcraft.domain;

import fr.fabienhebuterne.customcraft.CustomCraft;
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
                this.customCraft.getName() + " - " + "Recipe type"
        );

        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        IntStream.range(2, 9)
                .forEach(value -> {
                    inventory.setItem(value, itemStack);
                });

        Arrays.stream(RecipeType.values()).forEach(recipeType -> {
            inventory.setItem(recipeType.getInvIndex(), recipeType.getItemStack());
        });

        return inventory;
    }

    public Inventory createCraftShapedOrShapelessRecipeInventory(Player player, RecipeType recipeType) {
        Inventory inventory = Bukkit.createInventory(
                player,
                InventoryType.CHEST,
                this.customCraft.getName() + " - " + recipeType.getNameType()
        );

        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        IntStream.range(0, InventoryType.CHEST.getDefaultSize())
                .filter(value -> !CRAFT_CASES.contains(value) && RESULT_CRAFT_CASE != value)
                .forEach(value -> inventory.setItem(value, itemStack));

        setValidatorsInventory(inventory);

        return inventory;
    }

    public Inventory optionsInventory(Player player, RecipeType recipeType) {
        Inventory inventory = Bukkit.createInventory(
                player,
                9,
                this.customCraft.getName() + " - " + "Options"
        );

        // TODO : Using options with ENUM and link with each recipe type
        ItemStack toggleBlockPlace = new ItemStack(Material.DIRT);
        ItemStackUtils.setName(toggleBlockPlace, "§6Toggle block place");
        inventory.setItem(0, toggleBlockPlace);

        ItemStack toggleHighlight = new ItemStack(Material.SANDSTONE);
        ItemStackUtils.setName(toggleHighlight, "§6Toggle highlight item");
        toggleHighlight.addEnchantment(this.customCraft.customCraftEnchantment, 1);
        inventory.setItem(1, toggleHighlight);

        ItemStack leaveInventory = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
        ItemStackUtils.setName(leaveInventory, "§cRevenir au menu précédent");
        inventory.setItem(QUIT_INVENTORY_CASE, leaveInventory);

        return inventory;
    }

    private void setValidatorsInventory(Inventory inventory) {
        ItemStack leaveInventory = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
        ItemStackUtils.setName(leaveInventory, "§cAnnuler");
        inventory.setItem(QUIT_INVENTORY_CASE, leaveInventory);

        ItemStack optionsInventory = new ItemStack(Material.YELLOW_GLAZED_TERRACOTTA);
        ItemStackUtils.setName(optionsInventory, "§eOptions");
        inventory.setItem(OPTIONS_INVENTORY_CASE, optionsInventory);

        ItemStack validInventory = new ItemStack(Material.GREEN_GLAZED_TERRACOTTA);
        ItemStackUtils.setName(validInventory, "§aValider");
        inventory.setItem(VALID_INVENTORY_CASE, validInventory);
    }

}
