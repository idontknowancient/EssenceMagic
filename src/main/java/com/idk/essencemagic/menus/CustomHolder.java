package com.idk.essencemagic.menus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class CustomHolder implements InventoryHolder {
    //used for inventory click detect
    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
