package fr.fabienhebuterne.customcraft.domain.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class DefaultConfig implements ConfigurationSerializable {

    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("language", language);
        return map;
    }

    public static DefaultConfig deserialize(Map<String, Object> map) {
        DefaultConfig defaultConfig = new DefaultConfig();
        defaultConfig.language = (String) map.get("language");
        return defaultConfig;
    }

    @Override
    public String toString() {
        return "DefaultConfig{" +
                "language='" + language + '\'' +
                '}';
    }
}
