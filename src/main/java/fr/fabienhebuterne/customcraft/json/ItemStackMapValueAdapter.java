package fr.fabienhebuterne.customcraft.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import fr.fabienhebuterne.customcraft.nms.ItemStackReflection;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemStackMapValueAdapter implements JsonSerializer<HashMap<Integer, ItemStack>>, JsonDeserializer<HashMap<Integer, ItemStack>> {
    private Gson gson;

    public ItemStackMapValueAdapter() {
        gson = new GsonBuilder().create();
    }

    @Override
    public HashMap<Integer, ItemStack> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        HashMap<Integer, String> map = gson.fromJson(jsonElement, new TypeToken<HashMap<Integer, String>>(){}.getType());

        Map<Integer, ItemStack> deserializeItemStack = map.entrySet().stream().flatMap(integerMapEntry -> {
            HashMap<Integer, ItemStack> hashMap = new HashMap<>();
            hashMap.put(integerMapEntry.getKey(), ItemStackReflection.deserializeItemStack(integerMapEntry.getValue()));
            return hashMap.entrySet().stream();
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new HashMap<>(deserializeItemStack);
    }

    @Override
    public JsonElement serialize(HashMap<Integer, ItemStack> mapItemStack, Type typeOfSrc, JsonSerializationContext context) {
        Map<Integer, String> jsonElementMapItemStack = mapItemStack.entrySet().stream().flatMap(integerItemStackEntry -> {
            HashMap<Integer, String> hashMap = new HashMap<>();
            hashMap.put(integerItemStackEntry.getKey(), ItemStackReflection.serializeItemStack(integerItemStackEntry.getValue()));
            return hashMap.entrySet().stream();
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return gson.toJsonTree(new HashMap<>(jsonElementMapItemStack), new TypeToken<HashMap<Integer, String>>(){}.getType());
    }
}