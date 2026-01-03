package com.idk.essence.items;

import com.idk.essence.utils.configs.ConfigFile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ItemFactory {

    private static final Map<String, ItemBuilder> items = new HashMap<>();

    private static ConfigFile.ConfigName ci;

    private ItemFactory() {}

    @Nullable
    public static ItemStack get(String internalName) {
        ItemBuilder builder = items.get(internalName);
        return builder != null ? builder.build() : null;
    }

    public static void initialize() {
        items.clear();
        ci = ConfigFile.ConfigName.ITEMS;
        for(String name : ci.getConfig().getKeys(false)) {
            register(name);
        }
    }

    private static void register(String internalName) {
        if(!ci.has(internalName) || items.containsKey(internalName)) return;

        ItemBuilder builder = new ItemBuilder(ci.getString(internalName + ".type", "stone"))
                .displayName(ci.outString(internalName + ".display-name", ""))
                .lore(ci.outStringList(internalName + ".lore"))
                .enchant(ci.getConfigurationSection(internalName + ".enchantments"))
                .flag(ci.getConfigurationSection(internalName + ".options"))
                .persistentDataContainer(PersistentDataType.STRING, internalName);
        items.put(internalName, builder);
    }
}
