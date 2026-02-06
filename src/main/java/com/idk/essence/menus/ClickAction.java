package com.idk.essence.menus;

import com.idk.essence.items.items.ItemFactory;
import com.idk.essence.mobs.MobManager;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import dev.triumphteam.gui.components.GuiAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ClickAction {

    /**
     * Send the item lore to the clicker.
     */
    public static GuiAction<@NotNull InventoryClickEvent> detailInfoAction() {
        return e ->
            Optional.ofNullable(e.getCurrentItem()).map(ItemStack::getItemMeta).map(ItemMeta::lore)
                    .ifPresent(lore -> lore.forEach(l -> e.getWhoClicked().sendMessage(l)));
    }

    /**
     * Add the item to the clicker's inventory.
     */
    public static GuiAction<@NotNull InventoryClickEvent> getItemAction() {
        return e ->
            Optional.ofNullable(e.getCurrentItem())
                    .filter(item -> SystemPermission.checkPerm(e.getWhoClicked(), Permission.COMMAND_ITEM_MENU_GET))
                    .filter(ItemFactory::isCustom)
                    .map(ItemStack::getItemMeta)
                    .map(meta -> meta.getPersistentDataContainer().get(Key.Type.ITEM.getKey(), PersistentDataType.STRING))
                    .map(ItemFactory::get)
                    .ifPresent(item -> e.getWhoClicked().getInventory().addItem(item));
    }

    /**
     * Add the item to the clicker's inventory.
     */
    public static GuiAction<@NotNull InventoryClickEvent> shiftSpawnAction() {
        return e ->
            Optional.ofNullable(e.getCurrentItem())
                    .filter(item -> SystemPermission.checkPerm(e.getWhoClicked(), Permission.COMMAND_MOB_MENU_SPAWN))
                    .filter(item -> e.getClick().isShiftClick())
                    .map(ItemStack::getItemMeta)
                    .map(meta -> meta.getPersistentDataContainer().get(Key.Type.ITEM.getKey(), PersistentDataType.STRING))
                    .ifPresent(internalName -> MobManager.spawn(internalName, e.getWhoClicked().getLocation()));
    }
}
