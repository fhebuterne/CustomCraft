package fr.fabienhebuterne.customcraft.commands;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.commands.factory.CallCommand;
import fr.fabienhebuterne.customcraft.domain.RecipeInventoryService;
import fr.fabienhebuterne.customcraft.exceptions.BadArgumentsException;
import fr.fabienhebuterne.customcraft.exceptions.OnlyPlayerCommandException;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandCreate extends CallCommand<CustomCraft> {
    public final static ArrayList<Integer> CRAFT_CASES = new ArrayList<>(Arrays.asList(1, 2, 3, 10, 11, 12, 19, 20, 21));
    public final static Integer RESULT_CRAFT_CASE = 15;
    public final static Integer QUIT_INVENTORY_CASE = 8;
    public final static Integer VALID_INVENTORY_CASE = 26;

    public CommandCreate() {
        super("create");
    }

    protected void runFromOther(Server server, CommandSender commandSender, String commandLabel, Command cmd, String[] args) throws Exception {
        throw new OnlyPlayerCommandException(server.getConsoleSender());
    }

    protected void runFromPlayer(Server server, Player player, String commandLabel, Command cmd, String[] args) throws BadArgumentsException {
        // TODO : Add an other line to edit some options like block place etc...
        // TODO : Check if craftName already exist
        if (args.length == 1) {
            throw new BadArgumentsException(player, "create <craftName>");
        }

        CustomCraft customCraft = this.getInstance();
        customCraft.addTmpData(player.getUniqueId(), args[1]);

        new RecipeInventoryService(customCraft).openChooseCraftTypeInventory(player);
    }


}
