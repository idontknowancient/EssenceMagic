package com.idk.essence.menus;

import com.idk.essence.items.items.ItemFactory;
import com.idk.essence.menus.holders.CancelHolder;
import com.idk.essence.menus.holders.DetailInfoHolder;
import com.idk.essence.menus.holders.GetItemHolder;
import com.idk.essence.menus.holders.ShiftSpawnHolder;
import com.idk.essence.mobs.MobManager;
import com.idk.essence.utils.CustomKey;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import com.idk.essence.items.arcana.Wand;
import com.idk.essence.items.arcana.WandHandler;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Optional;

public class MenuListener implements Listener {

    @Getter private static final MenuListener instance = new MenuListener();

    private MenuListener() {}

    @EventHandler
    public void onCustomMenuClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player player) || e.getClickedInventory() == null || e.getCurrentItem() == null)
            return;

        Inventory inventory = e.getInventory();
        ItemStack item = e.getCurrentItem();
        ItemMeta itemMeta = item.getItemMeta();
        Component displayName = itemMeta.displayName();
        List<Component> lore = itemMeta.lore();

        // Cancel a click
        if(inventory.getHolder() instanceof CancelHolder)
            e.setCancelled(true);

        // Provide info by click (elements & skills & magics)
        if(inventory.getHolder() instanceof DetailInfoHolder) {
            if(displayName == null || lore == null) return;
            player.sendMessage(displayName);
            for(Component component : lore) {
                player.sendMessage(component);
            }
        }

        // Get items by click (artifacts & items & arcana)
         if(inventory.getHolder() instanceof GetItemHolder) {
            if(ItemFactory.isCustom(item) && SystemPermission.checkPerm(player, Permission.COMMAND_ITEM_MENU_GET)) {
                player.getInventory().addItem(item);
            }
            if(WandHandler.isWand(item) &&  SystemPermission.checkPerm(player, Permission.COMMAND_WAND_MENU_GET)) {
                Wand specificWand = WandHandler.getWand(item);
                if(specificWand == null) return;
                player.getInventory().addItem(specificWand.getItemStack());
            }
        }

        // Spawn custom mobs by shift-click
        if(inventory.getHolder() instanceof ShiftSpawnHolder) {
            if(!SystemPermission.checkPerm(player, Permission.COMMAND_MOB_MENU_SPAWN)) return;
            if(!(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT))) return;
            // e.g. internalName: test_zombie
            String mobName = itemMeta.getPersistentDataContainer().get(CustomKey.getItemKey(), PersistentDataType.STRING);
            Optional.ofNullable(mobName).ifPresent(internalName -> MobManager.spawn(internalName, player.getLocation()));
        }
    }
}
