package com.idk.essencemagic.mobs;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mob {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    public static final HashMap<String, Mob> mobs = new HashMap<>();

    @Getter private static final NamespacedKey mobKey = new NamespacedKey(plugin, "mob-key");

    @Getter private final String name;

    @Getter private final String displayName;

    @Getter private final EntityType type;

    @Getter private final double health;

    @Getter private final Element element;

    @Getter private final List<String> description;

    @Getter private final Map<EquipmentSlot, ItemStack> equipmentMap;

    @Getter private final NamespacedKey uniqueKey;

    @Getter private final String id;

    public Mob(String mobName) {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MOBS;
        String pre = mobName + ".";
        name = mobName;
        displayName = cm.isString(pre + "display_name") ? cm.outString(pre + "display_name") : "";
        type = EntityType.valueOf(cm.getString(pre + "type").toUpperCase());
        if(cm.isDouble(pre + "health"))
            health = cm.getDouble(pre + "health");
        else if(cm.isInteger(pre + "health"))
            health = cm.getInteger(pre + "health");
        else
            health = -1; //-1 means default value
        element = cm.isString(pre + "element") ? Element.elements.get(cm.getString(pre + "element"))
                : Element.elements.get("none");
        description = cm.isList(pre + "description") ? cm.outStringList(pre + "description") :
                new ArrayList<>();

        equipmentMap = new HashMap<>();
        if(cm.isConfigurationSection(pre + "equipment")) {
            ConfigurationSection equipmentSection = cm.getConfigurationSection(pre + "equipment");
            for(String s : equipmentSection.getKeys(false)) {
                ItemStack item;
                String itemName = equipmentSection.getString(s);
                if(Item.items.containsKey(itemName))
                    item = Item.items.get(itemName).getItem();
                else
                    item = new ItemStack(Material.valueOf(itemName.toUpperCase()));
                equipmentMap.put(EquipmentSlot.valueOf(s.toUpperCase()), item);
            }
        }

        uniqueKey = new NamespacedKey(plugin, name);
        id = name.getClass().getSimpleName();
    }
}
