package com.idk.essencemagic.utils;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.commands.EssenceCommand;
import com.idk.essencemagic.listeners.MenuListener;
import com.idk.essencemagic.listeners.PlayerJoinQuitListener;
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
    }

    public static void registerCommands() {
        register("essence", new EssenceCommand());
    }
}
