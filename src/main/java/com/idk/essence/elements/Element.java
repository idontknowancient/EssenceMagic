package com.idk.essence.elements;

import com.idk.essence.items.ItemBuilder;
import com.idk.essence.utils.configs.ConfigFile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Element {

    /**
     * For example, if A makes 2x damage to B, B makes 0.5x damage to A
     */
    @Getter @Setter private static boolean counterEffect;

    /**
     * Whether to show damage multiplier in the element menu
     */
    @Getter @Setter private static boolean showDamageMultiplier;

    public static final String defaultInternalName = "none";

    @Getter private final String internalName;

    @Getter @Setter private String displayName;

    @Getter private final ItemBuilder builder;

    @Getter @Setter private int slot;

    @Getter private final Map<Element, Double> suppressMap;

    @Getter private final Map<Element, Double> suppressedMap;

    private final Map<String, Double> primitiveDamageMultiplier = new HashMap<>();

    private final Map<Element, Double> damageMultiplier = new HashMap<>();

    public Element(String internalName) {
        builder = new ItemBuilder(Material.STONE);
        this.internalName = internalName;

        suppressMap = new HashMap<>();
        suppressedMap = new HashMap<>();
    }

    public ItemStack getSymbolItem() {
        return builder.build();
    }

    public void addDamageMultiplier(String elementString, double damageMultiplier) {
        this.primitiveDamageMultiplier.put(elementString, damageMultiplier);
    }

    private void addDamageMultiplier(Element element, double damageMultiplier) {
        this.damageMultiplier.put(element, damageMultiplier);
    }

    public double getDamageMultiplier(Element element) {
        return this.damageMultiplier.getOrDefault(element, 1d);
    }

    /**
     * Convert from primitive damage multiplier to damage multiplier after all elements are registered.
     */
    public void convert() {
        for(String s : primitiveDamageMultiplier.keySet()) {
            Element element = ElementFactory.get(s);
            if(element == null) continue;
            this.addDamageMultiplier(element, primitiveDamageMultiplier.get(s));
            if(counterEffect)
                element.addDamageMultiplier(this, 1d / primitiveDamageMultiplier.get(s));
            if(showDamageMultiplier) {
                builder.addLore("", ConfigFile.ConfigName.MENUS.outString(
                        "element.damage-multiplier-text", "&bDamage Multiplier:"));
                builder.addLore(damageMultiplier.entrySet().stream().map(
                        entry->entry.getKey().getDisplayName() + " &7x" +
                        entry.getValue()).toList());
            }
        }
        primitiveDamageMultiplier.clear();
    }
}
