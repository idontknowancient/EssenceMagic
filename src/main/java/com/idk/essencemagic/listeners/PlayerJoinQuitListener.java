package com.idk.essencemagic.listeners;

import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!ConfigFile.ConfigName.CONFIG.getBoolean("send-message-when-join")) {
            e.setJoinMessage("");
        } else if(ConfigFile.ConfigName.CONFIG.getBoolean("send-custom-message-when-join")) {
            //with placeholders
            e.setJoinMessage(ConfigFile.ConfigName.MESSAGES.outString("player-join-message", e.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(!ConfigFile.ConfigName.CONFIG.getBoolean("send-message-when-quit")) {
            e.setQuitMessage("");
        } else if(ConfigFile.ConfigName.CONFIG.getBoolean("send-custom-message-when-quit")) {
            //with placeholders
            e.setQuitMessage(ConfigFile.ConfigName.MESSAGES.outString("player-quit-message", e.getPlayer()));
        }
    }
}
