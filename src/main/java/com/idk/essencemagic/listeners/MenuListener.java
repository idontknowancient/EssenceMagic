package com.idk.essencemagic.listeners;

import com.idk.essencemagic.menus.holders.CancelHolder;
import com.idk.essencemagic.menus.holders.DetailInfoHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuListener implements Listener {

    @EventHandler
    public void onCustomMenuClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player player) || e.getClickedInventory() == null || e.getCurrentItem() == null)
            return;

        Inventory inventory = e.getInventory();
        ItemMeta itemMeta = e.getCurrentItem().getItemMeta();

        // cancel a click
        if(inventory.getHolder() instanceof CancelHolder)
            e.setCancelled(true);

        // provide name and lore by click
        if(inventory.getHolder() instanceof DetailInfoHolder) {
            if(itemMeta == null || itemMeta.getLore() == null) return;
            player.sendMessage(itemMeta.getDisplayName());
            for(String string : itemMeta.getLore()) {
                player.sendMessage(string);
            }
        }
    }
}
