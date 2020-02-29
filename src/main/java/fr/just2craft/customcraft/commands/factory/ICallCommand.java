package fr.just2craft.customcraft.commands.factory;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface ICallCommand {
    /**
     * @return Command name
     */
    String getName();

    /**
     * Player command use this method
     * @param server
     * @param player
     * @param commandLabel
     * @param cmd
     * @param args
     */
    void run(final Server server,
             final Player player,
             final String commandLabel,
             final Command cmd,
             final String[] args) throws Exception;

    /**
     * Other entities use this, like console, commandblocks...
     * @param server
     * @param commandLabel
     * @param cmd
     * @param args
     */
    void run(final Server server,
             final String commandLabel,
             final Command cmd,
             final String[] args) throws Exception;

    void setInstance(JavaPlugin instance);

    JavaPlugin getInstance();

    void setPermission(String permission);
}
