package fr.fabienhebuterne.customcraft;

import fr.fabienhebuterne.customcraft.commands.factory.CallCommandFactoryInit;
import fr.fabienhebuterne.customcraft.domain.RecipeService;
import fr.fabienhebuterne.customcraft.domain.config.*;
import fr.fabienhebuterne.customcraft.listeners.InventoryClickEventListener;
import fr.fabienhebuterne.customcraft.listeners.PlayerInteractEventListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class CustomCraft extends JavaPlugin {

    // TODO : Find a solution to register without write new line to each file ...
    static {
        ConfigurationSerialization.registerClass(CustomCraftConfig.class, "CustomCraftConfig");
        ConfigurationSerialization.registerClass(RecipeConfig.class, "ShapedRecipeConfig");
        ConfigurationSerialization.registerClass(OptionItemStackConfig.class, "OptionItemStackConfig");
        ConfigurationSerialization.registerClass(DefaultConfig.class, "DefaultConfig");
        ConfigurationSerialization.registerClass(TranslationConfig.class, "TranslationConfig");
    }

    private CallCommandFactoryInit<CustomCraft> callCommandFactoryInit;

    private ConfigService<CustomCraftConfig> customCraftConfig;
    private ConfigService<DefaultConfig> defaultConfig;
    private static ConfigService<TranslationConfig> translationConfig;

    // Used between inventory navigation to keep data before validation
    // TODO : Create object to stock tmp recipe and not only just craftName string ...
    private HashMap<UUID, String> tmpData = new HashMap<>();

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        // TODO : Add brigadier lib to implement command autocompletion in game
        this.loadAllConfig();

        this.callCommandFactoryInit = new CallCommandFactoryInit<>(this, "customcraft");
        new RecipeService(this).loadCustomRecipe();
        this.getServer().getPluginManager().registerEvents(new InventoryClickEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(this), this);
    }

    public void loadAllConfig() {
        customCraftConfig = new ConfigService<>(this, "customcraft", "customcraft", CustomCraftConfig.class);
        customCraftConfig.createOrLoadConfig(false);

        defaultConfig = new ConfigService<>(this, "config", "configTest", DefaultConfig.class);
        defaultConfig.createOrLoadConfig(true);

        String language = defaultConfig.getSerializable().getLanguage();

        translationConfig = new ConfigService<>(this, "translation-" + language, "translation", TranslationConfig.class);
        translationConfig.createOrLoadConfig(true);
    }

    @Override
    public boolean onCommand(final CommandSender sender,
                             final Command command,
                             final String commandLabel,
                             final String[] args) {
        return this.callCommandFactoryInit.onCommandCustomCraft(
                sender,
                command,
                commandLabel,
                args,
                CustomCraft.class.getClassLoader(),
                "fr.fabienhebuterne.customcraft.commands",
                "customcraft.",
                true
        );
    }

    public HashMap<UUID, String> getTmpData() {
        return tmpData;
    }

    public void addTmpData(UUID uuid, String craftName) {
        tmpData.put(uuid, craftName);
    }

    public void removeTmpData(UUID uuid) {
        tmpData.remove(uuid);
    }

    public ConfigService<CustomCraftConfig> getCustomCraftConfig() {
        return customCraftConfig;
    }

    public static ConfigService<TranslationConfig> getTranslationConfig() {
        return translationConfig;
    }

    public ConfigService<DefaultConfig> getDefaultConfig() {
        return defaultConfig;
    }
}
