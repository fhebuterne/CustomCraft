package fr.just2craft.customcraft.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static fr.just2craft.customcraft.commands.CommandCreate.CRAFT_CASES;
import static fr.just2craft.customcraft.commands.CommandCreate.QUIT_INVENTORY_CASE;

public class InventoryClickEventListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("CustomCraft")) {
            return;
        }

        this.createCommand(e);
    }

    private void createCommand(InventoryClickEvent e) {
        if (!CRAFT_CASES.contains(e.getSlot())) {
            e.setCancelled(true);
        }

        if (e.getSlot() == QUIT_INVENTORY_CASE) {
            e.getView().close();
        }
    }

}
