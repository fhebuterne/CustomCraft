package fr.fabienhebuterne.customcraft;

import fr.fabienhebuterne.customcraft.domain.CustomCraftEnchantment;
import fr.fabienhebuterne.customcraft.commands.factory.CallCommandFactoryInit;
import fr.fabienhebuterne.customcraft.domain.PrepareCustomCraft;
import fr.fabienhebuterne.customcraft.domain.config.ConfigService;
import fr.fabienhebuterne.customcraft.domain.config.CustomCraftConfig;
import fr.fabienhebuterne.customcraft.domain.config.DefaultConfig;
import fr.fabienhebuterne.customcraft.domain.config.TranslationConfig;
import fr.fabienhebuterne.customcraft.domain.recipe.RecipeLoadService;
import fr.fabienhebuterne.customcraft.listeners.InventoryClickEventListener;
import fr.fabienhebuterne.customcraft.listeners.PlayerInteractEventListener;
import fr.fabienhebuterne.customcraft.nms.BaseReflection;
import fr.fabienhebuterne.customcraft.nms.ItemStackSerializer;
import fr.fabienhebuterne.customcraft.nms.ItemStackSerializer_1_18_R2;
import fr.fabienhebuterne.customcraft.nms.ItemStackSerializer_1_19_R1;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class CustomCraft extends JavaPlugin {

    public ItemStackSerializer itemStackSerializer = null;
    private CallCommandFactoryInit<CustomCraft> callCommandFactoryInit;
    private ConfigService<CustomCraftConfig> customCraftConfig;
    private ConfigService<DefaultConfig> defaultConfig;
    private static ConfigService<TranslationConfig> translationConfig;

    // Used between inventory navigation to keep data before validation
    // TODO : Maybe HashMap<UUID, HashMap<ActionType, T>> tmpData ?
    private HashMap<UUID, PrepareCustomCraft> tmpData = new HashMap<>();

    public CustomCraftEnchantment customCraftEnchantment;

    public static final String PLUGIN_NAME = "CustomCraft";

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        customCraftEnchantment = new CustomCraftEnchantment(new NamespacedKey(this, PLUGIN_NAME));
        BaseReflection.enchantmentRegistration(customCraftEnchantment);

        loadNms();

        // TODO : Add brigadier lib to implement command autocompletion in game
        try {
            this.loadAllConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.callCommandFactoryInit = new CallCommandFactoryInit<>(this, PLUGIN_NAME.toLowerCase());
        new RecipeLoadService(this).loadCustomRecipe();
        this.getServer().getPluginManager().registerEvents(new InventoryClickEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(this), this);
    }

    private void loadNms() {
        String currentVersion = Bukkit.getServer().getClass().getPackage().getName()
                .replace(".", ",").split(",")[3];

        if (currentVersion == null) {
            Bukkit.getLogger().severe("Your server version isn't compatible with PickSpawner");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        this.itemStackSerializer = switch (currentVersion) {
            case "v1_18_R2" -> new ItemStackSerializer_1_18_R2();
            case "v1_19_R1" -> new ItemStackSerializer_1_19_R1();
            default -> {
                Bukkit.getLogger().severe("Your server version isn't compatible with CustomCraft");
                this.getServer().getPluginManager().disablePlugin(this);
                throw new IllegalStateException("Your server version isn't compatible with CustomCraft");
            }
        };
    }

    public void loadAllConfig() throws IOException {
        customCraftConfig = new ConfigService<>(this, "fr/fabienhebuterne/customcraft", CustomCraftConfig.class);
        customCraftConfig.createOrLoadConfig(false);

        defaultConfig = new ConfigService<>(this, "config", DefaultConfig.class);
        defaultConfig.createOrLoadConfig(true);

        String language = defaultConfig.getSerializable().getLanguage();

        translationConfig = new ConfigService<>(this, "translation-" + language, TranslationConfig.class);
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
                PLUGIN_NAME.toLowerCase() + ".",
                true
        );
    }

    public HashMap<UUID, PrepareCustomCraft> getTmpData() {
        return tmpData;
    }

    public void addTmpData(UUID uuid, PrepareCustomCraft prepareCustomCraft) {
        tmpData.put(uuid, prepareCustomCraft);
    }

    public void removeTmpData(UUID uuid) {
        tmpData.remove(uuid);
    }

    public ConfigService<CustomCraftConfig> getCustomCraftConfig() {
        return customCraftConfig;
    }

    public TranslationConfig getTranslationConfig() {
        return translationConfig.getSerializable();
    }
    public static TranslationConfig getStaticTranslationConfig() {
        return translationConfig.getSerializable();
    }

    public ConfigService<DefaultConfig> getDefaultConfig() {
        return defaultConfig;
    }
}
