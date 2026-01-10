package com.idk.essence.items;

import com.idk.essence.Essence;
import com.idk.essence.skills.Skill;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Item {

    private static final Essence plugin = Essence.getPlugin();

    // contain the order
    public static final Map<String, Item> items = new LinkedHashMap<>();

    // signify custom items
    @Getter private static final NamespacedKey itemKey = new NamespacedKey(plugin, "item-key");

    @Getter private final ItemStack item;

    @Getter private final ItemMeta itemMeta;

    @Getter private final String name;

    @Getter private final Component displayName;

    @Getter private final Material type;

    @Getter private List<String> lore = null;

    @Getter private final int customModelData;

    @Getter private final String id;

//    @Getter private final Element element;

    @Getter  private final Map<String, Integer> enchantments = new HashMap<>(); //temporarily useless

    @Getter private final List<String> optionList = new ArrayList<>();

    @Getter private final List<String> attributeList = new ArrayList<>();

    @Getter private final List<Skill> skillList = new ArrayList<>();

    {
        String[] options =
                {"hide-enchants", "hide-attributes", "hide-armor-trim", "hide-destroys",
                        "hide-dye", "hide-placed-on", "hide-potion-effects", "hide-unbreakable"};
        optionList.addAll(Arrays.asList(options));

        String[] attributes =
                {"armor", "armor-toughness", "attack-damage", "attack-knockback", "attack-speed", "flying-speed", "follow-range",
                        "knockback-resistance", "luck", "max-absorption", "max-health", "movement-speed"};
        attributeList.addAll(Arrays.asList(attributes));
    }

    public Item(String itemName) {
        ConfigManager.ConfigDefaultFile ci = ConfigManager.ConfigDefaultFile.ITEMS;

        // variable setting
        name = itemName;
        displayName = ci.outString(itemName+".display-name");
        type = Material.valueOf(ci.getString(itemName+".type").toUpperCase());
        if(ci.isList(itemName+".lore"))
            lore = ci.getStringList(itemName+".lore");
        id = getClass().getSimpleName();

        // set item model (default to none)
        if(ci.isInteger(name + ".custom-model-data"))
            customModelData = ci.getInteger(name + ".custom-model-data");
        else
            customModelData = -1;

        // element setting, if not provided, use none instead
//        if(ci.isString(itemName+".element"))
//            element = Element.elements.get(ci.getString(itemName+".element"));
//        else
//            element = Element.elements.get("none");


        // basic setting
        item = new ItemStack(type);
        itemMeta = item.getItemMeta();
        itemMeta.displayName(displayName);
        if(lore != null)
            itemMeta.setLore(lore);
        if(customModelData != -1)
            itemMeta.setCustomModelData(customModelData);

        // enchant setting
        if(ci.isConfigurationSection(itemName+".enchantments"))
            setItemEnchantments(ci.getConfigurationSection(itemName+".enchantments").getKeys(false), ci);

        // options setting
        if(ci.isConfigurationSection(itemName+".options"))
            setItemOptions(ci.getConfigurationSection(itemName+".options").getKeys(false), ci);

        // attributes setting
        if(ci.isConfigurationSection(itemName+".attributes"))
            setItemAttributes(ci.getConfigurationSection(itemName+".attributes").getKeys(false), ci);

        // skills setting
        if(ci.isList(itemName+".skills"))
            setItemSkills(ci.getStringList(itemName+".skills"));

        // key setting
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(itemKey, PersistentDataType.STRING, getId());
        container.set(new NamespacedKey(plugin, name), PersistentDataType.STRING, getId());

        item.setItemMeta(itemMeta);
    }

    private void setItemEnchantments(Set<String> enchantmentSet, ConfigManager.ConfigDefaultFile ci) {
        for(String s : enchantmentSet) {
            // notice some enchantments have different names from we are used to using
            Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(s));
            if(enchantment == null) continue;
            itemMeta.addEnchant(enchantment, ci.getInteger(name+".enchantments."+s), true);
        }
    }

    private void setItemOptions(Set<String> optionSet, ConfigManager.ConfigDefaultFile ci) {
        for(String s : optionSet) {
            String prefix = name+".options.";
            if(optionList.contains(s) && ci.getBoolean(prefix+s)) {
                // turn hide- to hide_
                s = s.replaceAll("-", "_");
                itemMeta.addItemFlags(ItemFlag.valueOf(s.toUpperCase()));
            }
        }
    }

    private void setItemAttributes(Set<String> attributeSet, ConfigManager.ConfigDefaultFile ci) {
        for(String s : attributeSet) {
            String prefix = name + ".attributes.";
            if(attributeList.contains(s)) {
                String s_ = s.replaceAll("-", "_");
                //add, highest priority
                if(getAttributeType(s, ci) == 0) {
                    itemMeta.addAttributeModifier(Attribute.valueOf("GENERIC_"+s_.toUpperCase()),
                            new AttributeModifier(UUID.randomUUID(), s_, ci.getDouble(prefix+s+".value"), AttributeModifier.Operation.ADD_NUMBER,
                                    EquipmentSlot.valueOf(ci.getString(prefix+s+".slot").toUpperCase())));
                    Util.consoleOuts("using add method");
                }
                //multiply A*(1+a+b+c) usually smaller, second highest
                if(getAttributeType(s, ci) == 1) {
                    itemMeta.addAttributeModifier(Attribute.valueOf("GENERIC_"+s_.toUpperCase()),
                            new AttributeModifier(UUID.randomUUID(), s, ci.getDouble(prefix+s+".value"), AttributeModifier.Operation.ADD_SCALAR,
                                    EquipmentSlot.valueOf(ci.getString(prefix+s+".slot").toUpperCase())));
                }
                //multiply A*(1+a)(1+b)(1+c) usually larger, lowest
                if(getAttributeType(s, ci) == 2) {
                    itemMeta.addAttributeModifier(Attribute.valueOf("GENERIC_"+s_.toUpperCase()),
                            new AttributeModifier(UUID.randomUUID(), s, ci.getDouble(prefix+s+".value"), AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                                    EquipmentSlot.valueOf(ci.getString(prefix+s+".slot").toUpperCase())));
                }
            }
        }
    }

    private void setItemSkills(List<String> skillNameList) {
        for(String s : skillNameList) {
            if(Skill.skills.containsKey(s))
                skillList.add(Skill.skills.get(s));
        }
    }

    //get the type of the attribute and return an int
    //add - 0 / multiply - 1 /
    private int getAttributeType(String attributeName, ConfigManager.ConfigDefaultFile ci) {
        if(ci.getString(name+".attributes."+attributeName+".type").equalsIgnoreCase("add")) return 0;
        if(ci.getString(name+".attributes."+attributeName+".type").equalsIgnoreCase("multiply-continuous")) return 1;
        if(ci.getString(name+".attributes."+attributeName+".type").equalsIgnoreCase("multiply-separate")) return 2;

        //-1 represents error
        return -1;
    }
}