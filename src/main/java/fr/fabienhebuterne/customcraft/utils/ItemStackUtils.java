package fr.fabienhebuterne.customcraft.utils;

import fr.fabienhebuterne.customcraft.CustomCraft;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class ItemStackUtils extends ItemStack {

    public static String LORE_OPTION_ENABLED = CustomCraft.getStaticTranslationConfig().getStatusEnabled();
    public static String LORE_OPTION_DISABLED = CustomCraft.getStaticTranslationConfig().getStatusDisabled();

    public ItemStackUtils(Material material) {
        super(material);
    }

    public void setName(String displayName) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
        }
        setItemMeta(itemMeta);
    }

    public void setLore(List<String> lore) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore(lore);
        }
        setItemMeta(itemMeta);
    }

    public void setOptionLore(boolean toggle) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta != null) {
            if (toggle) {
                itemMeta.setLore(Collections.singletonList(LORE_OPTION_ENABLED));
            } else {
                itemMeta.setLore(Collections.singletonList(LORE_OPTION_DISABLED));
            }
        }
        setItemMeta(itemMeta);
    }

    public static void setOptionLore(ItemStack itemStack, boolean toggle) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            if (toggle) {
                itemMeta.setLore(Collections.singletonList(LORE_OPTION_ENABLED));
            } else {
                itemMeta.setLore(Collections.singletonList(LORE_OPTION_DISABLED));
            }
        }
        itemStack.setItemMeta(itemMeta);
    }

}
