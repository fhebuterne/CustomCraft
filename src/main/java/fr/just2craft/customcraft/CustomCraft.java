package fr.just2craft.customcraft;

import fr.just2craft.customcraft.commands.factory.CallCommandFactoryInit;
import fr.just2craft.customcraft.domain.Config;
import fr.just2craft.customcraft.domain.ShapedRecipeConfig;
import fr.just2craft.customcraft.listeners.CraftItemEventListener;
import fr.just2craft.customcraft.listeners.InventoryClickEventListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;

public class CustomCraft extends JavaPlugin {
    private CallCommandFactoryInit callCommandFactoryInit;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        this.callCommandFactoryInit = new CallCommandFactoryInit(this, "customcraft");
        ConfigurationSerialization.registerClass(Config.class, "Config");
        saveDefaultConfig();
        this.loadCustomRecipe();
        Bukkit.getServer().getPluginManager().registerEvents(new CraftItemEventListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickEventListener(), this);
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
                "fr.just2craft.customcraft.commands",
                "customcraft.",
                true
        );
    }

    private void loadCustomRecipe() {
        ItemStack itemStack = new ItemStack(Material.APPLE);
        itemStack.setAmount(2);
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(this, "customcraft"), itemStack);

        shapedRecipe.shape(" E ", " E ", " S ");
        shapedRecipe.setIngredient('E', Material.EMERALD);
        shapedRecipe.setIngredient('S', Material.STICK);

        ShapedRecipeConfig shapedRecipeConfig = new ShapedRecipeConfig();
        shapedRecipeConfig.setItemToCraft(itemStack);
        shapedRecipeConfig.setGrid(Arrays.asList(" E ", " E ", " S "));
        HashMap<Character, String> gridHashMap = new HashMap<>();
        gridHashMap.put('E', Material.EMERALD.toString());
        gridHashMap.put('S', Material.STICK.toString());
        shapedRecipeConfig.setGridSequence(gridHashMap);

        Config customcraft = getConfig().getSerializable("customcraft", Config.class);

        if (customcraft == null) {
            customcraft = new Config();
        }

        customcraft.addShapedRecipe(shapedRecipeConfig);
        getConfig().set("customcraft", customcraft);
        saveConfig();

        this.getServer().addRecipe(shapedRecipe);
    }

}
