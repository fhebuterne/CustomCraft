package fr.just2craft.customcraft.commands.factory;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public abstract class CallCommand implements ICallCommand {
    private final transient String name;
    protected transient JavaPlugin instance;
    protected transient String permission;
    protected static final Logger logger = Logger.getLogger("CustomCraft");

    protected CallCommand(final String name) {
        this.name = name;
    }

    @Override
    public void setInstance(final JavaPlugin instance) {
        this.instance = instance;
    }

    @Override
    public JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void setPermission(final String permission) {
        this.permission = permission;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run(final Server server,
                    final Player player,
                    final String commandLabel,
                    final Command cmd,
                    final String[] args) throws Exception {
        if (!player.hasPermission(permission)) {
            player.sendMessage("§cVous n'avez pas la permission d'utiliser cette commande !");
            return;
        }
        runFromPlayer(server, player, commandLabel, cmd, args);
    }

    protected void runFromPlayer(final Server server,
                    final Player player,
                    final String commandLabel,
                    final Command cmd,
                    final String[] args) throws Exception {

    }

    @Override
    public void run(final Server server,
             final String commandLabel,
             final Command cmd,
             final String[] args) throws Exception {
        runFromOther(server, commandLabel, cmd, args);
    }

    protected void runFromOther(final Server server,
                                 final String commandLabel,
                                 final Command cmd,
                                 final String[] args) throws Exception {

    }

}
