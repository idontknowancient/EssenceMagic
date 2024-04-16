package com.idk.essencemagic.listeners;

import com.idk.essencemagic.utils.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final Util c = Util.getUtil();

    private final Util cm = Util.getUtil("messages"); //config message

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!c.getb("send-message-when-join")) {
            e.setJoinMessage("");
        } else if(c.getb("send-custom-message-when-join")) {
            //with placeholders
            e.setJoinMessage(cm.outs("player-join-message", e.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(!c.getb("send-message-when-quit")) {
            e.setQuitMessage("");
        } else if(c.getb("send-custom-message-when-quit")) {
            //with placeholders
            e.setQuitMessage(cm.outs("player-quit-message", e.getPlayer()));
        }
    }
}
