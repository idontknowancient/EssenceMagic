package com.idk.essencemagic.wands;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.placeholders.InternalPlaceholderHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Wand {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    public static final Map<String, Wand> wands = new LinkedHashMap<>();

    // storage wands with specific info like mana
    public static final Map<PersistentDataContainer, Wand> specificWands = new LinkedHashMap<>();

    // signify overall wands
     @Getter private static final NamespacedKey wandKey = new NamespacedKey(plugin, "wand-key");

     // signify specific wands
     @Setter private NamespacedKey specificWandKey;

     private final String name;

     private final Material material;

     private final String displayName;

     @Setter private double storageMana;

     private final List<String> lore = new ArrayList<>();

     private final String lowerLimit;

     private final String upperLimit;

     private final double manaInjection;

     private final boolean glowing;

     private final int customModelData;

     private final ItemStack itemStack;

     private final List<String> info = new ArrayList<>();

    public Wand(String wandName) {
        ConfigFile.ConfigName cw = ConfigFile.ConfigName.WANDS;
        name = wandName;

        // set wand material (default to stick)
        if(cw.isString(name + ".material"))
            material = Material.valueOf(cw.getString(name + ".material").toUpperCase());
        else
            material = Material.STICK;

        // set wand display name (default to the name of stick)
        if(cw.isString(name + ".name"))
            displayName = Util.colorize(cw.getString(name + ".name"));
        else
            displayName = material.name();

        // set storage mana (default to 0)
        if(cw.isDouble(name + ".default-mana") && cw.getDouble(name + ".default-mana") >= 0)
            storageMana = cw.getDouble(name + ".default-mana");
        else if(cw.isInteger(name + ".default-mana") && cw.getInteger(name + ".default-mana") >= 0)
            storageMana = cw.getInteger(name + ".default-mana");
        else
            storageMana = 0.0;

        // set item lore (default to empty)
        if(cw.isList(name + ".lore")) {
            // set internal placeholders
            for(String string : cw.getStringList(name + ".lore")) {
                lore.add(InternalPlaceholderHandler.translatePlaceholders(Util.colorize(string), this));
            }
        }

        // set wand lower limit (default to F;0)
        if(cw.isString(name + ".lower-limit"))
            lowerLimit = cw.getString(name + ".lower-limit");
        else
            lowerLimit = "F;0";

        // set wand upper limit (default to F;0)
        if(cw.isString(name + ".upper-limit"))
            upperLimit = cw.getString(name + ".upper-limit");
        else
            upperLimit = "F;0";

        // set wand mana injection (default to 1)(per right click)
        if(cw.isDouble(name + ".mana-injection"))
            manaInjection = cw.getDouble(name + ".mana-injection");
        else if(cw.isInteger(name + ".mana-injection"))
            manaInjection = cw.getInteger(name + ".mana-injection");
        else
            manaInjection = 1;

        // set wand glowing (default to false)
        if(cw.isBoolean(name + ".glowing"))
            glowing = cw.getBoolean(name + ".glowing");
        else
            glowing = false;

        // set wand model (default to none)
        if(cw.isInteger(name + ".custom-model-data"))
            customModelData = cw.getInteger(name + ".custom-model-data");
        else
            customModelData = -1;

        // set wand item stack
        itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) return;
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        if(glowing) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(customModelData != -1) itemMeta.setCustomModelData(customModelData);
        // custom item
        itemMeta.getPersistentDataContainer().set(Item.getItemKey(), PersistentDataType.STRING, name);
        // wand
        itemMeta.getPersistentDataContainer().set(wandKey, PersistentDataType.STRING, name);

        itemStack.setItemMeta(itemMeta);
    }
}
