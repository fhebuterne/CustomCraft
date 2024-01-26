package fr.fabienhebuterne.customcraft.nms;

import com.google.common.io.BaseEncoding;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTReadLimiter;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackSerializer_1_20_R3 implements ItemStackSerializer {
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
}
