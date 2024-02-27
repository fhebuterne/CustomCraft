package fr.fabienhebuterne.customcraft.nms;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import static fr.fabienhebuterne.customcraft.CustomCraft.PLUGIN_NAME;
import static fr.fabienhebuterne.customcraft.CustomCraft.plugin;

public class NmsLoader {

    public static ItemStackSerializer loadNms() {
        String currentVersion = Bukkit.getServer().getClass().getPackage().getName()
                .replace(".", ",").split(",")[3];

        if (currentVersion == null) {
            Bukkit.getLogger().severe("Your server version isn't compatible with CustomCraft");
            if (plugin != null) {
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }

        NamespacedKey namespacedKey = new NamespacedKey(plugin, PLUGIN_NAME);

        return switch (currentVersion) {
            case "v1_18_R2" -> new ItemStackSerializer_1_18_R2(namespacedKey);
            case "v1_19_R1" -> new ItemStackSerializer_1_19_R1(namespacedKey);
            case "v1_19_R2" -> new ItemStackSerializer_1_19_R2(namespacedKey);
            case "v1_19_R3" -> new ItemStackSerializer_1_19_R3(namespacedKey);
            case "v1_20_R1" -> new ItemStackSerializer_1_20_R1(namespacedKey);
            case "v1_20_R2" -> new ItemStackSerializer_1_20_R2(namespacedKey);
            case "v1_20_R3" -> new ItemStackSerializer_1_20_R3(namespacedKey);
            default -> {
                Bukkit.getLogger().severe("Your server version isn't compatible with CustomCraft");
                if (plugin != null) {
                    plugin.getServer().getPluginManager().disablePlugin(plugin);
                }
                throw new IllegalStateException("Your server version isn't compatible with CustomCraft");
            }
        };
    }

}
