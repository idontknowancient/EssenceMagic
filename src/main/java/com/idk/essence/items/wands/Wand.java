package com.idk.essence.items.wands;

import com.idk.essence.Essence;
import com.idk.essence.items.Item;
import com.idk.essence.magics.Magic;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigFile;
import com.idk.essence.utils.placeholders.InternalPlaceholderHandler;
import lombok.Getter;
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

    private static final Essence plugin = Essence.getPlugin();

    public static final Map<String, Wand> wands = new LinkedHashMap<>();

    // signify overall wands
    @Getter private static final NamespacedKey wandKey = new NamespacedKey(plugin, "wand-key");

    // signify specific wand
    private final NamespacedKey uniqueWandKey = new NamespacedKey(plugin, "unique-wand-key");

    private final String name;

    private final Material material;

    private final String displayName;

    private List<String> lore = new ArrayList<>();

    private final boolean glowing;

    private final int customModelData;

    private final ItemStack itemStack;

    private final List<String> info = new ArrayList<>();

    private final double defaultMana;
    // used to storage mana
    @Getter private static final NamespacedKey manaKey = new NamespacedKey(plugin, "mana-key");

    private final String lowerLimit;
    private final String upperLimit;
    @Getter private static final NamespacedKey limitKey = new NamespacedKey(plugin, "limit-key");

    private final double manaInjection;
    @Getter private static final NamespacedKey injectionKey = new NamespacedKey(plugin, "injection-key");

    private final int slot;
    @Getter private static final NamespacedKey slotKey = new NamespacedKey(plugin, "slot-key");

    // show when there is an empty slot in a wand
    @Getter private static String emptyString = "&7[empty]";

    // magicKey will be used in Magic.java
    private final StringBuilder defaultMagic = new StringBuilder();
    @Getter private static final NamespacedKey wandMagicKey = new NamespacedKey(plugin, "wand-magic-key");

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
            defaultMana = cw.getDouble(name + ".default-mana");
        else if(cw.isInteger(name + ".default-mana") && cw.getInteger(name + ".default-mana") >= 0)
            defaultMana = cw.getInteger(name + ".default-mana");
        else
            defaultMana = 0.0;

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
        if(cw.isDouble(name + ".mana-injection") && cw.getDouble(name + ".mana-injection") >= 0)
            manaInjection = cw.getDouble(name + ".mana-injection");
        else if(cw.isInteger(name + ".mana-injection") && cw.getInteger(name + ".mana-injection") >= 0)
            manaInjection = cw.getInteger(name + ".mana-injection");
        else
            manaInjection = 1;

        // set slot (available magic amount) (default to 1)
        if(cw.isInteger(name + ".magic-slot") && cw.getInteger(name + ".magic-slot") >= 0)
            slot = cw.getInteger(name + ".magic-slot");
        else
            slot = 1;

        // set empty string (default to "&7[empty]")
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MESSAGES;
        if(cm.isString("wand-magic-empty"))
            emptyString = cm.outString("wand-magic-empty");

        // set wand magic ([magic];[magic];...;<main>)
        // e.g. fire_beam;0
        int magicAmount = 0;
        int index = 0;
        boolean isList = cw.isList(name + ".default-magic");
        List<String> defaultMagics = isList ? cw.getStringList(name + ".default-magic") : new ArrayList<>();
        // default
        while(magicAmount < slot) {
            // only one magic in a string
            if(magicAmount < 1 && cw.isString(name + ".default-magic")) {
                if(Magic.magics.containsKey(cw.getString(name + ".default-magic"))) {
                    defaultMagic.append(cw.getString(name + ".default-magic")).append(";");
                    magicAmount++;
                }
            } else if(isList && defaultMagics.size() > index) {
                if(Magic.magics.containsKey(defaultMagics.get(index))){
                    defaultMagic.append(defaultMagics.get(index)).append(";");
                    magicAmount++;
                }
                index++;
            } else
                break;
        }
        // empty
        while(magicAmount < slot) {
            defaultMagic.append(";");
            magicAmount++;
        }
        // main magic default to 0 (the first)
        defaultMagic.append("0");

        // set item lore (default to empty)
        if(cw.isList(name + ".lore")) {
            // set internal placeholders
            for(String string : cw.getStringList(name + ".lore")) {
                lore.add(InternalPlaceholderHandler.translatePlaceholders(Util.colorize(string), this));
            }
            lore = Util.splitLore(lore);
        }

        // set wand glowing (default to false)
        if(cw.isBoolean(name + ".glowing"))
            glowing = cw.getBoolean(name + ".glowing");
        else
            glowing = false;

        // set wand model (default to none)
        if(cw.isInteger(name + ".custom-model-data") && cw.getInteger(name + ".custom-model-data") >= 0)
            customModelData = cw.getInteger(name + ".custom-model-data");
        else
            customModelData = -1;

        // set wand item stack
        itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) return;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        if(glowing) {
            itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(customModelData != -1) itemMeta.setCustomModelData(customModelData);

        // unique (unstackable)
        container.set(uniqueWandKey, PersistentDataType.STRING, System.currentTimeMillis()+""+Math.random());
        // custom item
        container.set(Item.getItemKey(), PersistentDataType.STRING, name);
        // wand (internal name)
        container.set(wandKey, PersistentDataType.STRING, name);
        // mana
        container.set(manaKey, PersistentDataType.DOUBLE, defaultMana);
        // limit
        container.set(limitKey, PersistentDataType.STRING, lowerLimit+upperLimit);
        // injection
        container.set(injectionKey, PersistentDataType.DOUBLE, manaInjection);
        // slot
        container.set(slotKey, PersistentDataType.INTEGER, slot);
        // magic
        container.set(wandMagicKey, PersistentDataType.STRING, defaultMagic.toString());

        itemStack.setItemMeta(itemMeta);
    }
}
