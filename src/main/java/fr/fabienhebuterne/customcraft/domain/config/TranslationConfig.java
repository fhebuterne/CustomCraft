package fr.fabienhebuterne.customcraft.domain.config;

import lombok.Data;

@Data
public class TranslationConfig {

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

}
