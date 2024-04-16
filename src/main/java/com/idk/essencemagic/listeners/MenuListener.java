package com.idk.essencemagic.listeners;

import com.idk.essencemagic.menus.CustomHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onCustomMenuClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player) || e.getClickedInventory() == null || e.getCurrentItem() == null) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if(e.getInventory().getHolder() instanceof CustomHolder) {
            e.setCancelled(true);
        }
    }
}
