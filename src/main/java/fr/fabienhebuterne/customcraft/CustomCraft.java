package fr.fabienhebuterne.customcraft;

import fr.fabienhebuterne.customcraft.commands.factory.CallCommandFactoryInit;
import fr.fabienhebuterne.customcraft.domain.CustomCraftEnchantment;
import fr.fabienhebuterne.customcraft.domain.PrepareCustomCraft;
import fr.fabienhebuterne.customcraft.domain.config.*;
import fr.fabienhebuterne.customcraft.domain.recipe.RecipeLoadService;
import fr.fabienhebuterne.customcraft.listeners.InventoryClickEventListener;
import fr.fabienhebuterne.customcraft.listeners.PlayerInteractEventListener;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
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
        ConfigurationSerialization.registerClass(ItemStack.class, "ItemStack");
        ConfigurationSerialization.registerClass(ItemMeta.class, "ItemMeta");
    }

    private CallCommandFactoryInit<CustomCraft> callCommandFactoryInit;
    private ConfigService<CustomCraftConfig> customCraftConfig;
    private ConfigService<DefaultConfig> defaultConfig;
    private static ConfigService<TranslationConfig> translationConfig;

    // Used between inventory navigation to keep data before validation
    private HashMap<UUID, PrepareCustomCraft> tmpData = new HashMap<>();

    public CustomCraftEnchantment customCraftEnchantment;

    public static final String PLUGIN_NAME = "CustomCraft";

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        customCraftEnchantment = new CustomCraftEnchantment(new NamespacedKey(this, PLUGIN_NAME));
        this.enchantmentRegistration();

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

    public void loadAllConfig() throws IOException {
        customCraftConfig = new ConfigService<>(this, "customcraft", "customcraft", CustomCraftConfig.class);
        customCraftConfig.createOrLoadConfig(false);

        /*ItemStack itemStack = new ItemStack(Material.ACACIA_LOG);
        itemStack.addUnsafeEnchantment(customCraftEnchantment, 1);

        RecipeConfig recipeConfig = new RecipeConfig();
        recipeConfig.setItemToCraft(itemStack);

        CustomCraftConfig customCraftConfig = new CustomCraftConfig();
        customCraftConfig.addRecipe(recipeConfig);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(customCraftConfig));*/

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

    private void enchantmentRegistration() {
        try {
            Field acceptNewEnchantement = Enchantment.class.getDeclaredField("acceptingNew");
            acceptNewEnchantement.setAccessible(true);
            acceptNewEnchantement.set(null, true);
            Enchantment.registerEnchantment(this.customCraftEnchantment);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Enchantment.stopAcceptingRegistrations();
        }
    }
}
