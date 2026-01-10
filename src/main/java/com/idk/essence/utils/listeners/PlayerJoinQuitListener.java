package com.idk.essence.utils.listeners;

import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.messages.SystemMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!ConfigManager.ConfigDefaultFile.CONFIG.getBoolean("send-message-when-join")) {
            e.joinMessage(Component.text(""));
        } else if(ConfigManager.ConfigDefaultFile.CONFIG.getBoolean("send-custom-message-when-join")) {
            //with placeholders
            e.joinMessage(SystemMessage.PLAYER_JOIN_MESSAGE.out(e.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(!ConfigManager.ConfigDefaultFile.CONFIG.getBoolean("send-message-when-quit")) {
            e.quitMessage(Component.text(""));
        } else if(ConfigManager.ConfigDefaultFile.CONFIG.getBoolean("send-custom-message-when-quit")) {
            //with placeholders
            e.quitMessage(SystemMessage.PLAYER_QUIT_MESSAGE.out(e.getPlayer()));
        }
    }
}
