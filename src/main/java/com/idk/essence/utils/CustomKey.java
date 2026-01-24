package com.idk.essence.utils;

import com.idk.essence.Essence;
import lombok.Getter;
import org.bukkit.NamespacedKey;

public class CustomKey {

    private static final Essence plugin = Essence.getPlugin();

    // Class key
    @Getter private static final NamespacedKey itemKey = new NamespacedKey(plugin, "item-key");
    @Getter private static final NamespacedKey artifactKey = new NamespacedKey(plugin, "artifact-key");
    @Getter private static final NamespacedKey elementKey = new NamespacedKey(plugin, "element-key");
    @Getter private static final NamespacedKey mobKey = new NamespacedKey(plugin, "mob-key");
    @Getter private static final NamespacedKey skillKey = new NamespacedKey(plugin, "skill-key");

    // Feature key
    @Getter private static final NamespacedKey placeableKey = new NamespacedKey(plugin, "placeable-key");
    @Getter private static final NamespacedKey usableKey = new NamespacedKey(plugin, "usable-key");
}
