package com.idk.essence.elements;

import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ElementFactory {

    private static final Map<String, ElementBuilder> elements = new HashMap<>();

    private ElementFactory() {}

    public static void initialize() {
        elements.clear();
        ConfigManager.DefaultFile cc = ConfigManager.DefaultFile.CONFIG;
        ConfigManager.DefaultFile cm = ConfigManager.DefaultFile.MENUS;
        Element.setCounterEffect(cc.getBoolean("element-counter-effect", true));
        Element.setShowDamageMultiplier(cm.getBoolean("element.show-damage-multiplier", true));
        ConfigManager.Folder.ELEMENTS.load(ElementFactory::register);
        registerDefault();
        for(ElementBuilder builder : elements.values())
            builder.setCounter();
        for(ElementBuilder builder : elements.values())
            builder.convert();
        Util.System.info("Registered Elements", elements.size());
    }

    /**
     * Get an element from a string.
     * @param internalName the internal name of the element
     * @return the corresponding element
     */
    @Nullable
    public static Element get(@Nullable String internalName) {
        if(internalName == null) return null;
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
        if(item == null) return null;
        String internalName = Key.Type.ELEMENT.getContent(item);
        return get(internalName);
    }

    /**
     * Get an element from a string.
     * @param internalName the internal name of the element
     * @return the corresponding element or the default element "none" if not found
     */
    public static Element getOrDefault(String internalName) {
        Element element = get(internalName);
        return element != null ? element : getDefault();
    }

    /**
     * Get an element from an item stack.
     * @param item the item stack to be checked
     * @return the corresponding element or the default element "none" if not found
     */
    public static Element getOrDefault(ItemStack item) {
        Element element = get(item);
        return element != null ? element : getDefault();
    }

    /**
     * Get default element.
     * @return element "none"
     */
    public static Element getDefault() {
        return elements.get(Element.defaultInternalName).build();
    }

    public static Collection<Element> getAll() {
        return elements.values().stream().map(ElementBuilder::build).toList();
    }

    public static boolean has(String internalName) {
        return elements.containsKey(internalName);
    }

    private static void register(String internalName, EssenceConfig config) {
        if(!config.has(internalName) || has(internalName)) return;
        ElementBuilder builder = new ElementBuilder(internalName)
                .displayName(config.outString(internalName + ".display-name", ""))
                .symbolItem(config.getConfigurationSection(internalName + ".symbol-item"))
                .slot(config.getInteger(internalName + ".slot", -1))
                .multiplier(config.getConfigurationSection(internalName + ".damage-multiplier"));
        elements.put(internalName, builder);
    }

    /**
     * Register a default element "none" if not registered.
     */
    private static void registerDefault() {
        if(elements.containsKey(Element.defaultInternalName)) return;
        ElementBuilder builder = new ElementBuilder(Element.defaultInternalName).displayName("&7None");
        elements.put(Element.defaultInternalName, builder);
    }
}
