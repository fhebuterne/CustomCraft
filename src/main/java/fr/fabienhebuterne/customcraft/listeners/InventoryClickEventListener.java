package fr.fabienhebuterne.customcraft.listeners;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.domain.InventoryInitService;
import fr.fabienhebuterne.customcraft.domain.PrepareCustomCraft;
import fr.fabienhebuterne.customcraft.domain.RecipeService;
import fr.fabienhebuterne.customcraft.domain.RecipeType;
import fr.fabienhebuterne.customcraft.domain.config.OptionItemStackConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static fr.fabienhebuterne.customcraft.domain.InventoryInitService.*;

public class InventoryClickEventListener implements Listener {

    private final CustomCraft customCraft;
    private final InventoryInitService inventoryInitService;

    public InventoryClickEventListener(CustomCraft customCraft) {
        this.customCraft = customCraft;
        // TODO : Dependency Injection
        this.inventoryInitService = new InventoryInitService(customCraft);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("CustomCraft")) {
            return;
        }

        Player player = (Player) e.getView().getPlayer();
        PrepareCustomCraft prepareCustomCraft = customCraft.getTmpData().get(player.getUniqueId());

        if (e.getView().getTitle().equals("CustomCraft - Options")) {
            this.chooseOptions(e, prepareCustomCraft);
            return;
        }

        if (prepareCustomCraft.getRecipeType() == null) {
            if (e.getView().getTitle().equalsIgnoreCase("CustomCraft - Recipe type")) {
                this.chooseCraftType(e, prepareCustomCraft);
            }
        } else {
            if (e.getView().getTitle().equalsIgnoreCase("CustomCraft - " + prepareCustomCraft.getRecipeType().getNameType())) {
                this.createCraftFromShapedOrShapelessRecipe(e, prepareCustomCraft);
            }
        }
    }

    // TODO : Refactor chooseOption and use ENUM
    private void chooseOptions(InventoryClickEvent e, PrepareCustomCraft prepareCustomCraft) {
        e.setCancelled(true);

        Player player = (Player) e.getView().getPlayer();

        if (e.getSlot() == 0) {
            OptionItemStackConfig optionItemStackConfig = prepareCustomCraft.getOptionItemStackConfig();
            optionItemStackConfig.setBlockCanBePlaced(!optionItemStackConfig.isBlockCanBePlaced());
            prepareCustomCraft.setOptionItemStackConfig(optionItemStackConfig);
        }

        if (e.getSlot() == 1) {
            prepareCustomCraft.setHighlightItem(!prepareCustomCraft.getHighlightItem());
        }

        if (e.getSlot() == QUIT_INVENTORY_CASE) {
            Inventory craftShapedOrShapelessRecipeInventory = this.inventoryInitService.createCraftShapedOrShapelessRecipeInventory(player, prepareCustomCraft);
            player.openInventory(craftShapedOrShapelessRecipeInventory);
        }
    }

    private void chooseCraftType(InventoryClickEvent e, PrepareCustomCraft prepareCustomCraft) {
        boolean invClick = e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.CHEST;

        if (invClick) {
            e.setCancelled(true);
        }

        Player player = (Player) e.getView().getPlayer();

        // Default recipe
        prepareCustomCraft.setRecipeType(RecipeType.SHAPED_RECIPE);

        if (e.getSlot() == RecipeType.SHAPELESS_RECIPE.getInvIndex()) {
            prepareCustomCraft.setRecipeType(RecipeType.SHAPELESS_RECIPE);
        }

        Inventory inventory = inventoryInitService.createCraftShapedOrShapelessRecipeInventory(
                player,
                prepareCustomCraft
        );

        player.openInventory(inventory);
    }

    private void createCraftFromShapedOrShapelessRecipe(InventoryClickEvent e, PrepareCustomCraft prepareCustomCraft) {
        boolean invCases = !CRAFT_CASES.contains(e.getSlot()) && RESULT_CRAFT_CASE != e.getSlot();
        boolean invClick = e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.CHEST;
        Player player = (Player) e.getView().getPlayer();

        if (invCases && invClick) {
            e.setCancelled(true);
        }

        if (e.getRawSlot() == QUIT_INVENTORY_CASE) {
            e.getView().close();
        }

        saveIntoPrepareCustomCraft(e, prepareCustomCraft);

        if (e.getRawSlot() == OPTIONS_INVENTORY_CASE) {
            Inventory inventory = inventoryInitService.optionsInventory(player, prepareCustomCraft.getRecipeType());
            player.openInventory(inventory);
        }

        if (e.getRawSlot() == VALID_INVENTORY_CASE) {
            if (prepareCustomCraft.getCraftResult() == null) {
                ItemStack itemStack = e.getInventory().getItem(VALID_INVENTORY_CASE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(Collections.singletonList(CustomCraft.getTranslationConfig().getSerializable().getMissingCraftItem()));
                itemStack.setItemMeta(itemMeta);
                e.getInventory().setItem(VALID_INVENTORY_CASE, itemStack);
                return;
            }

            // TODO : Cancel before add craft if one case have more than 1 item (technical limitation)

            addNewRecipe(player, prepareCustomCraft);
        }
    }

    private void saveIntoPrepareCustomCraft(InventoryClickEvent e, PrepareCustomCraft prepareCustomCraft) {
        Player player = (Player) e.getView().getPlayer();

        // Craft order list
        ArrayList<ItemStack> craftCaseOrderRecipe = new ArrayList<>();
        CRAFT_CASES.forEach(integer -> {
            craftCaseOrderRecipe.add(e.getInventory().getItem(integer));
        });
        prepareCustomCraft.setCraftCaseOrderRecipe(craftCaseOrderRecipe);

        // Craft item result
        ItemStack craftResult = e.getInventory().getItem(RESULT_CRAFT_CASE);
        prepareCustomCraft.setCraftResult(craftResult);

        customCraft.addTmpData(player.getUniqueId(), prepareCustomCraft);
    }

    private void addNewRecipe(Player player, PrepareCustomCraft prepareCustomCraft) {
        // Give for each different itemstack an unique id to set in grid
        HashMap<Integer, ItemStack> idCraftCaseRecipse = new HashMap<>();

        prepareCustomCraft.getCraftCaseOrderRecipe().forEach(itemStack -> {
            if (!idCraftCaseRecipse.containsValue(itemStack)) {
                idCraftCaseRecipse.put(prepareCustomCraft.getCraftCaseOrderRecipe().indexOf(itemStack), itemStack);
            }
        });

        // Add hidden enchant to keep highlight item in chest or with other plugins
        if (prepareCustomCraft.getHighlightItem()) {
            ItemStack craftResult = prepareCustomCraft.getCraftResult();
            craftResult.addEnchantment(this.customCraft.customCraftEnchantment, 1);
            prepareCustomCraft.setCraftResult(craftResult);
        }

        if (prepareCustomCraft.getRecipeType() == RecipeType.SHAPED_RECIPE) {
            new RecipeService(this.customCraft).addShapedRecipe(
                    player,
                    idCraftCaseRecipse,
                    prepareCustomCraft
            );
        } else {
            new RecipeService(this.customCraft).addShapelessRecipe(
                    player,
                    idCraftCaseRecipse,
                    prepareCustomCraft
            );
        }
    }

}
