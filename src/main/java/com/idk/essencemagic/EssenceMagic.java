package com.idk.essencemagic;

import com.idk.essencemagic.items.SystemItemHandler;
import com.idk.essencemagic.magics.MagicHandler;
import com.idk.essencemagic.mobs.MobHandler;
import com.idk.essencemagic.utils.particles.ParticleHandler;
import com.idk.essencemagic.players.PlayerDataHandler;
import com.idk.essencemagic.skills.SkillHandler;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.elements.ElementHandler;
import com.idk.essencemagic.items.ItemHandler;
import com.idk.essencemagic.utils.Register;
import com.idk.essencemagic.utils.placeholders.CustomPlaceholder;
import com.idk.essencemagic.wands.WandHandler;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class EssenceMagic extends JavaPlugin {

    @Getter private static EssenceMagic plugin;

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
