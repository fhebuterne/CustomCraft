package fr.fabienhebuterne.customcraft.commands;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.commands.factory.CallCommand;
import fr.fabienhebuterne.customcraft.domain.PrepareCustomCraft;
import fr.fabienhebuterne.customcraft.domain.config.CustomCraftConfig;
import fr.fabienhebuterne.customcraft.domain.config.RecipeConfig;
import fr.fabienhebuterne.customcraft.domain.inventory.InventoryInitService;
import fr.fabienhebuterne.customcraft.exceptions.BadArgumentsException;
import fr.fabienhebuterne.customcraft.exceptions.CustomCraftAlreadyExistException;
import fr.fabienhebuterne.customcraft.exceptions.OnlyPlayerCommandException;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public class CommandCreate extends CallCommand<CustomCraft> {

    public CommandCreate() {
        super("create");
    }

    protected void runFromOther(Server server, CommandSender commandSender, String commandLabel, Command cmd, String[] args) throws Exception {
        throw new OnlyPlayerCommandException(server.getConsoleSender());
    }

    protected void runFromPlayer(Server server, Player player, String commandLabel, Command cmd, String[] args) throws BadArgumentsException, CustomCraftAlreadyExistException {
        if (args.length == 1) {
            throw new BadArgumentsException(player, "create <craftName>");
        }

        CustomCraftConfig customCraftConfig = instance.getCustomCraftConfig().getSerializable();

        Optional<RecipeConfig> craftWithSameName = customCraftConfig.getRecipes()
                .stream()
                .filter(recipeConfig -> recipeConfig.getCraftName().equals(args[1]))
                .findFirst();

        if (craftWithSameName.isPresent()) {
            throw new CustomCraftAlreadyExistException(player);
        }

        // Remove previous tmp data if exist for same player
        instance.getTmpData().remove(player.getUniqueId());

        PrepareCustomCraft prepareCustomCraft = new PrepareCustomCraft();
        prepareCustomCraft.setCraftName(args[1]);
        instance.addTmpData(player.getUniqueId(), prepareCustomCraft);

        Inventory inventory = new InventoryInitService(instance).chooseCraftTypeInventory(player);
        player.openInventory(inventory);
    }


}
