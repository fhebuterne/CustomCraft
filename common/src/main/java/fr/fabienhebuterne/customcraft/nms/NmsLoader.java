package fr.fabienhebuterne.customcraft.nms;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class NmsLoader {

    public static ItemStackSerializer loadNms(Plugin plugin) {
        String currentVersion = Bukkit.getServer().getClass().getPackage().getName()
                .replace(".", ",").split(",")[3];

        if (currentVersion == null) {
            Bukkit.getLogger().severe("Your server version isn't compatible with CustomCraft");
            if (plugin != null) {
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }

        return switch (currentVersion) {
            case "v1_18_R2" -> new ItemStackSerializer_1_18_R2();
            case "v1_19_R1" -> new ItemStackSerializer_1_19_R1();
            case "v1_19_R2" -> new ItemStackSerializer_1_19_R2();
            case "v1_19_R3" -> new ItemStackSerializer_1_19_R3();
            case "v1_20_R1" -> new ItemStackSerializer_1_20_R1();
            case "v1_20_R2" -> new ItemStackSerializer_1_20_R2();
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
