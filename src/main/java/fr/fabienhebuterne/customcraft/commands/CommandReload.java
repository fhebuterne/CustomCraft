package fr.fabienhebuterne.customcraft.commands;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.commands.factory.CallCommand;
import fr.fabienhebuterne.customcraft.domain.RecipeService;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.io.IOException;
import java.util.Iterator;

public class CommandReload extends CallCommand<CustomCraft> {

    public CommandReload() {
        super("reload");
    }

    protected void runFromOther(Server server, CommandSender commandSender, String commandLabel, Command cmd, String[] args) throws Exception {
        this.reload(commandSender);
    }

    protected void runFromPlayer(Server server, Player player, String commandLabel, Command cmd, String[] args) throws IOException {
        this.reload(player);
    }

    private void reload(CommandSender commandSender) throws IOException {
        CustomCraft instance = this.getInstance();
        instance.loadAllConfig();
        Iterator<Recipe> recipeIterator = Bukkit.getServer().recipeIterator();
        Recipe recipe;
        while(recipeIterator.hasNext()) {
            recipe = recipeIterator.next();
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                if (shapedRecipe.getKey().getNamespace().contains("customcraft")) {
                    recipeIterator.remove();
                }
            }
            if (recipe instanceof ShapelessRecipe) {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
                if (shapelessRecipe.getKey().getNamespace().contains("customcraft")) {
                    recipeIterator.remove();
                }
            }
        }
        new RecipeService(instance).loadCustomRecipe();
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(o -> o.getOpenInventory().getTitle().contains("CustomCraft"))
                .forEach(HumanEntity::closeInventory);
        commandSender.sendMessage(CustomCraft.getTranslationConfig().getSerializable().getReload());
    }


}
