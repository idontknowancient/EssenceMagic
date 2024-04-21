package com.idk.essencemagic.items;

import com.idk.essencemagic.utils.configs.ConfigFile;

import java.util.Set;

public class ItemHandler {

    public static void initialize() {
        Item.items.clear();
        setItems();
    }

    public static void setItems() {
        Set<String> itemSet = ConfigFile.ConfigName.ITEMS.getConfig().getKeys(false); //directly use getKeys, not getDefaultSection
        for(String s : itemSet) { //register items
            Item.items.put(s, new Item(s));
        }
    }
}
