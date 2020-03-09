package fr.fabienhebuterne.customcraft.domain;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.stream.IntStream;

import static fr.fabienhebuterne.customcraft.commands.CommandCreate.*;

public class RecipeInventoryService {

    private JavaPlugin customCraft;

    public RecipeInventoryService(JavaPlugin customCraft) {
        this.customCraft = customCraft;
    }

    public void openChooseCraftTypeInventory(Player player) {
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

        player.openInventory(inventory);
    }

    public void openCreateCraftShapedOrShapelessRecipeInventory(Player player, RecipeType recipeType) {
        Inventory inventory = Bukkit.createInventory(
                player,
                InventoryType.CHEST,
                this.customCraft.getName() + " - " + recipeType.getNameType()
        );

        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        IntStream.range(0, InventoryType.CHEST.getDefaultSize())
                .filter(value -> !CRAFT_CASES.contains(value) && RESULT_CRAFT_CASE != value)
                .forEach(value -> inventory.setItem(value, itemStack));

        inventory.setItem(QUIT_INVENTORY_CASE, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        inventory.setItem(VALID_INVENTORY_CASE, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));

        player.openInventory(inventory);
    }

}
