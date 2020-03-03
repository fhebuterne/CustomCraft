package fr.just2craft.customcraft.commands;

import fr.just2craft.customcraft.commands.factory.CallCommand;
import fr.just2craft.customcraft.exceptions.OnlyPlayerCommandException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class CommandCreate extends CallCommand {
    public final static ArrayList<Integer> CRAFT_CASES = new ArrayList<>(Arrays.asList(1, 2, 3, 10, 11, 12, 19, 20, 21));
    public final static Integer RESULT_CRAFT_CASE = 15;
    public final static Integer QUIT_INVENTORY_CASE = 8;
    public final static Integer VALID_INVENTORY_CASE = 26;

    public CommandCreate() {
        super("create");
    }

    protected void runFromOther(Server server, String commandLabel, Command cmd, String[] args) throws Exception {
        throw new OnlyPlayerCommandException(server.getConsoleSender());
    }

    protected void runFromPlayer(Server server, Player player, String commandLabel, Command cmd, String[] args) {
        // TODO : Add an other line to edit some options like block place etc...
        // TODO : Check Craft name args 1 ... Required and control lenght

        Inventory inventory = Bukkit.createInventory(
                player,
                InventoryType.CHEST,
                this.getInstance().getName()
        );

        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(args[1]);
        }
        itemStack.setItemMeta(itemMeta);

        IntStream.range(0, InventoryType.CHEST.getDefaultSize())
                .filter(value -> !CRAFT_CASES.contains(value) && RESULT_CRAFT_CASE != value)
                .forEach(value -> inventory.setItem(value, itemStack));

        inventory.setItem(QUIT_INVENTORY_CASE, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        inventory.setItem(VALID_INVENTORY_CASE, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));

        player.openInventory(inventory);
    }
}
