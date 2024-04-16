package com.idk.essencemagic.menus;

import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Menu {

    private static final Util cm = Util.getUtil("menus"); //config menus, no .yml needed

    public static Inventory getElementMenu() {

        Inventory elementMenu = Bukkit.createInventory(new CustomHolder(), cm.geti("element.size"), cm.outs("element.title"));

        Element.elements.forEach((s, e)->{
            //s is name of an Element and e is an Element
            elementMenu.setItem(e.getSlot(), e.getSymbolItem());
        });

        //occupation
        if(cm.gets("element.occupation") != null) {
            for(int i = 0; i < cm.geti("element.size"); i++) {
                if(elementMenu.getItem(i) == null) elementMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.gets("element.occupation").toUpperCase())));
            }
        }

        return elementMenu;
    }
}
