package fr.fabienhebuterne.customcraft.json;

import com.google.gson.*;
import fr.fabienhebuterne.customcraft.nms.ItemStackSerializer;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    private final Gson gson;
    private final ItemStackSerializer itemStackSerializer;

    public ItemStackAdapter(ItemStackSerializer itemStackSerializer) {
        this.gson = new GsonBuilder().create();
        this.itemStackSerializer = itemStackSerializer;
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        String itemStackBase64 = gson.fromJson(jsonElement, String.class);
        return itemStackSerializer.deserializeItemStack(itemStackBase64);
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        String base64 = itemStackSerializer.serializeItemStack(itemStack);
        return gson.toJsonTree(base64);
    }
}