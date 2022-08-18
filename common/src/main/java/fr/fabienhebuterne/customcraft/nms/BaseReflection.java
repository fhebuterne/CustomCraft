package fr.fabienhebuterne.customcraft.nms;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;

public class BaseReflection {
    public static void enchantmentRegistration(Enchantment enchantment) {
        try {
            Field acceptNewEnchantement = Enchantment.class.getDeclaredField("acceptingNew");
            acceptNewEnchantement.setAccessible(true);
            acceptNewEnchantement.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Enchantment.stopAcceptingRegistrations();
        }
    }
}
