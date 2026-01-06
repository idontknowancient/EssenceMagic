package com.idk.essence.utils;

import com.idk.essence.Essence;
import lombok.Getter;
import org.bukkit.NamespacedKey;

public class CustomKey {

    private static final Essence plugin = Essence.getPlugin();

    @Getter private static final NamespacedKey itemKey = new NamespacedKey(plugin, "item-key");

    @Getter private static final NamespacedKey elementKey = new NamespacedKey(plugin, "element-key");

    @Getter private static final NamespacedKey mobKey = new NamespacedKey(plugin, "mob-key");

}
