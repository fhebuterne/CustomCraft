package fr.fabienhebuterne.customcraft.nms;

import com.google.common.io.BaseEncoding;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTReadLimiter;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class ItemStackSerializer_1_20_R3 implements ItemStackSerializer {

    CustomCraftEnchantmentLegacy customCraftEnchantment;

    ItemStackSerializer_1_20_R3(NamespacedKey namespacedKey) {
        customCraftEnchantment = new CustomCraftEnchantmentLegacy(namespacedKey);
    }

    @Override
    public String serializeItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return "null";
        }

        ByteArrayOutputStream outputStream = null;

        try {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            net.minecraft.world.item.ItemStack itemStackNms = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound nbtToSave = itemStackNms.b(nbtTagCompound);
            outputStream = new ByteArrayOutputStream();
            NBTCompressedStreamTools.a(nbtToSave, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BaseEncoding.base64().encode(outputStream.toByteArray());
    }

    @Override
    public ItemStack deserializeItemStack(String itemStack) {
        if (itemStack.equals("null")) {
            return null;
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseEncoding.base64().decode(itemStack));

        try {
            NBTTagCompound tagCompound = NBTCompressedStreamTools.a(inputStream, NBTReadLimiter.a());
            net.minecraft.world.item.ItemStack itemStackNms = net.minecraft.world.item.ItemStack.a(tagCompound);
            return CraftItemStack.asBukkitCopy(itemStackNms);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void loadCustomCraftEnchantment() {
        Bukkit.getLogger().info("[CUSTOMCRAFT] Start to loaded custom enchantment");
        // unfreeze registry
        try {
            Field enchantFreezeField = BuiltInRegistries.f.getClass().getDeclaredField("l");
            enchantFreezeField.setAccessible(true);
            enchantFreezeField.setBoolean(BuiltInRegistries.f, false);
            Field enchantMapField = BuiltInRegistries.f.getClass().getDeclaredField("m");
            enchantMapField.setAccessible(true);
            enchantMapField.set(BuiltInRegistries.f, new IdentityHashMap<>());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // init nms enchant
        CustomCraftEnchantmentVanilla customCraftEnchantment = new CustomCraftEnchantmentVanilla();
        IRegistry.a(BuiltInRegistries.f, "customcraft:customcraft", customCraftEnchantment);

        // freeze registry
        BuiltInRegistries.f.l();
        Bukkit.getLogger().info("[CUSTOMCRAFT] Finish to loaded custom enchantment");
    }

    @Override
    public Enchantment getCustomCraftEnchantment() {
        return customCraftEnchantment;
    }
}
