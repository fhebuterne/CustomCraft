package fr.just2craft.customcraft.listeners;

import fr.just2craft.customcraft.CustomCraft;
import fr.just2craft.customcraft.domain.RecipeService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static fr.just2craft.customcraft.commands.CommandCreate.*;

public class InventoryClickEventListener implements Listener {

    private CustomCraft customCraft;

    public InventoryClickEventListener(CustomCraft customCraft) {
        this.customCraft = customCraft;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("CustomCraft")) {
            return;
        }

        this.createCommand(e);
    }

    private void createCommand(InventoryClickEvent e) {
        if (!CRAFT_CASES.contains(e.getSlot()) &&
                e.getClickedInventory() != null &&
                e.getClickedInventory().getType() == InventoryType.CHEST) {
            e.setCancelled(true);
        }

        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getSlot() == QUIT_INVENTORY_CASE) {
            e.getView().close();
        }

        if (e.getSlot() == VALID_INVENTORY_CASE) {
            // Craft order
            ArrayList<ItemStack> craftCaseRecipe = new ArrayList<>();

            // Give for each diffrent itemstack an unique id to set in grid
            HashMap<Integer, ItemStack> idCraftCaseRecipse = new HashMap<>();

            CRAFT_CASES.forEach(integer -> craftCaseRecipe.add(e.getClickedInventory().getItem(integer)));

            craftCaseRecipe.forEach(itemStack -> {
                if (!idCraftCaseRecipse.containsValue(itemStack)) {
                    idCraftCaseRecipse.put(craftCaseRecipe.indexOf(itemStack), itemStack);
                }
            });

            ItemStack resultCraft = e.getClickedInventory().getItem(RESULT_CRAFT_CASE);

            if (resultCraft == null) {
                ItemStack itemStack = e.getClickedInventory().getItem(VALID_INVENTORY_CASE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(Collections.singletonList("§cErreur : Vous n'avez pas mis d'item craftable..."));
                itemStack.setItemMeta(itemMeta);
                e.getClickedInventory().setItem(VALID_INVENTORY_CASE, itemStack);
                return;
            }

            String craftName = e.getClickedInventory().getItem(0).getItemMeta().getDisplayName();

            new RecipeService(this.customCraft, craftCaseRecipe, idCraftCaseRecipse, resultCraft, craftName).addNewRecipe();
        }
    }

}
