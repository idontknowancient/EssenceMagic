package com.idk.essence.elements;

import com.idk.essence.utils.configs.ConfigFile;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ElementFactory {

    private static final Map<String, Element> elements = new HashMap<>();

    private static ConfigFile.ConfigName ce;

    private ElementFactory() {}

    public static void initialize() {
        elements.clear();
        ce = ConfigFile.ConfigName.ELEMENTS;
        for(String name : ce.getConfig().getKeys(false)) {
            register(name);
        }
    }

    /**
     * Get an element from a string.
     * @param internalName the internal name of the element
     * @return the corresponding element
     */
    @Nullable
    public static Element get(String internalName) {
        return elements.get(internalName);
    }

    private static void register(String internalName) {
        if(!ce.has(internalName) || elements.containsKey(internalName)) return;
        elements.put(internalName, new Element(internalName));
    }
}
