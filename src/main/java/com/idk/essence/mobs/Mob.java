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

    @Getter private final EntityType type;

    @Getter private final ItemBuilder builder;

    @Getter private final double health;

    @Getter private final Element element;

    @Getter private final Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();

    public Mob(String internalName) {
        builder = new ItemBuilder(Material.STONE);
        this.internalName = internalName;

        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MOBS;
        String pre = internalName + ".";
        displayName = cm.isString(pre + "display-name") ? cm.outString(pre + "display-name") : "";
        type = EntityType.valueOf(cm.getString(pre + "type").toUpperCase());
        if(cm.isDouble(pre + "health"))
            health = cm.getDouble(pre + "health");
        else if(cm.isInteger(pre + "health"))
            health = cm.getInteger(pre + "health");
        else
            health = -1; //-1 means default value
        element = cm.isString(pre + "element") ? ElementFactory.get(cm.getString(pre + "element"))
                : ElementFactory.getDefault();
//        description = cm.isList(pre + "description") ? cm.outStringList(pre + "description") :
//                new ArrayList<>();

        if(cm.isConfigurationSection(pre + "equipment")) {
            ConfigurationSection equipmentSection = cm.getConfigurationSection(pre + "equipment");
            for(String s : equipmentSection.getKeys(false)) {
                ItemStack item;
                String itemName = equipmentSection.getString(s);
                if(Item.items.containsKey(itemName))
                    item = Item.items.get(itemName).getItem();
//                else if(itemName != null)
//                    item = new ItemStack(Material.valueOf(itemName.toUpperCase()));
                else
                    item = new ItemStack(Material.IRON_SWORD);
                // turn off-hand to off_hand
                s = s.replaceAll("-", "_");
                equipment.put(EquipmentSlot.valueOf(s.toUpperCase()), item);
            }
        }
    }
}
