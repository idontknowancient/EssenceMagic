package com.idk.essence.elements;

import com.idk.essence.items.items.ItemBuilder;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

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

    @Getter @Setter private Component displayName;

    @Getter private final ItemBuilder itemBuilder;

    @Getter @Setter private int slot;

    private final Map<String, Double> primitiveDamageMultiplier = new LinkedHashMap<>();

    private final Map<Element, Double> damageMultiplier = new LinkedHashMap<>();

    public Element(String internalName) {
        itemBuilder = new ItemBuilder(Material.STONE);
        this.internalName = internalName;
    }

    public ItemStack getSymbolItem() {
        return itemBuilder.build();
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
     * For example, if A makes 2x damage to B, B makes 0.5x damage to A
     */
    public void setCounter() {
        if(!counterEffect) return;
        primitiveDamageMultiplier.forEach((id, multiplier) ->
                Optional.ofNullable(ElementFactory.get(id)).ifPresent(
                        e -> e.addDamageMultiplier(internalName, Util.Tool.round(1.0 / multiplier, 4)))
        );
    }

    /**
     * Convert from primitive damage multiplier to damage multiplier after all elements are registered.
     */
    public void convert() {
        primitiveDamageMultiplier.forEach((id, multiplier) ->
                Optional.ofNullable(ElementFactory.get(id)).ifPresent(
                        e -> this.addDamageMultiplier(e, multiplier))
        );
        if(showDamageMultiplier) {
            itemBuilder.addLore("", ConfigManager.DefaultFile.MENUS.getString(
                    "element.damage-multiplier-text", "&bDamage Multiplier:"));
            // Entry<Element, Double> -> Stream<String>
            itemBuilder.addLore(damageMultiplier.entrySet().stream().map(
                    entry -> MiniMessage.miniMessage().serialize(entry.getKey().getDisplayName()) + " &7x" +
                            entry.getValue()).toList());
        }
        primitiveDamageMultiplier.clear();
    }
}
