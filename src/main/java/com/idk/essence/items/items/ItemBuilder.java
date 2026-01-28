package com.idk.essence.items.items;

import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.skills.SkillManager;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Guide to adding a field:
 * 1. Add a field
 * 2. Add setters
 * 3. Add apply
 * 4. Use apply
 */
public class ItemBuilder {

    private ItemStack item = null;
    @NotNull private Material material;
    private Component displayName;
    private final List<Component> lore = new ArrayList<>();
    private Element element;
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private final Map<ItemFlag, Boolean> flags = new HashMap<>();
    private boolean forceGlowing = false;
    private boolean glowing;
    private final List<String> skills = new ArrayList<>();
    private final List<Consumer<PersistentDataContainer>> persistentData = new ArrayList<>();

    public ItemBuilder(@NonNull Material material) {
        this.material = material;
    }

    public ItemBuilder(String materialString) {
        material = getMaterial(materialString);
    }

    public ItemBuilder displayName(String name) {
        displayName = Util.System.parseMessage(name);
        return this;
    }

    public ItemBuilder displayName(Component name) {
        displayName = name;
        return this;
    }

    private void applyDisplayName(ItemMeta meta) {
        if(displayName != null)
            meta.displayName(displayName);
    }

    public ItemBuilder material(String materialString) {
        material = getMaterial(materialString);
        return this;
    }

    public ItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder lore(String ... lore) {
        this.lore.clear();
        return addLore(lore);
    }

    public ItemBuilder lore(Component ... lore) {
        this.lore.clear();
        return addLore(lore);
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore.clear();
        return addLore(lore);
    }

    public ItemBuilder addLore(String ... lore) {
        this.lore.addAll(Arrays.stream(lore).map(Util.System::parseMessage).toList());
        return this;
    }

    public ItemBuilder addLore(Component ... lore) {
        this.lore.addAll(Arrays.stream(lore).toList());
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        this.lore.addAll(lore.stream().map(Util.System::parseMessage).toList());
        return this;
    }

    private void applyLore(ItemMeta meta) {
        meta.lore(lore);
    }

    public ItemBuilder element(String elementString) {
        element = ElementFactory.getOrDefault(elementString);
        return this;
    }

    public ItemBuilder element(Element element) {
        if(element == null) element = ElementFactory.getDefault();
        this.element = element;
        return this;
    }

    private void applyElement(PersistentDataContainer container) {
        if(element != null)
            container.set(Key.Type.ELEMENT.getKey(), PersistentDataType.STRING, element.getInternalName());
        else
            container.set(Key.Type.ELEMENT.getKey(), PersistentDataType.STRING, Element.defaultInternalName);
    }

    public ItemBuilder enchant(String enchantment) {
        return enchant(enchantment, 1);
    }

    public ItemBuilder enchant(Enchantment enchantment) {
        return enchant(enchantment, 1);
    }

    public ItemBuilder enchant(String enchantmentString, int level) {
        Enchantment enchantment = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.ENCHANTMENT).getOrThrow(NamespacedKey.minecraft(enchantmentString));
        return enchant(enchantment, level);
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        enchantments.put(enchantment, Math.max(level, 1));
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

    private void applyEnchantments(ItemMeta meta) {
        for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet())
            meta.addEnchant(entry.getKey(), entry.getValue(), true);
    }

    public ItemBuilder flag(ItemFlag flag) {
        return flag(flag, true);
    }

    public ItemBuilder flag(ItemFlag flag, boolean value) {
        flags.put(flag, value);
        return this;
    }

    public ItemBuilder flag(String flag) {
        flag = flag.toUpperCase().replaceAll("-", "_");
        ItemFlag itemFlag;
        try {
            itemFlag = ItemFlag.valueOf(flag);
            return flag(itemFlag, true);
        } catch(IllegalArgumentException e) {
            return this;
        }
    }

    public ItemBuilder flag(String flag, boolean value) {
        flag = flag.toUpperCase().replaceAll("-", "_");
        ItemFlag itemFlag;
        try {
            itemFlag = ItemFlag.valueOf(flag);
            return flag(itemFlag, value);
        } catch(IllegalArgumentException e) {
            return this;
        }
    }

    public ItemBuilder flag(ConfigurationSection flagSection) {
        if(flagSection == null) return this;
        for(String flag : flagSection.getKeys(false)) {
            flag(flag, flagSection.getBoolean(flag, false));
        }
        return this;
    }

    private void applyFlags(ItemMeta meta) {
        for(Map.Entry<ItemFlag, Boolean> entry : flags.entrySet())
            meta.addItemFlags(entry.getKey());
    }

    public ItemBuilder glowing(boolean glowing) {
        forceGlowing = true;
        this.glowing = glowing;
        return this;
    }

    private void applyGlowing(ItemMeta meta) {
        if(forceGlowing)
            meta.setEnchantmentGlintOverride(glowing);
    }

    public ItemBuilder skill(List<String> skillStrings) {
        skills.addAll(skillStrings.stream().filter(SkillManager::has).toList());
        return this;
    }

    private void applySkill(PersistentDataContainer container) {
        // e.g. [a, b, c] -> a;b;c;
        container.set(Key.Type.SKILL.getKey(), PersistentDataType.STRING,
                skills.stream().collect(Collectors.joining(";", "", ";")));
    }

    public <T> ItemBuilder container(Key.Type<T> key, T value) {
        persistentData.add(container -> key.set(container, value));
        return this;
    }

    public <T> ItemBuilder container(Key.Feature<T> key, T value) {
        persistentData.add(container -> key.set(container, value));
        return this;
    }

    /**
     * Set persistent data container with default key "item-key".
     */
    public ItemBuilder container(String value) {
        persistentData.add(container -> Key.Type.ITEM.set(container, value));
        return container(Key.Type.ITEM, value);
    }

    private void applyContainer(PersistentDataContainer container) {
        persistentData.forEach(consumer -> consumer.accept(container));
    }

    /**
     * Apply all fields to the item stack.
     */
    private void generate() {
        item = ItemStack.of(material);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        applyDisplayName(meta);
        applyLore(meta);
        applyElement(container);
        applyEnchantments(meta);
        applyFlags(meta);
        applyGlowing(meta);
        applySkill(container);
        applyContainer(container);

        item.setItemMeta(meta);
    }

    /**
     * Build an item stack from the builder.
     * Complete with deep copy.
     * Automatically handle default element.
     * @return the brand new item stack
     */
    public ItemStack build() {
        if(item == null) generate();
        return item.clone();
    }

    /**
     * Get a material from a string. Automatically handle uppercase and exception.
     * @param materialString the string to convert
     */
    public static Material getMaterial(String materialString) {
        if(materialString == null) return Material.STONE;
        return Optional.ofNullable(Material.matchMaterial(materialString)).orElse(Material.STONE);
    }
}
