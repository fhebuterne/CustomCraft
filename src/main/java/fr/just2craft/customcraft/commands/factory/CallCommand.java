package fr.just2craft.customcraft.commands.factory;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public abstract class CallCommand implements ICallCommand {
    private final transient String name;
    protected transient JavaPlugin instance;
    protected static final Logger logger = Logger.getLogger("CustomCraft");

    protected CallCommand(final String name) {
        this.name = name;
    }

    @Override
    public void setInstance(final JavaPlugin instance) {
        this.instance = instance;
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
                    final String[] args) {

    }

    @Override
    public void run(final Server server,
             final String commandLabel,
             final Command cmd,
             final String[] args) {

    }

}
