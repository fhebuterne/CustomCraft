package fr.fabienhebuterne.customcraft.exceptions;

import fr.fabienhebuterne.customcraft.CustomCraft;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

public class BadArgumentsException extends CustomException {

    public BadArgumentsException(CommandSender sender, String commandHelp) {
        String translation = CustomCraft.getTranslationConfig().getSerializable().getUsageCommand();
        sender.sendMessage(MessageFormat.format(translation, commandHelp));
    }

}
