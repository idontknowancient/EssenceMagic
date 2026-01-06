package com.idk.essence.utils;

import com.idk.essence.Essence;
import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.items.SystemItemHandler;
import com.idk.essence.utils.damage.DamageManager;
import com.idk.essence.utils.interactiveSlots.InteractiveSlot;
import com.idk.essence.utils.interactiveSlots.InteractiveSlotHandler;
import com.idk.essence.utils.listeners.MenuListener;
import com.idk.essence.utils.listeners.PlayerJoinQuitListener;
import com.idk.essence.utils.particles.ParticleHandler;
import com.idk.essence.players.PlayerDataHandler;
import com.idk.essence.skills.SkillHandler;
import com.idk.essence.items.wands.WandHandler;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;

public class Register {

    private static final Essence plugin = Essence.getPlugin();

    private static void register(Listener l) {
        plugin.getServer().getPluginManager().registerEvents(l, plugin);
    }

    private static void register(String s, CommandExecutor c) {
        plugin.getCommand(s).setExecutor(c);
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

        // needed when using ConfigurationSerializable
        ConfigurationSerialization.registerClass(InteractiveSlot.class);

        // CustomBlockData will automatically handle moving/removing block data for changed blocks
        CustomBlockData.registerListener(plugin);
    }

    public static void registerCommands() {
        register("essence", new EssenceCommand());
    }
}
