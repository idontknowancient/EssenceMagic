package com.idk.essencemagic.menus;

import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.mobs.Mob;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
        Item.items.forEach((s, e)-> itemMenu.addItem(e.getItem()));

        if(cm.getString("item.occupation") != null) {
            for(int i = 0; i < cm.getInteger("item.size"); i++) {
                if(itemMenu.getItem(i) == null) itemMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("item.occupation").toUpperCase())));
            }
        }
        return itemMenu;
    }

    public static Inventory getMobMenu() {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        Inventory mobMenu = Bukkit.createInventory(new CustomHolder(), cm.getInteger("mob.size"), cm.outString("mob.title"));

        Mob.mobs.forEach((s, m)->{
            ItemStack item = new ItemStack(
                    Material.valueOf(m.getType().toString() + "_spawn_egg" .toUpperCase()));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(m.getDisplayName());

            List<String> lore = new ArrayList<>();
            lore.add(Util.colorize("&fInterior name: " + m.getName()));
            lore.add(Util.colorize("&fType: " + m.getType().toString()));
            if(m.getHealth() != -1)
                lore.add(Util.colorize("&fHealth: " + m.getHealth()));
            else
                lore.add(Util.colorize("&fHealth: default"));
            lore.add(Util.colorize("&fElement: " + m.getElement().getDisplayName()));
            lore.add("");
            lore.addAll(m.getDescription());
            meta.setLore(lore);

            item.setItemMeta(meta);
            mobMenu.addItem(item);
        });

        if(cm.getString("mob.occupation") != null) {
            for(int i = 0; i < cm.getInteger("mob.size"); i++) {
                if(mobMenu.getItem(i) == null) mobMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("mob.occupation").toUpperCase())));
            }
        }
        return mobMenu;
    }
}
