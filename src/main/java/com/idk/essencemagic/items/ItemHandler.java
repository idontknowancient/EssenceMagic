package com.idk.essencemagic.items;

import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ItemHandler {

    public static void initialize() {
        Item.items.clear();
        setItems();
    }

    public static void setItems() {
        Set<String> itemSet = ConfigFile.ConfigName.ITEMS.getConfig().getKeys(false); //directly use getKeys, not getDefaultSection
        for(String s : itemSet) { //register items
            Item.items.put(s, new Item(s));
        }
    }

    public static boolean isCustomItem(ItemStack i) {
        return i.getItemMeta() != null &&
                i.getItemMeta().getPersistentDataContainer().has(Item.getItemKey());
    }

    public static boolean isHoldingCustomItem(LivingEntity entity) {
        if(entity.getEquipment() == null) return false;
        ItemStack itemInMainHand = entity.getEquipment().getItemInMainHand();
        return itemInMainHand.getItemMeta() != null &&
                itemInMainHand.getItemMeta().getPersistentDataContainer().has(Item.getItemKey());
    }

    //return the corresponding item
    @Nullable
    public static Item getCorrespondingItem(ItemStack itemStack) {
        if(itemStack.getItemMeta() == null) return null;
        for(Item i : Item.items.values()) {
            if(i.getItem().getItemMeta() == null) continue;
            if(i.getItem().getItemMeta().getPersistentDataContainer()
                    .equals(itemStack.getItemMeta().getPersistentDataContainer())) {
                return i;
            }
        }
        return null;
    }

    //return the corresponding item in an entity's main hand
    @Nullable
    public static Item getCorrespondingItem(LivingEntity entity) {
        if(entity.getEquipment() == null ||
                entity.getEquipment().getItemInMainHand().getItemMeta() == null) return null;
        for(Item i : Item.items.values()) {
            if(i.getItem().getItemMeta() == null) continue;
            if(i.getItem().getItemMeta().getPersistentDataContainer()
                    .equals(entity.getEquipment().getItemInMainHand().getItemMeta().getPersistentDataContainer())) {
                return i;
            }
        }
        return null;
    }
}
