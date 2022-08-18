package fr.fabienhebuterne.customcraft.exceptions;

import org.bukkit.command.CommandSender;

public class CustomCraftAlreadyExistException extends CustomException {

    public CustomCraftAlreadyExistException(CommandSender sender) {
        sender.sendMessage("§cThis craft name already exist, please choose other name or delete existing !");
    }

}
