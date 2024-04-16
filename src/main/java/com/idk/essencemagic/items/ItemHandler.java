package com.idk.essencemagic.items;

import com.idk.essencemagic.utils.Util;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class ItemHandler {

    private static final Util ci = Util.getUtil("items");

    private static final FileConfiguration config = ci.getConfig();

    public static void setItems() {
        Set<String> itemSet = config.getKeys(false); //directly use getKeys, not getDefaultSection
        for(String s : itemSet) { //register items
            Item.items.put(s, new Item(s));
        }
    }
}
