package com.idk.essencemagic.menus;

import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Menu {

    public static Inventory getElementMenu() {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        Inventory elementMenu = Bukkit.createInventory(new CustomHolder(), cm.getInteger("element.size"), cm.outString("element.title"));
        Element.elements.forEach((s, e)->{
            //s is name of an Element and e is an Element
            elementMenu.setItem(e.getSlot(), e.getSymbolItem());
        });

        //occupation
        if(cm.getString("element.occupation") != null) {
            for(int i = 0; i < cm.getInteger("element.size"); i++) {
                if(elementMenu.getItem(i) == null) elementMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("element.occupation").toUpperCase())));
            }
        }
        return elementMenu;
    }

    public static Inventory getItemMenu() {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        Inventory itemMenu = Bukkit.createInventory(new CustomHolder(), cm.getInteger("item.size"), cm.outString("item.title"));
        Item.items.forEach((s, e)->{
            itemMenu.addItem(e.getItem());
        });

        if(cm.getString("item.occupation") != null) {
            for(int i = 0; i < cm.getInteger("element.size"); i++) {
                if(itemMenu.getItem(i) == null) itemMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("item.occupation").toUpperCase())));
            }
        }
        return itemMenu;
    }
}
