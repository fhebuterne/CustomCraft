package fr.fabienhebuterne.customcraft.exceptions;

import org.bukkit.command.CommandSender;

public class OnlyPlayerCommandException extends CustomException {

    public OnlyPlayerCommandException(CommandSender sender) {
        sender.sendMessage("§cThis command is not available in console !");
    }

}
