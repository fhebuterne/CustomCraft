package fr.fabienhebuterne.customcraft.listeners;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.domain.config.Config;
import fr.fabienhebuterne.customcraft.domain.config.OptionItemStackConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

public class PlayerInteractEventListener implements Listener {

    private CustomCraft customCraft;

    public PlayerInteractEventListener(CustomCraft customCraft) {
        this.customCraft = customCraft;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (e.getItem() == null) {
            return;
        }

        Config config = this.customCraft.getConfig().getSerializable("customcraft", Config.class);

        if (config == null) {
            return;
        }

        ItemStack itemInv = e.getItem().clone();
        itemInv.setAmount(1);

        Optional<Map.Entry<ItemStack, OptionItemStackConfig>> first = config.getOptionItemStack()
                .entrySet()
                .stream()
                .filter(itemStackOptionConfig -> {
                    ItemStack itemOption = itemStackOptionConfig.getKey();
                    itemOption.setAmount(1);
                    return itemInv.equals(itemOption);
                })
                .filter(itemStackOptionConfig -> !itemStackOptionConfig.getValue().isBlockCanBePlaced())
                .findFirst();

        if (first.isPresent()) {
            e.setCancelled(true);
        }
    }

}
