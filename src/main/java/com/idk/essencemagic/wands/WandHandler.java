package com.idk.essencemagic.wands;

import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class WandHandler {

    public static void initialize() {
        Wand.wands.clear();
        setWands();
    }

    private static void setWands() {
        Set<String> wandSet = ConfigFile.ConfigName.WANDS.getConfig().getKeys(false);
        for(String wand : wandSet) {
            Wand.wands.put(wand, new Wand(wand));
        }
    }

    public static boolean isWand(ItemStack i) {
        return i.getItemMeta() != null &&
                i.getItemMeta().getPersistentDataContainer().has(Wand.getWandKey());
    }

    public static boolean isHoldingWand(LivingEntity entity) {
        if(entity.getEquipment() == null) return false;
        ItemStack itemInMainHand = entity.getEquipment().getItemInMainHand();
        return isWand(itemInMainHand);
    }

    //return the corresponding wand
    @Nullable
    public static Wand getCorrespondingWand(ItemStack itemStack) {
        if(itemStack.getItemMeta() == null) return null;
        for(Wand w : Wand.wands.values()) {
            if(w.getItemStack().getItemMeta() == null) continue;
            if(w.getItemStack().getItemMeta().getPersistentDataContainer()
                    .equals(itemStack.getItemMeta().getPersistentDataContainer())) {
                return w;
            }
        }
        return null;
    }

    //return the corresponding wand in an entity's main hand
    @Nullable
    public static Wand getCorrespondingWand(LivingEntity entity) {
        if(entity.getEquipment() == null ||
                entity.getEquipment().getItemInMainHand().getItemMeta() == null) return null;
        for(Wand w : Wand.wands.values()) {
            if(w.getItemStack().getItemMeta() == null) continue;
            if(w.getItemStack().getItemMeta().getPersistentDataContainer()
                    .equals(entity.getEquipment().getItemInMainHand().getItemMeta().getPersistentDataContainer())) {
                return w;
            }
        }
        return null;
    }
}
