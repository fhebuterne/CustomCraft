package fr.just2craft.customcraft;

import fr.just2craft.customcraft.commands.factory.CallCommandFactoryInit;
import fr.just2craft.customcraft.domain.Config;
import fr.just2craft.customcraft.listeners.CraftItemEventListener;
import fr.just2craft.customcraft.listeners.InventoryClickEventListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomCraft extends JavaPlugin {
    private CallCommandFactoryInit callCommandFactoryInit;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        this.callCommandFactoryInit = new CallCommandFactoryInit(this, "customcraft");
        ConfigurationSerialization.registerClass(Config.class, "Config");
        saveDefaultConfig();
        this.loadCustomRecipe();
        Bukkit.getServer().getPluginManager().registerEvents(new CraftItemEventListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickEventListener(this), this);
    }

    @Override
    public boolean onCommand(final CommandSender sender,
                             final Command command,
                             final String commandLabel,
                             final String[] args) {
        return this.callCommandFactoryInit.onCommandCustomCraft(
                sender,
                command,
                commandLabel,
                args,
                CustomCraft.class.getClassLoader(),
                "fr.just2craft.customcraft.commands",
                "customcraft.",
                true
        );
    }

    private void loadCustomRecipe() {
        /*ItemStack itemStack = new ItemStack(Material.APPLE);
        itemStack.setAmount(2);
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(this, "customcraft"), itemStack);

        shapedRecipe.shape(" 0 ", " 0 ", " 1 ");
        shapedRecipe.setIngredient('0', Material.EMERALD);
        shapedRecipe.setIngredient('1', Material.STICK);

        ShapedRecipeConfig shapedRecipeConfig = new ShapedRecipeConfig();
        shapedRecipeConfig.setItemToCraft(itemStack);
        shapedRecipeConfig.setGrid(Arrays.asList(" 0 ", " 0 ", " 1 "));
        HashMap<Integer, ItemStack> gridHashMap = new HashMap<>();
        gridHashMap.put(0, new ItemStack(Material.EMERALD));
        gridHashMap.put(1, new ItemStack(Material.STICK));
        shapedRecipeConfig.setGridSequence(gridHashMap);

        Config customcraft = getConfig().getSerializable("customcraft", Config.class);

        if (customcraft == null) {
            customcraft = new Config();
        }

        customcraft.addShapedRecipe(shapedRecipeConfig);
        getConfig().set("customcraft", customcraft);
        saveConfig();

        this.getServer().addRecipe(shapedRecipe);*/
    }

}
