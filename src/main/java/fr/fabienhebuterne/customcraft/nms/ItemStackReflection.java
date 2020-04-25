package fr.fabienhebuterne.customcraft.nms;

import com.google.common.io.BaseEncoding;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;

import static fr.fabienhebuterne.customcraft.nms.BaseReflection.getNMSClass;
import static fr.fabienhebuterne.customcraft.nms.BaseReflection.getOBClass;

public class ItemStackReflection {

    public static String serializeItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return "null";
        }

        ByteArrayOutputStream outputStream = null;
        try {
            Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
            Constructor<?> nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor();
            Object nbtTagCompound = nbtTagCompoundConstructor.newInstance();
            Object nmsItemStack = getOBClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);
            getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass).invoke(nmsItemStack, nbtTagCompound);
            outputStream = new ByteArrayOutputStream();
            getNMSClass("NBTCompressedStreamTools").getMethod("a", nbtTagCompoundClass, OutputStream.class).invoke(null, nbtTagCompound, outputStream);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return BaseEncoding.base64().encode(outputStream.toByteArray());
    }

    public static ItemStack deserializeItemStack(String itemStackString) {
        if (itemStackString.equals("null")) {
            return null;
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseEncoding.base64().decode(itemStackString));

        Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
        Class<?> nmsItemStackClass = getNMSClass("ItemStack");
        Object nbtTagCompound;
        ItemStack itemStack = null;
        try {
            nbtTagCompound = getNMSClass("NBTCompressedStreamTools").getMethod("a", InputStream.class).invoke(null, inputStream);
            Object craftItemStack = nmsItemStackClass.getMethod("a", nbtTagCompoundClass).invoke(null, nbtTagCompound);
            itemStack = (ItemStack) getOBClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, craftItemStack);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return itemStack;
    }

}
