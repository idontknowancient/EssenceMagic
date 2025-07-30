package com.idk.essencemagic.utils;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.commands.EssenceCommand;
import com.idk.essencemagic.utils.damage.DamageCalculator;
import com.idk.essencemagic.items.SystemItemHandler;
import com.idk.essencemagic.utils.listeners.MenuListener;
import com.idk.essencemagic.utils.listeners.PlayerJoinQuitListener;
import com.idk.essencemagic.utils.particles.ParticleHandler;
import com.idk.essencemagic.players.PlayerDataHandler;
import com.idk.essencemagic.skills.SkillHandler;
import com.idk.essencemagic.wands.WandHandler;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

public class Register {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    private static void register(Listener l) {
        plugin.getServer().getPluginManager().registerEvents(l, plugin);
    }

    private static void register(String s, CommandExecutor c) {
        plugin.getCommand(s).setExecutor(c);
    }

    public static void registerListeners() {
        register(new PlayerJoinQuitListener());
        register(new MenuListener());
        register(new DamageCalculator());
        register(new PlayerDataHandler());
        register(new SkillHandler());
        register(new WandHandler());
        register(new SystemItemHandler());
        register(new ParticleHandler());

        // CustomBlockData will automatically handle moving/removing block data for changed blocks
        CustomBlockData.registerListener(plugin);
    }

    public static void registerCommands() {
        register("essence", new EssenceCommand());
    }
}
