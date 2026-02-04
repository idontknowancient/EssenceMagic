package com.idk.essence.elements;

import com.idk.essence.items.items.ItemFactory;
import com.idk.essence.utils.messages.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;

public class ElementBuilder {

    private final Element element;

    public ElementBuilder(String internalName) {
        element = new Element(internalName);
    }

    public ElementBuilder displayName(String displayName) {
        element.setDisplayName(Message.parse(displayName));
        element.getItemBuilder().displayName(Message.parse(displayName));
        return this;
    }

    public ElementBuilder displayName(Component displayName) {
        element.setDisplayName(displayName);
        element.getItemBuilder().displayName(displayName);
        return this;
    }

    public ElementBuilder symbolItem(String materialString) {
        element.getItemBuilder().material(materialString);
        return this;
    }

    public ElementBuilder symbolItem(ConfigurationSection symbolSection) {
        if(symbolSection == null) return this;
        ItemFactory.setSymbolItemBuilder(element.getInternalName(), symbolSection, element.getItemBuilder());
        return this;
    }

    public ElementBuilder slot(int slot) {
        if(slot >= 0 && slot <= 53)
            element.setSlot(slot);
        return this;
    }

    public ElementBuilder aptitudeChance(double aptitudeChance) {
        element.setAptitudeChance(Math.clamp(aptitudeChance, 0, 1));
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
