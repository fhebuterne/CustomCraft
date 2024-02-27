package fr.fabienhebuterne.customcraft.json;

import com.google.gson.*;
import fr.fabienhebuterne.customcraft.nms.ItemStackSerializer;
import fr.fabienhebuterne.customcraft.nms.NmsLoader;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    private Gson gson;
    private final ItemStackSerializer itemStackSerializer;

    public ItemStackAdapter() {
        this.itemStackSerializer = NmsLoader.loadNms();
        this.gson = new GsonBuilder().create();
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (this.gson == null) {
            this.gson = new GsonBuilder().create();
        }
        String itemStackBase64 = this.gson.fromJson(jsonElement, String.class);
        return this.itemStackSerializer.deserializeItemStack(itemStackBase64);
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        if (this.gson == null) {
            this.gson = new GsonBuilder().create();
        }
        String base64 = this.itemStackSerializer.serializeItemStack(itemStack);
        return this.gson.toJsonTree(base64);
    }
}