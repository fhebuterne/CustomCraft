package fr.fabienhebuterne.customcraft.nms;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public interface ItemStackSerializer {
    String serializeItemStack(ItemStack itemStack);
    ItemStack deserializeItemStack(String itemStack);
    void loadCustomCraftEnchantment();
    Enchantment getCustomCraftEnchantment();
}
