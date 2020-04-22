package fr.fabienhebuterne.customcraft.domain.config;

import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

@Data
public class TranslationConfig implements ConfigurationSerializable {

    // TODO : Refactor with "sub-category"
    private String onlyPlayerCommand;
    private String usageCommand;
    private String missingCraftItem;
    private String reload;
    private String cancel;
    private String settings;
    private String validate;
    private String backToPreviousMenu;
    private String toggleBlockPlace;
    private String toggleHighlight;
    private String statusEnabled;
    private String statusDisabled;
    private String recipeAlreadyExist;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("onlyPlayerCommand", onlyPlayerCommand);
        map.put("usageCommand", usageCommand);
        map.put("missingCraftItem", missingCraftItem);
        map.put("reload", reload);
        map.put("cancel", cancel);
        map.put("settings", settings);
        map.put("validate", reload);
        map.put("backToPreviousMenu", backToPreviousMenu);
        map.put("toggleBlockPlace", toggleBlockPlace);
        map.put("toggleHighlight", toggleHighlight);
        map.put("statusEnabled", statusEnabled);
        map.put("statusDisabled", statusDisabled);
        map.put("recipeAlreadyExist", recipeAlreadyExist);
        return map;
    }

    public static TranslationConfig deserialize(Map<String, Object> map) {
        TranslationConfig translationConfig = new TranslationConfig();
        translationConfig.onlyPlayerCommand = (String) map.get("onlyPlayerCommand");
        translationConfig.usageCommand = (String) map.get("usageCommand");
        translationConfig.missingCraftItem = (String) map.get("missingCraftItem");
        translationConfig.reload = (String) map.get("reload");
        translationConfig.cancel = (String) map.get("cancel");
        translationConfig.settings = (String) map.get("settings");
        translationConfig.validate = (String) map.get("validate");
        translationConfig.backToPreviousMenu = (String) map.get("backToPreviousMenu");
        translationConfig.toggleBlockPlace = (String) map.get("toggleBlockPlace");
        translationConfig.toggleHighlight = (String) map.get("toggleHighlight");
        translationConfig.statusEnabled = (String) map.get("statusEnabled");
        translationConfig.statusDisabled = (String) map.get("statusDisabled");
        translationConfig.recipeAlreadyExist = (String) map.get("recipeAlreadyExist");
        return translationConfig;
    }
}
