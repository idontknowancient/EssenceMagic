package com.idk.essencemagic;

import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.elements.ElementHandler;
import com.idk.essencemagic.items.ItemHandler;
import com.idk.essencemagic.utils.Register;
import com.idk.essencemagic.utils.placeholders.CustomPlaceholder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EssenceMagic extends JavaPlugin {

    @Getter private static EssenceMagic plugin;

    public static void initialize() {
        ConfigFile.initialize();
        Register.registerListeners();
        Register.registerCommands();
        ElementHandler.setElements();
        ItemHandler.setItems();
    }

    public static void registerDependencies() {
        for(String dependency : plugin.getDescription().getDepend()) {
            if(!Bukkit.getPluginManager().isPluginEnabled(dependency)) {
                plugin.getLogger().warning("Depend plugin not found: " + dependency);
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
        for(String softDependency : plugin.getDescription().getSoftDepend()) {
            if(!Bukkit.getPluginManager().isPluginEnabled(softDependency)) {
                plugin.getLogger().warning("Soft depend plugin not found: " + softDependency);
            }
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        registerDependencies();
        initialize();

        try {
            new CustomPlaceholder().register();
        } catch(NoClassDefFoundError e) {
            //
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
