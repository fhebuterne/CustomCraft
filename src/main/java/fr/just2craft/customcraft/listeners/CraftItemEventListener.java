package fr.just2craft.customcraft.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Arrays;

public class CraftItemEventListener implements Listener {

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent e) {
        System.out.println(Arrays.toString(e.getInventory().getMatrix()));
        System.out.println(e.getInventory().getMatrix().length);
    }

}
