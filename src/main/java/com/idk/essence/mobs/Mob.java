package com.idk.essence.mobs;

import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.items.Item;
import com.idk.essence.items.ItemBuilder;
import com.idk.essence.utils.configs.ConfigFile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Mob {

    @Getter private final String internalName;

    @Getter @Setter private String displayName;

    @Getter @Setter private EntityType type;

    @Getter private final ItemBuilder builder;

    @Getter @Setter private double health;

    @Getter @Setter private Element element;

    private final Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();

    public void addEquipment(EquipmentSlot slot, ItemStack item) {
        equipment.put(slot, item);
    }

    public ItemStack getHelmet() {
        return equipment.get(EquipmentSlot.HEAD);
    }

    public ItemStack getChestplate() {
        return equipment.get(EquipmentSlot.BODY);
    }

    public ItemStack getLeggings() {
        return equipment.get(EquipmentSlot.LEGS);
    }

    public ItemStack getBoots() {
        return equipment.get(EquipmentSlot.FEET);
    }

    public ItemStack getMainHand() {
        return equipment.get(EquipmentSlot.HAND);
    }

    public ItemStack getOffHand() {
        return equipment.get(EquipmentSlot.OFF_HAND);
    }

    public Mob(String internalName) {
        builder = new ItemBuilder(Material.STONE);
        this.internalName = internalName;
    }
}
