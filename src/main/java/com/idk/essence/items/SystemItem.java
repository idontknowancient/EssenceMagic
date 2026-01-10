package com.idk.essence.items;

import com.idk.essence.Essence;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.placeholders.PlaceholderManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@Getter
public abstract class SystemItem {

    private static final Essence plugin = Essence.getPlugin();

    public static final Map<String, SystemItem> systemItems = new LinkedHashMap<>();

    // signify overall system item
    @Getter private static final NamespacedKey systemItemKey = new NamespacedKey(plugin, "system-item-key");

    private final String name;

    private final boolean usable;

    private final Component displayName;

    private final Material material;

    private List<String> lore = new ArrayList<>();

    private final boolean glowing;

    private final boolean placeable;

    private final ItemStack itemStack;

    protected SystemItem(String itemName) {
        ConfigManager.ConfigDefaultFile cs = ConfigManager.ConfigDefaultFile.SYSTEM_ITEMS;
        name = itemName;

        // set usable (default to true)
        usable = cs.getBoolean(name + ".usable", true);

        // set display name (default to "")
        displayName = cs.outString(name + ".name", "");

        // set material (default to apple)
        String type = cs.getString(name + ".type", "APPLE").toUpperCase();
        // if the material is null then return apple
        material = Optional.ofNullable(Material.getMaterial(type)).orElse(Material.APPLE);

        // set item lore (default to empty)
        // set internal placeholders & cs.getStringList is safe(will not return null)
        for(String string : cs.getStringList(name + ".lore")) {
            lore.add(PlaceholderManager.translate(string, this));
        }
        lore = Util.splitLore(lore);

        // set glowing (default to true)
        glowing = cs.getBoolean(name + ".glowing", true);

        // set placeable (default to true)
        placeable = cs.getBoolean(name + ".placeable", true);

        itemStack = new ItemStack(material);

        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.getPersistentDataContainer().set(systemItemKey, PersistentDataType.STRING, name);
        meta.displayName(displayName);
        meta.setLore(lore);
        if(glowing) {
            meta.addEnchant(Enchantment.UNBREAKING, 0, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(meta);
    }

    public abstract void onItemRightClick(PlayerInteractEvent event);

}
