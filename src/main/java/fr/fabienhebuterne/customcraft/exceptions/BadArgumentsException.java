package fr.fabienhebuterne.customcraft.exceptions;

import org.bukkit.command.CommandSender;

public class BadArgumentsException extends CustomException {

    public BadArgumentsException(CommandSender sender, String commandHelp) {
        sender.sendMessage("§cUsage : /customcraft " + commandHelp);
    }

}
