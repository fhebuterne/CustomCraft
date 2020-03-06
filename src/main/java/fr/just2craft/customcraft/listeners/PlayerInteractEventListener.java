package fr.just2craft.customcraft.listeners;

import fr.just2craft.customcraft.CustomCraft;
import fr.just2craft.customcraft.domain.Config;
import fr.just2craft.customcraft.domain.ShapedRecipeConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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

        Config config = this.customCraft.getConfig().getSerializable("customcraft", Config.class);

        if (config == null) {
            return;
        }

        // TODO : Add option in config if we want accept block place or not
        Optional<ShapedRecipeConfig> first = config.getShapedRecipes()
                .stream()
                .filter(shapedRecipeConfig -> shapedRecipeConfig.getItemToCraft() != e.getItem())
                .findFirst();

        if (first.isPresent()) {
            e.setCancelled(true);
        }
    }

}
