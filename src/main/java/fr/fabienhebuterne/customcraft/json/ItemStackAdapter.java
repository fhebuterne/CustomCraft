package fr.fabienhebuterne.customcraft.json;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

import static fr.fabienhebuterne.customcraft.nms.ItemStackReflection.deserializeItemStack;
import static fr.fabienhebuterne.customcraft.nms.ItemStackReflection.serializeItemStack;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    private Gson gson;

    public ItemStackAdapter() {
        gson = new GsonBuilder().create();
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        String itemStackBase64 = gson.fromJson(jsonElement, String.class);

        return deserializeItemStack(itemStackBase64);
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        String base64 = serializeItemStack(itemStack);

        return gson.toJsonTree(base64);
    }
}