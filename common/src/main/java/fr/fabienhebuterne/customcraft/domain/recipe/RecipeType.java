package fr.fabienhebuterne.customcraft.domain.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum RecipeType {
    SHAPED_RECIPE("SHAPED_RECIPE", 0, new ItemStack(Material.CRAFTING_TABLE)),
    SHAPELESS_RECIPE("SHAPELESS_RECIPE", 1, new ItemStack(Material.CRAFTING_TABLE));

    private String nameType;
    private int invIndex;
    private ItemStack itemStack;

    RecipeType(String nameType, int invIndex, ItemStack itemStack) {
        this.nameType = nameType;
        this.invIndex = invIndex;

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.nameType);
        itemStack.setItemMeta(itemMeta);

        this.itemStack = itemStack;
    }

    public String getNameType() {
        return nameType;
    }

    public int getInvIndex() {
        return invIndex;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String toString() {
        return "RecipeType{" +
                "nameType='" + nameType + '\'' +
                ", invIndex=" + invIndex +
                ", itemStack=" + itemStack +
                '}';
    }
}
