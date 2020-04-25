package fr.fabienhebuterne.customcraft.nms;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;

public class BaseReflection {
    public static Class<?> getNMSClass(String className) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.replace(".", ",").split(",")[3];
        String classLocation = "net.minecraft.server." + version + "." + className;
        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName(classLocation);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Unable to find reflection class " + classLocation + "!");
        }
        return nmsClass;
    }


    public static Class<?> getOBClass(String className) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.replace(".", ",").split(",")[3];
        String classLocation = "org.bukkit.craftbukkit." + version + "." + className;
        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName(classLocation);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Unable to find reflection class " + classLocation + "!");
        }
        return nmsClass;
    }

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
