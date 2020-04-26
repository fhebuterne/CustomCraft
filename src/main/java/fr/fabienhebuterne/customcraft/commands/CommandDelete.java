package fr.fabienhebuterne.customcraft.commands;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.commands.factory.CallCommand;
import fr.fabienhebuterne.customcraft.domain.config.CustomCraftConfig;
import fr.fabienhebuterne.customcraft.domain.inventory.InventoryInitService;
import fr.fabienhebuterne.customcraft.exceptions.OnlyPlayerCommandException;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommandDelete extends CallCommand<CustomCraft> {

    public CommandDelete() {
        super("delete");
    }

    protected void runFromOther(Server server, CommandSender commandSender, String commandLabel, Command cmd, String[] args) throws Exception {
        throw new OnlyPlayerCommandException(server.getConsoleSender());
    }

    protected void runFromPlayer(Server server, Player player, String commandLabel, Command cmd, String[] args) {
        CustomCraftConfig customCraftConfig = instance.getCustomCraftConfig().getSerializable();
        
        Inventory inventory = new InventoryInitService(instance).deleteInventory(player, customCraftConfig.getRecipes());
        player.openInventory(inventory);
    }


}
