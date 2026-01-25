package com.idk.essence.utils.messages;

import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitMessage implements Listener {

    @Getter private static final PlayerJoinQuitMessage instance =  new PlayerJoinQuitMessage();

    private PlayerJoinQuitMessage() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!ConfigManager.DefaultFile.CONFIG.getBoolean("send-message-when-join")) {
            e.joinMessage(Component.text(""));
        } else if(ConfigManager.DefaultFile.CONFIG.getBoolean("send-custom-message-when-join")) {
            // With placeholders
            e.joinMessage(SystemMessage.PLAYER_JOIN_MESSAGE.out(e.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(!ConfigManager.DefaultFile.CONFIG.getBoolean("send-message-when-quit")) {
            e.quitMessage(Component.text(""));
        } else if(ConfigManager.DefaultFile.CONFIG.getBoolean("send-custom-message-when-quit")) {
            // With placeholders
            e.quitMessage(SystemMessage.PLAYER_QUIT_MESSAGE.out(e.getPlayer()));
        }
    }
}
