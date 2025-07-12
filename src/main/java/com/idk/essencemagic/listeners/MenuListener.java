package com.idk.essencemagic.listeners;

import com.idk.essencemagic.items.ItemHandler;
import com.idk.essencemagic.menus.holders.CancelHolder;
import com.idk.essencemagic.menus.holders.DetailInfoHolder;
import com.idk.essencemagic.menus.holders.GetItemHolder;
import com.idk.essencemagic.menus.holders.ShiftSpawnHolder;
import com.idk.essencemagic.mobs.Mob;
import com.idk.essencemagic.mobs.MobHandler;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import com.idk.essencemagic.wands.Wand;
import com.idk.essencemagic.wands.WandHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuListener implements Listener {

    @EventHandler
    public void onCustomMenuClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player player) || e.getClickedInventory() == null || e.getCurrentItem() == null)
            return;

        Inventory inventory = e.getInventory();
        ItemStack item = e.getCurrentItem();
        ItemMeta itemMeta = item.getItemMeta();

        // cancel a click
        if(inventory.getHolder() instanceof CancelHolder)
            e.setCancelled(true);

        // provide name and lore by click (skills & magic)
        if(inventory.getHolder() instanceof DetailInfoHolder) {
            if(itemMeta == null || itemMeta.getLore() == null) return;
            player.sendMessage(itemMeta.getDisplayName());
            for(String string : itemMeta.getLore()) {
                player.sendMessage(string);
            }
        }

        // get custom items by click (items & wands)
        if(inventory.getHolder() instanceof GetItemHolder) {
            // wands are also custom items
            if(SystemPermission.checkPerm(player, Permission.COMMAND_WAND_MENU_GET.name)) {
                if(WandHandler.isWand(item)) {

                    Wand specificWand = WandHandler.getWand(item);
                    if(specificWand == null) return;
                    player.getInventory().addItem(specificWand.getItemStack());
                }
            } else if(SystemPermission.checkPerm(player, Permission.COMMAND_ITEM_MENU_GET.name)) {
                if(ItemHandler.isCustomItem(item))
                    player.getInventory().addItem(item);
            }
        }

        // spawn custom mobs by shift-click
        if(inventory.getHolder() instanceof ShiftSpawnHolder) {
            if(!SystemPermission.checkPerm(player, Permission.COMMAND_MOB_MENU_SPAWN.name) ||
                    !(e.getClick().equals(ClickType.SHIFT_LEFT) ||
                            e.getClick().equals(ClickType.SHIFT_RIGHT))) return;
            if(itemMeta == null || itemMeta.getLore() == null) return;

            // e.g. Interior name: test_zombie
            String mobName = itemMeta.getLore().get(0).split(" ")[2];
            if(Mob.mobs.containsKey(mobName))
                MobHandler.spawnMob(player.getLocation(), Mob.mobs.get(mobName));
        }
    }
}
