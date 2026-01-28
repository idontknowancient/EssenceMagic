package com.idk.essence.items.items;

import com.idk.essence.elements.Element;
import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemFactory {

    private static final Map<String, ItemBuilder> items = new HashMap<>();

    private ItemFactory() {}

    public static void initialize() {
        items.clear();
        ConfigManager.Folder.ITEMS_ITEMS.load(ItemFactory::register);
    }

    /**
     * Get a custom item from a string. If no custom item found, try to get a normal item.
     * @param internalName the internal name of the item
     * @return the corresponding item stack
     */
    @Nullable
    public static ItemStack get(String internalName) {
        ItemBuilder builder = items.get(internalName);
        if(builder != null)
            return builder.build();
        Material material = Material.getMaterial(internalName);
        return material != null ? new ItemStack(material) : null;
    }


    /**
     * Get a custom item from an entity's main hand.
     * @param entity the entity to be checked
     * @return the corresponding item stack
     */
    @Nullable
    public static ItemStack get(LivingEntity entity) {
        EntityEquipment equipment = entity.getEquipment();
        if(equipment == null || !isCustom(equipment.getItemInMainHand())) return null;
        ItemMeta meta = equipment.getItemInMainHand().getItemMeta();
        if(meta == null) return null;
        String internalName = meta.getPersistentDataContainer().get(Key.Class.ITEM.get(), PersistentDataType.STRING);
        return get(internalName);
    }

    public static Collection<String> getAllKeys() {
        return items.keySet();
    }

    public static Collection<ItemStack> getAll() {
        return items.values().stream().map(ItemBuilder::build).toList();
    }

    /**
     * Check if an item stack is a custom item.
     */
    public static boolean isCustom(ItemStack itemStack) {
        if(itemStack == null) return false;
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return false;
        return meta.getPersistentDataContainer().has(Key.Class.ITEM.get());
    }

    /**
     * Check if an entity is holding a custom item.
     */
    public static boolean isHoldingCustom(LivingEntity entity) {
        ItemStack item = Optional.ofNullable(entity.getEquipment()).map(EntityEquipment::getItemInMainHand).orElse(null);
        return isCustom(item);
    }

    /**
     * Check if an item stack is placeable. Only effective for blocks.
     */
    public static boolean isPlaceable(ItemStack item) {
        if(!isCustom(item)) return true;
        return Optional.ofNullable(item.getItemMeta().getPersistentDataContainer()
                .get(Key.Feature.PLACEABLE.get(),  PersistentDataType.BOOLEAN)).orElse(true);
    }

    /**
     * Check if an item stack is usable. Only effective for interactable items.
     */
    public static boolean isUsable(ItemStack item) {
        if(!isCustom(item)) return true;
        return Optional.ofNullable(item.getItemMeta().getPersistentDataContainer()
                .get(Key.Feature.USABLE.get(),  PersistentDataType.BOOLEAN)).orElse(true);
    }

    /**
     * Register a custom item.
     * @param internalName the internal name of the item
     */
    private static void register(String internalName, EssenceConfig config) {
        // Artifacts have higher priority to items. If the artifact is not enabled, item can use its name.
        if(!config.has(internalName) || items.containsKey(internalName) || ArtifactFactory.hasActiveArtifact(internalName)) return;

        ItemBuilder builder = new ItemBuilder(config.getString(internalName + ".material", "stone"))
                .displayName(config.outString(internalName + ".display-name", ""))
                .lore(config.getStringList(internalName + ".lore"))
                .enchant(config.getConfigurationSection(internalName + ".enchantments"))
                .flag(config.getConfigurationSection(internalName + ".options"))
                .element(config.getString(internalName + ".element", Element.defaultInternalName))
                .skill(config.getStringListOrString(internalName + ".skills"))
                .container(internalName);
        items.put(internalName, builder);
    }

    /**
     * Get symbol item based on a section. Can be used as Element or Skill symbol. Automatically handle null section.
     */
    public static void setSymbolItemBuilder(String internalName, ConfigurationSection symbolSection, ItemBuilder builder) {
        if(symbolSection == null) return;
        builder.material(symbolSection.getString("material"))
                .lore(symbolSection.getStringList("description"))
                .glowing(symbolSection.getBoolean("glowing", false))
                .container(Key.Class.ITEM.get(), internalName);
    }
}
