package fr.fabienhebuterne.customcraft.domain.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

// TODO : Add lombok lib to auto generate toString and getters
public class TranslationConfig implements ConfigurationSerializable {

    private String onlyPlayerCommand;
    private String usageCommand;
    private String missingCraftItem;

    public String getOnlyPlayerCommand() {
        return onlyPlayerCommand;
    }

    public String getUsageCommand() {
        return usageCommand;
    }

    public String getMissingCraftItem() {
        return missingCraftItem;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("onlyPlayerCommand", onlyPlayerCommand);
        map.put("usageCommand", usageCommand);
        map.put("missingCraftItem", missingCraftItem);
        return map;
    }

    public static TranslationConfig deserialize(Map<String, Object> map) {
        TranslationConfig translationConfig = new TranslationConfig();
        translationConfig.onlyPlayerCommand = (String) map.get("onlyPlayerCommand");
        translationConfig.usageCommand = (String) map.get("usageCommand");
        translationConfig.missingCraftItem = (String) map.get("missingCraftItem");
        return translationConfig;
    }

    @Override
    public String toString() {
        return "TranslationConfig{" +
                "onlyPlayerCommand='" + onlyPlayerCommand + '\'' +
                ", usageCommand='" + usageCommand + '\'' +
                ", missingCraftItem='" + missingCraftItem + '\'' +
                '}';
    }
}
