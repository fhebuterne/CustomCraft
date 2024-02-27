package fr.fabienhebuterne.customcraft.nms;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class CustomCraftEnchantmentLegacy extends Enchantment {

    private NamespacedKey key;

    public CustomCraftEnchantmentLegacy(NamespacedKey key) {
        super();
        this.key = key;
    }

    @Override
    public String getName() {
        return getKey().getKey();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
