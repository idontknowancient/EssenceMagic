package com.idk.essence.elements;

import com.idk.essence.utils.CustomKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataType;

public class ElementBuilder {

    private final Element element;

    public ElementBuilder(String internalName) {
        element = new Element(internalName);
    }

    public ElementBuilder displayName(String displayName) {
        element.setDisplayName(displayName);
        return this;
    }

    public ElementBuilder symbolItem(String materialString) {
        element.getBuilder().material(materialString);
        return this;
    }

    public ElementBuilder symbolItem(ConfigurationSection symbolSection) {
        if(symbolSection == null) return this;
        element.getBuilder().material(symbolSection.getString("type", "stone"))
                .displayName(element.getDisplayName())
                .lore(symbolSection.getStringList("description"))
                .glow(symbolSection.getBoolean("glowing", false))
                .persistentDataContainer(CustomKey.getElementKey(), PersistentDataType.STRING, element.getInternalName());
        return this;
    }

    public ElementBuilder slot(int slot) {
        if(slot >= 0 && slot <= 53)
            element.setSlot(slot);
        return this;
    }

    public ElementBuilder multiplier(String elementString, double damageMultiplier) {
        element.addDamageMultiplier(elementString, damageMultiplier);
        return this;
    }

    public ElementBuilder multiplier(ConfigurationSection multiplierSection) {
        if(multiplierSection == null) return this;
        for(String s : multiplierSection.getKeys(false)) {
            multiplier(s, multiplierSection.getDouble(s, 1d));
        }
        return this;
    }

    /**
     * For example, if A makes 2x damage to B, B makes 0.5x damage to A
     */
    public void setCounter() {
        element.setCounter();
    }

    /**
     * Convert from primitive damage multiplier to damage multiplier after all elements are registered.
     */
    public void convert() {
        element.convert();
    }

    /**
     * Build an element from the builder.
     * @return the element object
     */
    public Element build() {
        return element;
    }
}
