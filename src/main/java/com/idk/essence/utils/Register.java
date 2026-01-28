package com.idk.essence.utils;

import com.idk.essence.Essence;
import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.items.items.ItemFactory;
import com.idk.essence.magics.MagicHandler;
import com.idk.essence.mobs.MobManager;
import com.idk.essence.players.PlayerDataManager;
import com.idk.essence.skills.SkillManager;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.damage.DamageManager;
import com.idk.essence.menus.MenuListener;
import com.idk.essence.utils.messages.Message;
import com.idk.essence.utils.messages.PlayerJoinQuitMessage;
import com.idk.essence.items.arcana.WandHandler;
import com.idk.essence.utils.nodes.NodeManager;
import com.idk.essence.utils.particles.ParticleManager;
import com.idk.essence.utils.placeholders.PlaceholderManager;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Optional;

public class Register {

    private static final Essence plugin = Essence.getPlugin();

    public static void registerDependencies() {
        for(String dependency : plugin.getPluginMeta().getPluginDependencies()) {
            if(!Bukkit.getPluginManager().isPluginEnabled(dependency)) {
                plugin.getLogger().warning("Depend plugin not found: " + dependency);
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
        for(String softDependency : plugin.getPluginMeta().getPluginSoftDependencies()) {
            if(!Bukkit.getPluginManager().isPluginEnabled(softDependency)) {
                plugin.getLogger().warning("Soft depend plugin not found: " + softDependency);
            }
        }
    }

    public static void initialize() {
        Message.initialize();
        PlaceholderManager.initialize();
        ConfigManager.initialize();
        ElementFactory.initialize();
        SkillManager.initialize();
        MagicHandler.initialize();
        ArtifactFactory.initialize();
        ItemFactory.initialize();
        WandHandler.initialize();
        MobManager.initialize();
        PlayerDataManager.initialize();
        ParticleManager.initialize();
        NodeManager.initialize();
    }

    private static void register(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public static void registerListeners() {
        register(PlayerJoinQuitMessage.getInstance());
        register(MenuListener.getInstance());
        register(DamageManager.getInstance());
        register(PlayerDataManager.getInstance());
        register(SkillManager.getInstance());
        register(ArtifactFactory.getInstance());
        register(new WandHandler());
        register(ParticleManager.getInstance());
        register(NodeManager.getInstance());

        // CustomBlockData will automatically handle moving/removing block data for changed blocks
        CustomBlockData.registerListener(plugin);
    }

    public static void registerCommands() {
        Optional.ofNullable(plugin.getCommand("essence"))
                .ifPresent(command -> command.setExecutor(EssenceCommand.getInstance()));
    }
}
