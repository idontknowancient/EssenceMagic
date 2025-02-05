package com.idk.essencemagic.mobs;

import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

public class MobHandler {

    public static void initialize() {
        Mob.mobs.clear();
        setMobs();
    }

    private static void setMobs() {
        Set<String> mobSet = ConfigFile.ConfigName.MOBS.getConfig().getKeys(false);
        for(String s : mobSet) {
            Mob.mobs.put(s, new Mob(s));
        }
    }

    public static boolean isCustomMob(LivingEntity entity) {
        return entity.getPersistentDataContainer().has(Mob.getMobKey());
    }

    public static Mob getCorrespondingMob(LivingEntity entity) {
        for(Mob mob : Mob.mobs.values()) {
            if(entity.getPersistentDataContainer().has(mob.getUniqueKey()))
                return mob;
        }
        return null;
    }

    public static void spawnMob(Location location, Mob mob) {
        if(location.getWorld() == null) return;
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, mob.getType());
        entity.setCustomName(mob.getDisplayName());
        if(mob.getHealth() != -1) {
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mob.getHealth());
            entity.setHealth(mob.getHealth());
        }
        if(!mob.getEquipmentMap().isEmpty()) {
            for(EquipmentSlot slot : mob.getEquipmentMap().keySet()) {
                if(entity.getEquipment() == null) continue;
                entity.getEquipment().setItem(slot, mob.getEquipmentMap().get(slot));
            }
        }
        //uniform container for all custom mobs
        entity.getPersistentDataContainer().set(Mob.getMobKey(), PersistentDataType.STRING, mob.getId());
        //unique container for one custom mob
        entity.getPersistentDataContainer().set(mob.getUniqueKey(), PersistentDataType.STRING, mob.getId());
    }
}
