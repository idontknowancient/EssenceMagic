package com.idk.essencemagic.menus.holders;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class DetailInfoHolder extends CancelHolder implements InventoryHolder {

    // used to get detail info by click
    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
