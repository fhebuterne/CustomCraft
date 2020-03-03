package fr.just2craft.customcraft.listeners;

import fr.just2craft.customcraft.CustomCraft;
import fr.just2craft.customcraft.domain.Config;
import fr.just2craft.customcraft.domain.ShapedRecipeConfig;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.*;
import java.util.stream.Collectors;

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

            addNewRecipe(craftCaseRecipe, idCraftCaseRecipse, new ItemStack(Material.APPLE), e.getClickedInventory().getItem(0).getItemMeta().getDisplayName());
        }
    }

    // TODO : Deplace this method in separated class
    private void addNewRecipe(ArrayList<ItemStack> craftCaseRecipe, HashMap<Integer, ItemStack> idCraftCaseRecipse, ItemStack resultCraft, String craftName) {
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(this.customCraft, craftName), resultCraft);

        // TODO : Other class to init row ?
        ArrayList<String> grid = getGrid(craftCaseRecipe, idCraftCaseRecipse);

        shapedRecipe.shape(grid.get(0), grid.get(1), grid.get(2));

        idCraftCaseRecipse.forEach((integer, itemStack) -> {
            if (itemStack != null && itemStack.getData() != null) {
                shapedRecipe.setIngredient(integer.toString().charAt(0), new RecipeChoice.ExactChoice(itemStack));
            }
        });

        ShapedRecipeConfig shapedRecipeConfig = new ShapedRecipeConfig();
        shapedRecipeConfig.setItemToCraft(resultCraft);
        shapedRecipeConfig.setGrid(grid);

        HashMap<Integer, ItemStack> gridHashMap = new HashMap<>();
        idCraftCaseRecipse.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .forEach(entry -> gridHashMap.put(entry.getKey(), entry.getValue()));

        shapedRecipeConfig.setGridSequence(gridHashMap);

        // Save into config
        Config customcraft = this.customCraft.getConfig().getSerializable("customcraft", Config.class);

        if (customcraft == null) {
            customcraft = new Config();
        }

        customcraft.addShapedRecipe(shapedRecipeConfig);
        this.customCraft.getConfig().set("customcraft", customcraft);
        this.customCraft.saveConfig();

        this.customCraft.getServer().addRecipe(shapedRecipe);
    }

    private ArrayList<String> getGrid(ArrayList<ItemStack> craftCaseRecipe, HashMap<Integer, ItemStack> idCraftCaseRecipse) {
        String completeRow = craftCaseRecipe.stream().map(itemStack -> {
            String row = "";
            if (itemStack == null) {
                row += " ";
            } else {
                row += getKeysByValue(idCraftCaseRecipse, itemStack).get(0);
            }
            return row;
        }).collect(Collectors.joining());

        String[] splitCompleteRow = completeRow.split("(?<=\\G...)");

        return new ArrayList<>(Arrays.asList(splitCompleteRow));
    }

    // TODO : Put in utils class
    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
