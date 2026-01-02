package com.idk.essence.utils.listeners;

import com.idk.essence.utils.configs.ConfigFile;
import com.idk.essence.utils.messages.SystemMessage;
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
            e.setJoinMessage(SystemMessage.PLAYER_JOIN_MESSAGE.out(e.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(!ConfigFile.ConfigName.CONFIG.getBoolean("send-message-when-quit")) {
            e.setQuitMessage("");
        } else if(ConfigFile.ConfigName.CONFIG.getBoolean("send-custom-message-when-quit")) {
            //with placeholders
            e.setQuitMessage(SystemMessage.PLAYER_QUIT_MESSAGE.out(e.getPlayer()));
        }
    }
}
