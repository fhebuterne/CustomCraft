package fr.fabienhebuterne.customcraft.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import fr.fabienhebuterne.customcraft.domain.config.OptionItemStackConfig;
import fr.fabienhebuterne.customcraft.nms.ItemStackSerializer;
import fr.fabienhebuterne.customcraft.nms.NmsLoader;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemStackMapKeyAdapter implements JsonSerializer<HashMap<ItemStack, OptionItemStackConfig>>, JsonDeserializer<HashMap<ItemStack, OptionItemStackConfig>> {
    private final Gson gson;
    private final ItemStackSerializer itemStackSerializer;

    public ItemStackMapKeyAdapter() {
        this.itemStackSerializer = NmsLoader.loadNms(null);
        this.gson = new GsonBuilder().create();
    }

    @Override
    public HashMap<ItemStack, OptionItemStackConfig> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        HashMap<String, OptionItemStackConfig> map = gson.fromJson(jsonElement, new TypeToken<HashMap<String, OptionItemStackConfig>>(){}.getType());

        Map<ItemStack, OptionItemStackConfig> deserializeItemStack = map.entrySet().stream().flatMap(integerMapEntry -> {
            HashMap<ItemStack, OptionItemStackConfig> hashMap = new HashMap<>();
            hashMap.put(itemStackSerializer.deserializeItemStack(integerMapEntry.getKey()), integerMapEntry.getValue());
            return hashMap.entrySet().stream();
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new HashMap<>(deserializeItemStack);
    }

    @Override
    public JsonElement serialize(HashMap<ItemStack, OptionItemStackConfig> mapItemStack, Type typeOfSrc, JsonSerializationContext context) {
        Map<String, OptionItemStackConfig> jsonElementMapItemStack = mapItemStack.entrySet().stream().flatMap(integerItemStackEntry -> {
            HashMap<String, OptionItemStackConfig> hashMap = new HashMap<>();
            hashMap.put(itemStackSerializer.serializeItemStack(integerItemStackEntry.getKey()), integerItemStackEntry.getValue());
            return hashMap.entrySet().stream();
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return gson.toJsonTree(new HashMap<>(jsonElementMapItemStack), new TypeToken<HashMap<String, OptionItemStackConfig>>(){}.getType());
    }
}