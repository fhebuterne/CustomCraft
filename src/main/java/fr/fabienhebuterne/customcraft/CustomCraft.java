package fr.fabienhebuterne.customcraft;

import fr.fabienhebuterne.customcraft.commands.factory.CallCommandFactoryInit;
import fr.fabienhebuterne.customcraft.domain.RecipeService;
import fr.fabienhebuterne.customcraft.domain.config.CustomCraftConfig;
import fr.fabienhebuterne.customcraft.domain.config.OptionItemStackConfig;
import fr.fabienhebuterne.customcraft.domain.config.RecipeConfig;
import fr.fabienhebuterne.customcraft.listeners.InventoryClickEventListener;
import fr.fabienhebuterne.customcraft.listeners.PlayerInteractEventListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class CustomCraft extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(CustomCraftConfig.class, "Config");
        ConfigurationSerialization.registerClass(RecipeConfig.class, "ShapedRecipeConfig");
        ConfigurationSerialization.registerClass(OptionItemStackConfig.class, "OptionItemStackConfig");
    }

    private CallCommandFactoryInit callCommandFactoryInit;

    // TODO : Put custom config in other class
    private File customCraftFile;
    private FileConfiguration customCraftFileConfiguration;

    // Used between inventory navigation to keep data before validation
    // TODO : Create object to stock tmp recipe and not only just craftName string ...
    private HashMap<UUID, String> tmpData = new HashMap<>();

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        // TODO : Add brigadier lib to implement command autocompletion in game
        createCustomCraftConfig();

        this.callCommandFactoryInit = new CallCommandFactoryInit(this, "customcraft");
        new RecipeService(this).loadCustomRecipe();
        this.getServer().getPluginManager().registerEvents(new InventoryClickEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(this), this);
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


    // TODO : Refactor to use custom service to add unlimited custom file config
    // Custom config to put custom craft info
    // config.yml is reserved to add futur custom options
    private void createCustomCraftConfig() {
        customCraftFile = new File(getDataFolder(), "customcraft.yml");
        if (!customCraftFile.exists()) {
            customCraftFile.getParentFile().mkdirs();
            saveResource("customcraft.yml", false);
        }

        customCraftFileConfiguration = new YamlConfiguration();
        try {
            customCraftFileConfiguration.load(customCraftFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getCustomConfig() {
        return this.customCraftFileConfiguration;
    }

    public CustomCraftConfig getCustomCraftConfig() {
        return customCraftFileConfiguration.getSerializable("customcraft", CustomCraftConfig.class);
    }

    public void saveCustomCraftConfig(CustomCraftConfig customCraftConfig) {
        try {
            customCraftFileConfiguration.set("customcraft", customCraftConfig);
            customCraftFileConfiguration.save(customCraftFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
