package fr.fabienhebuterne.customcraft.listeners;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.domain.PrepareCustomCraft;
import fr.fabienhebuterne.customcraft.domain.config.CustomCraftConfig;
import fr.fabienhebuterne.customcraft.domain.config.OptionItemStackConfig;
import fr.fabienhebuterne.customcraft.domain.config.RecipeConfig;
import fr.fabienhebuterne.customcraft.domain.inventory.InventoryInitService;
import fr.fabienhebuterne.customcraft.domain.recipe.RecipeService;
import fr.fabienhebuterne.customcraft.domain.recipe.RecipeType;
import fr.fabienhebuterne.customcraft.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import static fr.fabienhebuterne.customcraft.CustomCraft.PLUGIN_NAME;
import static fr.fabienhebuterne.customcraft.domain.inventory.InventoryInitService.*;

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
        if (!e.getView().getTitle().contains(PLUGIN_NAME)) {
            return;
        }

        Player player = (Player) e.getView().getPlayer();
        PrepareCustomCraft prepareCustomCraft = customCraft.getTmpData().get(player.getUniqueId());

        if (e.getView().getTitle().equals(PLUGIN_NAME + " - " + "Settings")) {
            this.settingsInventory(e, prepareCustomCraft);
            return;
        }

        if (e.getView().getTitle().equals(PLUGIN_NAME + " - " + "Delete")) {
            this.deleteInventory(e);
            return;
        }

        if (prepareCustomCraft.getRecipeType() == null) {
            if (e.getView().getTitle().equalsIgnoreCase(PLUGIN_NAME + " - " + "Recipe type")) {
                this.chooseCraftType(e, prepareCustomCraft);
            }
        } else {
            if (e.getView().getTitle().equalsIgnoreCase(PLUGIN_NAME + " - " + prepareCustomCraft.getRecipeType().getNameType())) {
                this.createCraftFromShapedOrShapelessRecipe(e, prepareCustomCraft);
            }
        }
    }

    private void deleteInventory(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getView().getPlayer();
        CustomCraftConfig customCraftConfig = this.customCraft.getCustomCraftConfig().getSerializable();

        if (e.getRawSlot() < 0) {
            return;
        }

        if (customCraftConfig.getRecipes().size() >= e.getRawSlot() + 1) {
            RecipeConfig recipeConfig = customCraftConfig.getRecipes().get(e.getRawSlot());

            String deleteCraftTranslation = this.customCraft.getTranslationConfig().getDeleteCraft();
            player.sendMessage(MessageFormat.format(deleteCraftTranslation, recipeConfig.getCraftName()));

            // TODO : Use stream and not while with iterator
            /*ListUtils.convertIteratorToStream(this.customCraft.getServer().recipeIterator())
                    .filter(recipe -> recipe.getResult().equals(recipeConfig.getItemToCraft()))
                    .forEach(recipe -> {
                        System.out.println("recipe removed : " + recipe.getResult());
                    });*/

            Recipe recipe;
            Iterator<Recipe> recipeIterator = Bukkit.getServer().recipeIterator();
            while(recipeIterator.hasNext()) {
                recipe = recipeIterator.next();
                if (recipe.getResult().equals(recipeConfig.getItemToCraft())) {
                    recipeIterator.remove();
                }
            }

            customCraftConfig.getRecipes().remove(e.getRawSlot());
            customCraftConfig.getOptionItemStack().remove(recipeConfig.getItemToCraft());
            customCraft.getCustomCraftConfig().save(customCraftConfig);
            player.closeInventory();
        }
    }

    // TODO : Refactor chooseOption and use ENUM
    private void settingsInventory(InventoryClickEvent e, PrepareCustomCraft prepareCustomCraft) {
        e.setCancelled(true);

        Player player = (Player) e.getView().getPlayer();

        ItemStack currentItem = e.getCurrentItem();

        if (currentItem == null) {
            return;
        }

        if (e.getRawSlot() == 0) {
            OptionItemStackConfig optionItemStackConfig = prepareCustomCraft.getOptionItemStackConfig();
            optionItemStackConfig.setBlockCanBePlaced(!optionItemStackConfig.isBlockCanBePlaced());
            prepareCustomCraft.setOptionItemStackConfig(optionItemStackConfig);

            ItemStackUtils.setOptionLore(currentItem, optionItemStackConfig.isBlockCanBePlaced());
            e.getInventory().setItem(e.getRawSlot(), currentItem);
        }

        if (e.getRawSlot() == 1) {
            prepareCustomCraft.setHighlightItem(!prepareCustomCraft.getHighlightItem());

            ItemStackUtils.setOptionLore(currentItem, prepareCustomCraft.getHighlightItem());
            e.getInventory().setItem(e.getRawSlot(), currentItem);
        }

        if (e.getRawSlot() == QUIT_INVENTORY_CASE) {
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
            Inventory inventory = inventoryInitService.optionsInventory(player, prepareCustomCraft);
            player.openInventory(inventory);
        }

        if (e.getRawSlot() == VALID_INVENTORY_CASE) {
            if (prepareCustomCraft.getCraftResult() == null) {
                ItemStack itemStack = e.getInventory().getItem(VALID_INVENTORY_CASE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(Collections.singletonList(customCraft.getTranslationConfig().getMissingCraftItem()));
                itemStack.setItemMeta(itemMeta);
                e.getInventory().setItem(VALID_INVENTORY_CASE, itemStack);
                return;
            }

            // TODO : Cancel before add craft if one case have more than 1 item (technical limitation)

            addNewRecipe(player, prepareCustomCraft);

            player.closeInventory();
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
            craftResult.addUnsafeEnchantment(this.customCraft.customCraftEnchantment, 1);
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
