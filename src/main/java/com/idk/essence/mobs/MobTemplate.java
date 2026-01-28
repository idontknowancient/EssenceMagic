package com.idk.essence.mobs;

import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.items.items.ItemFactory;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Optional;

public class MobTemplate {

    @Getter private final Mob mob;

    public MobTemplate(String internalName) {
        mob = new Mob(internalName);
    }

    public MobTemplate displayName(String displayName) {
        mob.setDisplayName(Util.System.parseMessage(displayName));
        mob.getItemBuilder().displayName(displayName);
        return this;
    }

    public MobTemplate displayName(Component displayName) {
        mob.setDisplayName(displayName);
        mob.getItemBuilder().displayName(displayName);
        return this;
    }

    public MobTemplate type(String typeString) {
        mob.setType(getEntityType(typeString));
        mob.getItemBuilder().material(getSpawnEgg(mob.getType()));
        return this;
    }

    public MobTemplate type(EntityType type) {
        mob.setType(type);
        mob.getItemBuilder().material(getSpawnEgg(type));
        return this;
    }

    public MobTemplate health(double health) {
        if(health > 0)
            mob.setHealth(health);
        return this;
    }

    public MobTemplate element(String elementString) {
        mob.setElement(ElementFactory.getOrDefault(elementString));
        return this;
    }

    public MobTemplate element(Element element) {
        mob.setElement(element != null ? element : ElementFactory.getDefault());
        return this;
    }

    public MobTemplate description(List<String> description) {
        mob.getItemBuilder().lore(description);
        return this;
    }

    /**
     * Add equipment to the mob. No action if itemName does not correspond to any custom or normal item.
     */
    public MobTemplate equipment(EquipmentSlot slot, String itemName) {
        Optional.ofNullable(ItemFactory.get(itemName)).ifPresent(item -> mob.addEquipment(slot, item));
        return this;
    }

    public MobTemplate equipment(ConfigurationSection equipSection) {
        if(equipSection == null) return this;
        for(String s : equipSection.getKeys(false)) {
            if(s.equalsIgnoreCase("HEAD"))
                equipment(EquipmentSlot.HEAD, equipSection.getString(s, "there_is_nothing_and_neither_is_my_love"));
            if(s.equalsIgnoreCase("BODY"))
                equipment(EquipmentSlot.BODY, equipSection.getString(s, "there_is_nothing_and_neither_is_my_love"));
            if(s.equalsIgnoreCase("LEGS"))
                equipment(EquipmentSlot.LEGS, equipSection.getString(s, "there_is_nothing_and_neither_is_my_love"));
            if(s.equalsIgnoreCase("FEET"))
                equipment(EquipmentSlot.FEET, equipSection.getString(s, "there_is_nothing_and_neither_is_my_love"));
            if(s.equalsIgnoreCase("HAND"))
                equipment(EquipmentSlot.HAND, equipSection.getString(s, "there_is_nothing_and_neither_is_my_love"));
            if(s.equalsIgnoreCase("OFF-HAND") || s.equalsIgnoreCase("OFF_HAND"))
                equipment(EquipmentSlot.OFF_HAND, equipSection.getString(s, "there_is_nothing_and_neither_is_my_love"));
        }
        return this;
    }

    public <P, C>MobTemplate persistentDataContainer(PersistentDataContainer container, NamespacedKey key,
                                                     PersistentDataType<P, C> type, C value) {
        container.set(key, type, value);
        return this;
    }

    /**
     * Set persistent data container with default key "mob-key".
     */
    public <P, C>MobTemplate persistentDataContainer(PersistentDataContainer container, PersistentDataType<P, C> type, C value) {
        return persistentDataContainer(container, Key.Type.MOB.getKey(), type, value);
    }

    public void spawn(Location location) {
        World world = location.getWorld();
        if(world == null) return;
        Entity entity = world.spawnEntity(location, mob.getType());
        PersistentDataContainer container = entity.getPersistentDataContainer();

        entity.customName(mob.getDisplayName());
        if(entity instanceof LivingEntity livingEntity) {
            Optional.ofNullable(livingEntity.getAttribute(Attribute.MAX_HEALTH))
                    .ifPresent(ins -> ins.setBaseValue(mob.getHealth()));
            livingEntity.setHealth(mob.getHealth());
            Optional.ofNullable(livingEntity.getEquipment())
                    .ifPresent(e -> {
                        e.setHelmet(mob.getHelmet());
                        e.setChestplate(mob.getChestplate());
                        e.setLeggings(mob.getLeggings());
                        e.setBoots(mob.getBoots());
                        e.setItemInMainHand(mob.getMainHand());
                        e.setItemInOffHand(mob.getOffHand());
                    });
        }
        persistentDataContainer(container, PersistentDataType.STRING, mob.getInternalName());
        persistentDataContainer(container, Key.Type.ELEMENT.getKey(),
                PersistentDataType.STRING, mob.getElement().getInternalName());
    }

    /**
     * Get an entity type from a string. Automatically handle uppercase and exception.
     * @param entityTypeString the string to convert
     */
    public static EntityType getEntityType(String entityTypeString) {
        entityTypeString = entityTypeString.toUpperCase();
        try {
            return EntityType.valueOf(entityTypeString.toUpperCase());
        } catch(IllegalArgumentException e) {
            return EntityType.ZOMBIE;
        }
    }

    /**
     * Get a spawn egg material from an entity type. Automatically handle uppercase and exception.
     */
    private static Material getSpawnEgg(EntityType type) {
        return Optional.ofNullable(Material.getMaterial(type.name().toUpperCase() + "_SPAWN_EGG"))
                .orElse(Material.ZOMBIE_SPAWN_EGG);
    }

}
