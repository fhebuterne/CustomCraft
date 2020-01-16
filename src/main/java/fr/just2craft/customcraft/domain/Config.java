package fr.just2craft.customcraft.domain;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("Config")
public class Config implements ConfigurationSerializable {
    // TODO : WIP
    public String test;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("test", test);
        return map;
    }
}
