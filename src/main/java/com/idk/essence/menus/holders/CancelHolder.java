package com.idk.essence.menus.holders;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Cancel a click.
 */
public class CancelHolder implements InventoryHolder {

    @Getter private static final CancelHolder instance = new CancelHolder();

    protected CancelHolder() {}

    @NotNull
    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(this, 9);
    }
}
