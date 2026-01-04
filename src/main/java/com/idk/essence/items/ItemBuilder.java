package com.idk.essence.items;

import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.utils.CustomKey;
import com.idk.essence.utils.Util;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private final ItemStack item;

    private final ItemMeta meta;

    private final List<String> lore = new ArrayList<>();

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public ItemBuilder(String materialString) {
        item = new ItemStack(getMaterial(materialString));
        meta = item.getItemMeta();
    }

    public ItemBuilder displayName(String name) {
        meta.setDisplayName(Util.colorize(name));
        return this;
    }

    public ItemBuilder displayName(TextComponent name) {
        meta.setDisplayName(Util.colorize(name.content()));
        return this;
    }

    public ItemBuilder material(String materialString) {
        item.setType(getMaterial(materialString));
        return this;
    }

    public ItemBuilder material(Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder lore(String ... lore) {
        final List<String> formattedLore = Arrays.stream(lore).toList();
        return lore(formattedLore);
    }

    public ItemBuilder lore(TextComponent ... lore) {
        final List<String> formattedLore = Arrays.stream(lore).map(TextComponent::content).toList();
        return lore(formattedLore);
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore.clear();
        this.lore.addAll(lore.stream().map(Util::colorize).toList());
        meta.setLore(this.lore);
        return this;
    }

    public ItemBuilder addLore(String ... lore) {
        final List<String> formattedLore = Arrays.stream(lore).toList();
        return addLore(formattedLore);
    }

    public ItemBuilder addLore(TextComponent ... lore) {
        final List<String> formattedLore = Arrays.stream(lore).map(TextComponent::content).toList();
        return addLore(formattedLore);
    }

    public ItemBuilder addLore(List<String> lore) {
        this.lore.addAll(lore.stream().map(Util::colorize).toList());
        meta.setLore(this.lore);
        return this;
    }

    public ItemBuilder element(String elementString) {
        if(ElementFactory.get(elementString) == null) return this;
        persistentDataContainer(CustomKey.getElementKey(), PersistentDataType.STRING, elementString);
        return this;
    }

    public ItemBuilder element(Element element) {
        if(element == null) return this;
        persistentDataContainer(CustomKey.getElementKey(), PersistentDataType.STRING, element.getInternalName());
        return this;
    }

    public ItemBuilder enchant(String enchantment) {
        return enchant(enchantment, 1);
    }

    public ItemBuilder enchant(Enchantment enchantment) {
        return enchant(enchantment, 1);
    }

    public ItemBuilder enchant(String enchantment, int level) {
        Enchantment enchant = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantment));
        if(enchant != null) enchant(enchant, level);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder enchant(ConfigurationSection enchantmentSection) {
        if(enchantmentSection == null) return this;
        for(String enchantment : enchantmentSection.getKeys(false)) {
            int level = enchantmentSection.getInt(enchantment, 1);
            enchant(enchantment, level);
        }
        return this;
    }

    public ItemBuilder flag(ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }

    public ItemBuilder flag(String flag) {
        flag = flag.toUpperCase().replaceAll("-", "_");
        ItemFlag itemFlag;
        try {
            itemFlag = ItemFlag.valueOf(flag);
            return flag(itemFlag);
        } catch(IllegalArgumentException e) {
            return this;
        }
    }

    public ItemBuilder flag(ConfigurationSection flagSection) {
        if(flagSection == null) return this;
        for(String flag : flagSection.getKeys(false)) {
            if(flagSection.getBoolean(flag, false))
                flag(flag);
        }
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if(glow)
            meta.setEnchantmentGlintOverride(glow);
        return this;
    }

    public <P, C>ItemBuilder persistentDataContainer(NamespacedKey key, PersistentDataType<P, C> type, C value) {
        meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    /**
     * Set persistent data container with default key "item-key".
     */
    public <P, C>ItemBuilder persistentDataContainer(PersistentDataType<P, C> type, C value) {
        return persistentDataContainer(CustomKey.getItemKey(), type, value);
    }

    /**
     * Build an item stack from the builder.
     * Complete with deep copy.
     * Automatically handle default element.
     * @return the brand new item stack
     */
    public ItemStack build() {
        if(!meta.getPersistentDataContainer().has(CustomKey.getElementKey()))
            persistentDataContainer(CustomKey.getElementKey(), PersistentDataType.STRING, Element.defaultInternalName);
        item.setItemMeta(meta);
        return item.clone();
    }

    /**
     * Get a material from a string. Automatically handle uppercase and exception.
     * @param materialString the string to convert
     */
    public static Material getMaterial(String materialString) {
        materialString = materialString.toUpperCase();
        try {
            return Material.valueOf(materialString.toUpperCase());
        } catch(IllegalArgumentException e) {
            return Material.STONE;
        }
    }
}
