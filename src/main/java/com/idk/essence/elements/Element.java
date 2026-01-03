package com.idk.essence.elements;

import com.idk.essence.Essence;
import com.idk.essence.utils.configs.ConfigFile;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Element {

    public static final Map<String, Element> elements = new LinkedHashMap<>();

    @Getter private static final NamespacedKey elementKey = new NamespacedKey(plugin, "element-key");

    @Getter private final String name;

    @Getter private final String displayName;

    @Getter private final ItemStack symbolItem;

    @Getter private final ItemMeta itemMeta;

    @Getter private final int slot;

    @Getter private List<String> description;

    public void setDescription(List<String> description) {
        this.description = description;
        itemMeta.setLore(this.description);
        symbolItem.setItemMeta(itemMeta);
    }

    @Getter private boolean glowing = false;

    @Getter private final String id;

    @Getter private final Map<Element, Double> suppressMap;

    @Getter private final Map<Element, Double> suppressedMap;

    public Element(String elementName) {
        //config elements
        ConfigFile.ConfigName ce = ConfigFile.ConfigName.ELEMENTS; //config elements
        name = elementName;
        displayName = ce.outString(elementName+".display-name");
        symbolItem = new ItemStack(Material.valueOf(ce.getString(elementName+".symbol-item").toUpperCase()));
        slot = ce.getInteger(elementName+".slot");
        if(ce.getStringList(elementName+".description") != null)
            description = ce.outStringList(elementName+".description");
        id = getClass().getSimpleName();


        itemMeta = symbolItem.getItemMeta();
        itemMeta.setDisplayName(displayName);
        if(description != null)
            itemMeta.setLore(description);
        if(ce.getBoolean(elementName+".glowing")) { //if not exist?
            glowing = true;
            itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(elementKey, PersistentDataType.STRING, getId());

        symbolItem.setItemMeta(itemMeta);

        suppressMap = new HashMap<>();
        suppressedMap = new HashMap<>();
    }
}
