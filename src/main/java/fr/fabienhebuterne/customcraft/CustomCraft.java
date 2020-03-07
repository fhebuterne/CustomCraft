package fr.fabienhebuterne.customcraft;

import fr.fabienhebuterne.customcraft.commands.factory.CallCommandFactoryInit;
import fr.fabienhebuterne.customcraft.domain.RecipeService;
import fr.fabienhebuterne.customcraft.domain.config.Config;
import fr.fabienhebuterne.customcraft.domain.config.OptionItemStackConfig;
import fr.fabienhebuterne.customcraft.domain.config.ShapedRecipeConfig;
import fr.fabienhebuterne.customcraft.listeners.InventoryClickEventListener;
import fr.fabienhebuterne.customcraft.listeners.PlayerInteractEventListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomCraft extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(Config.class, "Config");
        ConfigurationSerialization.registerClass(ShapedRecipeConfig.class, "ShapedRecipeConfig");
        ConfigurationSerialization.registerClass(OptionItemStackConfig.class, "OptionItemStackConfig");
    }

    private CallCommandFactoryInit callCommandFactoryInit;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        // TODO : Add brigadier lib to implement command autocompletion in game

        this.callCommandFactoryInit = new CallCommandFactoryInit(this, "customcraft");
        saveDefaultConfig();
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

}
