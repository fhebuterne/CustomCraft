package fr.fabienhebuterne.customcraft.exceptions;

import fr.fabienhebuterne.customcraft.CustomCraft;
import org.bukkit.command.CommandSender;

public class OnlyPlayerCommandException extends CustomException {

    public OnlyPlayerCommandException(CommandSender sender) {
        sender.sendMessage(CustomCraft.getTranslationConfig().getSerializable().getOnlyPlayerCommand());
    }

}
