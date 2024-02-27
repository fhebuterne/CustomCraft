package fr.fabienhebuterne.customcraft.nms;

import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentSlotType;

public class CustomCraftEnchantmentVanilla extends Enchantment {

    public CustomCraftEnchantmentVanilla() {
        super(Rarity.d, EnchantmentSlotType.n, new EnumItemSlot[]{});
    }

}
