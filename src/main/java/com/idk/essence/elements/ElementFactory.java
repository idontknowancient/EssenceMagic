package com.idk.essence.elements;

import com.idk.essence.utils.CustomKey;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigFile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ElementFactory {

    private static final Map<String, ElementBuilder> elements = new HashMap<>();

    private static ConfigFile.ConfigName ce;

    private ElementFactory() {}

    public static void initialize() {
        elements.clear();
        ce = ConfigFile.ConfigName.ELEMENTS;
        ConfigFile.ConfigName cc = ConfigFile.ConfigName.CONFIG;
        Element.setCounterEffect(cc.getBoolean("element-counter-effect", true));
        for(String name : ce.getConfig().getKeys(false)) {
            register(name);
        }
        registerDefault();
    }

    /**
     * Get an element from a string.
     * @param internalName the internal name of the element
     * @return the corresponding element
     */
    @Nullable
    public static Element get(String internalName) {
        ElementBuilder builder = elements.get(internalName);
        return builder != null ? builder.build() : null;
    }

    /**
     * Get an element from an item stack.
     * @param item the item stack to be checked
     * @return the corresponding element
     */
    @Nullable
    public static Element get(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null || !meta.getPersistentDataContainer().has(CustomKey.getElementKey())) return null;
        String internalName = meta.getPersistentDataContainer().get(CustomKey.getElementKey(), PersistentDataType.STRING);
        ElementBuilder builder = elements.get(internalName);
        return builder != null ? builder.build() : null;
    }

    private static void register(String internalName) {
        if(!ce.has(internalName) || elements.containsKey(internalName)) return;
        ElementBuilder builder = new ElementBuilder(internalName)
                .displayName(ce.getString(internalName + ".display-name", ""))
                .symbolItem(ce.getConfigurationSection(internalName + ".symbol-item"))
                .slot(ce.getInteger(internalName + ".slot", -1))
                .multiplier(ce.getConfigurationSection(internalName + ".damage-multiplier"));
        elements.put(internalName, builder);
    }

    /**
     * Register a default element "none" if not registered.
     */
    private static void registerDefault() {
        if(elements.containsKey(Element.defaultInternalName)) return;
        ElementBuilder builder = new ElementBuilder(Element.defaultInternalName).displayName(Util.colorize("&7None"));
        elements.put(Element.defaultInternalName, builder);
    }
}
