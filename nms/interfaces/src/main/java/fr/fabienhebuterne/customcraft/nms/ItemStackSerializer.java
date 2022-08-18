package fr.fabienhebuterne.customcraft.nms;

import org.bukkit.inventory.ItemStack;

public interface ItemStackSerializer {
    public String serializeItemStack(ItemStack itemStack);
    public ItemStack deserializeItemStack(String itemStack);
}
