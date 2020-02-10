package fr.just2craft.customcraft.commands;

import fr.just2craft.customcraft.commands.factory.CallCommand;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandCreate extends CallCommand {
    public CommandCreate() {
        super("create");
    }

    @Override
    public void run(Server server, String commandLabel, Command cmd, String[] args) {
        this.instance.getServer().broadcastMessage("TEST broadcast from server");
        System.out.println("TEST command create server");
    }

    @Override
    public void run(Server server, Player player, String commandLabel, Command cmd, String[] args) {
        this.instance.getServer().broadcastMessage("TEST broadcast from player");
        System.out.println("TEST command create player");
    }
}
