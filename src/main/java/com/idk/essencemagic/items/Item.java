package com.idk.essencemagic.items;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Item {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    public static final Map<String, Item> items = new HashMap<>();

    @Getter private static final NamespacedKey itemKey = new NamespacedKey(plugin, "item-key");

    @Getter private final ItemStack item;

    @Getter private final ItemMeta itemMeta;

    @Getter private final String name;

    @Getter private final String displayName;

    @Getter private final Material type;

    @Getter private List<String> lore = null;

    @Getter private final String id;

    @Getter private final Element element;

    @Getter  private final Map<String, Integer> enchantments = new HashMap<>(); //temporarily useless

    @Getter private final List<String> optionList = new ArrayList<>();

    @Getter private final List<String> attributeList = new ArrayList<>();

    {
        String[] options =
            {"hide_enchants", "hide_attributes", "hide_armor_trim", "hide_destroys",
                    "hide_dye", "hide_placed_on", "hide_potion_effects", "hide_unbreakable"};
        optionList.addAll(Arrays.asList(options));

        String[] attributes =
                {"armor", "armor_toughness", "attack_damage", "attack_knockback", "attack_speed", "flying_speed", "follow_range",
                        "knockback_resistance", "luck", "max_absorption", "max_health", "movement_speed"};
        attributeList.addAll(Arrays.asList(attributes));
    }

    public Item(String itemName) {
        ConfigFile.ConfigName ci = ConfigFile.ConfigName.ITEMS;

        //variable setting
        name = itemName;
        displayName = ci.outString(itemName+".display_name");
        type = Material.valueOf(ci.getString(itemName+".type").toUpperCase());
        if(ci.getStringList(itemName+".lore") != null)
            lore = ci.outStringList(itemName+".lore");
        id = getClass().getSimpleName();
        //element setting, if not provided, use none instead
        if(ci.getString(itemName+".element") != null)
            element = Element.elements.get(ci.getString(itemName+".element"));
        else
            element = Element.elements.get("none");


        //basic setting
        item = new ItemStack(type);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        if(lore != null)
            itemMeta.setLore(lore);

        //enchant setting
        ConfigurationSection enchantmentSection = ci.getConfig().getConfigurationSection(itemName+".enchantments");
        if(enchantmentSection != null)
            setItemEnchantments(enchantmentSection.getKeys(false), ci);

        //options setting
        ConfigurationSection optionSection = ci.getConfig().getConfigurationSection(itemName+".options");
        if(optionSection != null)
            setItemOptions(optionSection.getKeys(false), ci);

        //attributes setting
        ConfigurationSection attributeSection = ci.getConfig().getConfigurationSection(itemName+".attributes");
        if(attributeSection != null)
            setItemAttributes(attributeSection.getKeys(false), ci);

        //key setting
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(itemKey, PersistentDataType.STRING, getId());

        item.setItemMeta(itemMeta);
    }

    private void setItemEnchantments(Set<String> enchantmentSet, ConfigFile.ConfigName ci) {
        for(String s : enchantmentSet) {
            //can use Registry to modify lots of things, but i'm not able to
//            itemMeta.addEnchant(Registry.ENCHANTMENT.get(NamespacedKey.minecraft(s)),
//                    ci.geti(itemName+".enchantments."+s), true);
            itemMeta.addEnchant(Enchantment.getByName(s), ci.getInteger(name+".enchantments."+s), true);
        }
    }

    private void setItemOptions(Set<String> optionSet, ConfigFile.ConfigName ci) {
        for(String s : optionSet) {
            String prefix = name+".options.";
            if(optionList.contains(s) && ci.getBoolean(prefix+s)) {
                itemMeta.addItemFlags(ItemFlag.valueOf(s.toUpperCase()));
            }
        }
    }

    private void setItemAttributes(Set<String> attributeSet, ConfigFile.ConfigName ci) {
        for(String s : attributeSet) {
            String prefix = name+".attributes.";
            if(attributeList.contains(s)) {
                //add
                if(getAttributeType(s, ci) == 0) {
                    itemMeta.addAttributeModifier(Attribute.valueOf("GENERIC_"+s.toUpperCase()),
                            new AttributeModifier(UUID.fromString(s), s, ci.getDouble(prefix+s+".value"), AttributeModifier.Operation.ADD_NUMBER,
                                    EquipmentSlot.valueOf(ci.getString(prefix+s+".slot").toUpperCase())));
                }
                //multiply
                if(getAttributeType(s, ci) == 1) {
                    itemMeta.addAttributeModifier(Attribute.valueOf("GENERIC_"+s.toUpperCase()),
                            new AttributeModifier(UUID.fromString(s), s, ci.getDouble(prefix+s+".value"), AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                                    EquipmentSlot.valueOf(ci.getString(prefix+s+".slot").toUpperCase())));
                }
            }
        }
    }

    //get the type of the attribute and return an int
    //add - 0 / multiply - 1
    private int getAttributeType(String attributeName, ConfigFile.ConfigName ci) {
        if(ci.getString(name+".attributes."+attributeName+".type").equalsIgnoreCase("add")) return 0;
        if(ci.getString(name+".attributes."+attributeName+".type").equalsIgnoreCase("multiply")) return 1;

        //-1 represents error
        return -1;
    }
}
