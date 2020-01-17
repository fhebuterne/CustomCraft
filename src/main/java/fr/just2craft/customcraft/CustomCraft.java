package fr.just2craft.customcraft;

import fr.just2craft.customcraft.domain.Config;
import fr.just2craft.customcraft.listeners.CraftItemEventListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class CustomCraft extends JavaPlugin {

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(Config.class, "config");

        saveDefaultConfig();
        Config config = getConfig().getObject("customcraft", Config.class);
        System.out.println(config);

        this.loadCustomRecipe();
        Bukkit.getServer().getPluginManager().registerEvents(new CraftItemEventListener(), this);
    }

    private void loadCustomRecipe() {
        ItemStack itemStack = new ItemStack(Material.APPLE);
        itemStack.setAmount(2);
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(this, "customcraft"), itemStack);

        shapedRecipe.shape(" E ", " E ", " S ");
        shapedRecipe.setIngredient('E', Material.EMERALD);
        shapedRecipe.setIngredient('S', Material.STICK);

        this.getServer().addRecipe(shapedRecipe);
    }

}
