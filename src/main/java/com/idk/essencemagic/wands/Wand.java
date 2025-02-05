package com.idk.essencemagic.wands;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Wand {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    public static final Map<String, Wand> wands = new LinkedHashMap<>();

    // signify custom items
    @Getter private static final NamespacedKey itemKey = new NamespacedKey(plugin, "item-key");

    // signify wands
    @Getter private static final NamespacedKey wandKey = new NamespacedKey(plugin, "wand-key");

    @Getter private final String name;

    @Getter private final Material material;

    @Getter private final String displayName;

    @Getter private final List<String> lore = new ArrayList<>();

    @Getter private final String lowerLimit;

    @Getter private final String upperLimit;

    @Getter private final String manaVelocity;

    @Getter private final boolean glowing;

    @Getter private final ItemStack itemStack;

    @Getter private final List<String> info = new ArrayList<>();

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

        // set item lore (default to empty)
        if(cw.isList(name + ".lore"))
            lore.addAll(cw.getStringList(name + ".lore"));
        lore.replaceAll(Util::colorize);

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

        // set wand mana velocity (default to 1/20)(amount/ticks)
        if(cw.isString(name + ".mana-velocity"))
            manaVelocity = cw.getString(name + ".mana-velocity");
        else
            manaVelocity = "1/20";

        // set wand glowing (default to false)
        if(cw.isBoolean(name + ".glowing"))
            glowing = cw.getBoolean(name + ".glowing");
        else
            glowing = false;

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
        itemMeta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, name);
        itemMeta.getPersistentDataContainer().set(wandKey, PersistentDataType.STRING, name);
        itemStack.setItemMeta(itemMeta);
    }
}
