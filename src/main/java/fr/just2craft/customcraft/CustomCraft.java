package fr.just2craft.customcraft;

import fr.just2craft.customcraft.listeners.CraftItemEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomCraft extends JavaPlugin {

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new CraftItemEventListener(), this);
    }

}
