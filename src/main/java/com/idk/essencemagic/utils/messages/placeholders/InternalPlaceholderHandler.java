package com.idk.essencemagic.utils.messages.placeholders;

import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.player.ManaHandler;
import com.idk.essencemagic.player.PlayerData;
import com.idk.essencemagic.skills.Skill;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.wands.Wand;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class InternalPlaceholderHandler {

    public static String translatePlaceholders(String string, Object info) {
        if(info instanceof String s)
            string = handleString(string, s);
        else if(info instanceof ItemStack i)
            string = handleItemStack(string, i);
        else if(info instanceof Item i)
            string = handleItem(string, i);
        else if(info instanceof Skill s)
            string = handleSkill(string ,s);
        else if(info instanceof Wand w)
            string = handleWand(string, w);
        else if(info instanceof PlayerData d)
            string = handlePlayerData(string, d);
        else if(info instanceof Double d)
            string = handleDouble(string, d);
        else if(info instanceof Player p)
            string = handlePlaceholderAPI(string, p);
        return string;
    }

    private static String handleString(String string, String info) {
        if(string.contains(InternalPlaceholder.PLAYER.name))
            string = string.replaceAll(InternalPlaceholder.PLAYER.name, info);
        if(string.contains(InternalPlaceholder.USAGE.name))
            string = string.replaceAll(InternalPlaceholder.USAGE.name, info);
        return string;
    }

    private static String handleItemStack(String string, ItemStack info) {
        ItemMeta meta = info.getItemMeta();
        if(meta == null) return string;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(container.has(Wand.getWandKey()))
            string = handleWand(string, info);
        if(container.has(Item.getItemKey())) {

        }
        return string;
    }

    private static String handleItem(String string, Item info) {
        if(string.contains(InternalPlaceholder.ITEM_NAME.name))
            string = string.replaceAll(InternalPlaceholder.ITEM_NAME.name, info.getName());
        if(string.contains(InternalPlaceholder.ITEM_DISPLAY_NAME.name))
            string = string.replaceAll(InternalPlaceholder.ITEM_DISPLAY_NAME.name, info.getDisplayName());
        if(string.contains(InternalPlaceholder.ITEM_TYPE.name))
            string = string.replaceAll(InternalPlaceholder.ITEM_TYPE.name, info.getType().name());
        if(string.contains(InternalPlaceholder.ITEM_ID.name))
            string = string.replaceAll(InternalPlaceholder.ITEM_ID.name, info.getId());
        if(string.contains(InternalPlaceholder.ITEM_ELEMENT.name))
            string = string.replaceAll(InternalPlaceholder.ITEM_ELEMENT.name, info.getElement().getDisplayName());
        return string;
    }

    private static String handleSkill(String string, Skill info) {
        if(string.contains(InternalPlaceholder.SKILL_DISPLAY_NAME.name))
            string = string.replaceAll(InternalPlaceholder.SKILL_DISPLAY_NAME.name, String.valueOf(info.getDisplayName()));
        return string;
    }

    // use when initializing
    private static String handleWand(String string, Wand info) {
        if(string.contains(InternalPlaceholder.WAND_NAME.name))
            string = string.replaceAll(InternalPlaceholder.WAND_NAME.name, info.getName());
        if(string.contains(InternalPlaceholder.WAND_DISPLAY_NAME.name))
            string = string.replaceAll(InternalPlaceholder.WAND_DISPLAY_NAME.name, String.valueOf(info.getDisplayName()));
        if(string.contains(InternalPlaceholder.WAND_MANA.name))
            // rounding two digits
            string = string.replaceAll(InternalPlaceholder.WAND_MANA.name, String.valueOf(Math.round(info.getDefaultMana() * 100.00) / 100.00));
        if(string.contains(InternalPlaceholder.WAND_SLOT.name))
            string = string.replaceAll(InternalPlaceholder.WAND_SLOT.name, info.getSlot()+"");
        if(string.contains(InternalPlaceholder.WAND_MAGIC.name)) {
            StringBuilder magic = new StringBuilder();
            String empty = "";
            ConfigFile.ConfigName cm = ConfigFile.ConfigName.MESSAGES;
            if(cm.isString("wand-magic-empty"))
                empty = cm.outString("wand-magic-empty");
            else
                empty = "&7[empty]";
            for(int i = 0; i < info.getSlot(); i++) {
                magic.append(empty).append("\n");
            }
            string = string.replaceAll(InternalPlaceholder.WAND_MAGIC.name, magic.substring(0, magic.length()));
        }
        return string;
    }

    // use when updating
    private static String handleWand(String string, ItemStack info) {
        // checked before
        assert info.getItemMeta() != null;
        PersistentDataContainer container = info.getItemMeta().getPersistentDataContainer();

        if(string.contains(InternalPlaceholder.WAND_NAME.name)) {
            String Name = container.get(Wand.getWandKey(), PersistentDataType.STRING);
            String name = Name != null ? Name : "";
            string = string.replaceAll(InternalPlaceholder.WAND_NAME.name, name);
        }
        if(string.contains(InternalPlaceholder.WAND_DISPLAY_NAME.name)) {
            string = string.replaceAll(InternalPlaceholder.WAND_DISPLAY_NAME.name, info.getItemMeta().getDisplayName());
        }
        if(string.contains(InternalPlaceholder.WAND_MANA.name)) {
            Double Mana = container.get(Wand.getManaKey(), PersistentDataType.DOUBLE);
            // assign default value if the key doesn't exist
            double mana = Mana != null ? Mana : 0.0;
            string = string.replaceAll(InternalPlaceholder.WAND_MANA.name, mana+"");
        }
        if(string.contains(InternalPlaceholder.WAND_SLOT.name)) {
            Integer Slot = container.get(Wand.getSlotKey(), PersistentDataType.INTEGER);
            double slot = Slot != null ? Slot : 1;
            string = string.replaceAll(InternalPlaceholder.WAND_SLOT.name, slot+"");
        }
        if(string.contains(InternalPlaceholder.WAND_MAGIC.name)) {
            Integer Slot = container.get(Wand.getSlotKey(), PersistentDataType.INTEGER);
            double slot = Slot != null ? Slot : 1;
            StringBuilder magic = new StringBuilder();
            String empty = "";
            ConfigFile.ConfigName cm = ConfigFile.ConfigName.MESSAGES;
            if(cm.isString("wand-magic-empty"))
                empty = cm.outString("wand-magic-empty");
            else
                empty = "&7[empty]";
            for(int i = 0; i < slot; i++) {
                magic.append(empty).append("\n");
            }
            string = string.replaceAll(InternalPlaceholder.WAND_MAGIC.name, magic.substring(0, magic.length()));
        }
        return string;
    }

    private static String handlePlayerData(String string, PlayerData info) {
        if(string.contains(InternalPlaceholder.PLAYER.name))
            string = string.replaceAll(InternalPlaceholder.PLAYER.name, String.valueOf(info.getPlayerName()));
        if(string.contains(InternalPlaceholder.MANA_LEVEL.name))
            string = string.replaceAll(InternalPlaceholder.MANA_LEVEL.name, String.valueOf(info.getManaLevel()));
        if(string.contains(InternalPlaceholder.MANA.name))
            // rounding two digits
            string = string.replaceAll(InternalPlaceholder.MANA.name, String.valueOf(Math.round(info.getMana() * 100.00) / 100.00));
        if(string.contains(InternalPlaceholder.DEFAULT_MANA.name))
            string = string.replaceAll(InternalPlaceholder.DEFAULT_MANA.name, String.valueOf(ManaHandler.getDefaultMana()));
        if(string.contains(InternalPlaceholder.MAX_MANA.name))
            // rounding two digits
            string = string.replaceAll(InternalPlaceholder.MAX_MANA.name, String.valueOf(Math.round(info.getMaxMana() * 100.00) / 100.00));
        if(string.contains(InternalPlaceholder.MANA_RECOVERY_SPEED.name))
            string = string.replaceAll(InternalPlaceholder.MANA_RECOVERY_SPEED.name, String.valueOf(info.getManaRecoverySpeed()));
        return string;
    }

    private static String handleDouble(String string, double info) {
        if(string.contains(InternalPlaceholder.DEFAULT_MANA.name))
            string = string.replaceAll(InternalPlaceholder.DEFAULT_MANA.name, String.valueOf(ManaHandler.getDefaultMana()));
        return string;
    }

    private static String handlePlaceholderAPI(String string, Player player) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }
}
