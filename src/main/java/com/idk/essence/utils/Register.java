package com.idk.essence.utils;

import com.idk.essence.Essence;
import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.items.ItemFactory;
import com.idk.essence.items.SystemItemHandler;
import com.idk.essence.magics.MagicHandler;
import com.idk.essence.mobs.MobFactory;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.damage.DamageManager;
import com.idk.essence.utils.interactiveSlots.InteractiveSlot;
import com.idk.essence.utils.interactiveSlots.InteractiveSlotHandler;
import com.idk.essence.utils.listeners.MenuListener;
import com.idk.essence.utils.listeners.PlayerJoinQuitListener;
import com.idk.essence.utils.particles.ParticleHandler;
import com.idk.essence.players.PlayerDataHandler;
import com.idk.essence.skills.SkillHandler;
import com.idk.essence.items.wands.WandHandler;
import com.idk.essence.utils.placeholders.PlaceholderManager;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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
        PlaceholderManager.initialize();
        ConfigManager.initialize();
        ElementFactory.initialize();
        SkillHandler.initialize();
        MagicHandler.initialize();
        ItemFactory.initialize();
        SystemItemHandler.initialize();
        WandHandler.initialize();
        MobFactory.initialize();
        PlayerDataHandler.initialize();
        ParticleHandler.initialize();
        InteractiveSlotHandler.initialize();
    }

    private static void register(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public static void registerListeners() {
        register(new PlayerJoinQuitListener());
        register(new MenuListener());
        register(new DamageManager());
        register(new PlayerDataHandler());
        register(new SkillHandler());
        register(new WandHandler());
        register(new SystemItemHandler());
        register(new ParticleHandler());
        register(new InteractiveSlotHandler());

        // Needed when using ConfigurationSerializable
        ConfigurationSerialization.registerClass(InteractiveSlot.class);

        // CustomBlockData will automatically handle moving/removing block data for changed blocks
        CustomBlockData.registerListener(plugin);
    }

    private static void register(String string, CommandExecutor executor) {
        Optional.ofNullable(plugin.getCommand(string)).ifPresent(command -> command.setExecutor(executor));
    }

    public static void registerCommands() {
        register("essence", new EssenceCommand());
    }
}
