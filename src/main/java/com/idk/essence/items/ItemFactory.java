package com.idk.essence.items;

import com.idk.essence.elements.Element;
import com.idk.essence.utils.CustomKey;
import com.idk.essence.utils.configs.ConfigFile;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemFactory {

    private static final Map<String, ItemBuilder> items = new HashMap<>();

    private static ConfigFile.ConfigName ci;

    private ItemFactory() {}

    public static void initialize() {
        items.clear();
        ci = ConfigFile.ConfigName.ITEMS;
        for(String name : ci.getConfig().getKeys(false)) {
            register(name);
        }
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
        String internalName = meta.getPersistentDataContainer().get(CustomKey.getItemKey(), PersistentDataType.STRING);
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
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return false;
        return meta.getPersistentDataContainer().has(CustomKey.getItemKey());
    }

    /**
     * Register a custom item.
     * @param internalName the internal name of the item
     */
    private static void register(String internalName) {
        if(!ci.has(internalName) || items.containsKey(internalName)) return;

        ItemBuilder builder = new ItemBuilder(ci.getString(internalName + ".type", "stone"))
                .displayName(ci.outString(internalName + ".display-name", ""))
                .lore(ci.outStringList(internalName + ".lore"))
                .enchant(ci.getConfigurationSection(internalName + ".enchantments"))
                .flag(ci.getConfigurationSection(internalName + ".options"))
                .element(ci.getString(internalName + ".element", Element.defaultInternalName))
                .persistentDataContainer(PersistentDataType.STRING, internalName);
        items.put(internalName, builder);
    }
}
