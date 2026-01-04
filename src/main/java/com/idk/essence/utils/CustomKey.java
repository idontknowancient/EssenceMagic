package com.idk.essence.utils;

import com.idk.essence.Essence;
import lombok.Getter;
import org.bukkit.NamespacedKey;

public class CustomKey {

    @Getter private static final NamespacedKey itemKey = new NamespacedKey(Essence.getPlugin(), "item-key");

    @Getter private static final NamespacedKey elementKey = new NamespacedKey(Essence.getPlugin(), "element-key");
}
