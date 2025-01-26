package com.idk.essencemagic.menus.holders;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class CancelHolder implements InventoryHolder {

    // used to cancel a click
    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
