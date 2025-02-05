package com.idk.essencemagic.wands;

import com.idk.essencemagic.EssenceMagic;
import lombok.Getter;
import org.bukkit.NamespacedKey;

import java.util.LinkedHashMap;
import java.util.Map;

public class Wand {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    public static final Map<String, Wand> wands = new LinkedHashMap<>();

    // signify custom items
    @Getter private static final NamespacedKey itemKey = new NamespacedKey(plugin, "item-key");

    // signify wands
    @Getter private static final NamespacedKey wandKey = new NamespacedKey(plugin, "wand-key");
}
