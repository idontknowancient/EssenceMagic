package com.idk.essence;

import com.idk.essence.items.SystemItemHandler;
import com.idk.essence.magics.MagicHandler;
import com.idk.essence.mobs.MobHandler;
import com.idk.essence.utils.interactiveSlots.InteractiveSlotHandler;
import com.idk.essence.utils.particles.ParticleHandler;
import com.idk.essence.players.PlayerDataHandler;
import com.idk.essence.skills.SkillHandler;
import com.idk.essence.utils.configs.ConfigFile;
import com.idk.essence.elements.ElementHandler;
import com.idk.essence.items.ItemHandler;
import com.idk.essence.utils.Register;
import com.idk.essence.utils.placeholders.CustomPlaceholder;
import com.idk.essence.items.wands.WandHandler;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Essence extends JavaPlugin {

    @Getter private static Essence plugin;

    public static void initialize() {
        ConfigFile.initialize();
        ElementHandler.initialize();
        SkillHandler.initialize();
        MagicHandler.initialize();
        ItemHandler.initialize();
        SystemItemHandler.initialize();
        WandHandler.initialize();
        MobHandler.initialize();
        PlayerDataHandler.initialize();
        ParticleHandler.initialize();
        InteractiveSlotHandler.initialize();
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

        Register.registerListeners();
        Register.registerCommands();

        if(!PlaceholderAPI.isRegistered(plugin.getDescription().getName().toLowerCase())) {
            try {
                new CustomPlaceholder().register();
            } catch(NoClassDefFoundError e) {
               getLogger().log(Level.SEVERE, "", e);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
