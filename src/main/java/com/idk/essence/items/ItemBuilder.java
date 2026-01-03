package com.idk.essence.items;

import com.idk.essence.Essence;
import lombok.Getter;
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

    @Getter private static final NamespacedKey itemKey = new NamespacedKey(Essence.getPlugin(), "item-key");

    private final ItemStack item;

    private final ItemMeta meta;

    private final List<String> lore = new ArrayList<>();

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public ItemBuilder(String materialString) {
        materialString = materialString.toUpperCase();
        Material material;
        try {
            material = Material.valueOf(materialString.toUpperCase());
        } catch(IllegalArgumentException e) {
            material = Material.STONE;
        }
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public ItemBuilder displayName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder displayName(TextComponent name) {
        meta.setDisplayName(name.content());
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
        this.lore.addAll(lore);
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
        this.lore.addAll(lore);
        meta.setLore(this.lore);
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

    public <P, C>ItemBuilder persistentDataContainer(PersistentDataType<P, C> type, C value) {
        meta.getPersistentDataContainer().set(itemKey, type, value);
        return this;
    }

    /**
     * Build an item stack from the builder.
     * Complete with deep copy.
     * @return the brand new item stack
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item.clone();
    }
}
