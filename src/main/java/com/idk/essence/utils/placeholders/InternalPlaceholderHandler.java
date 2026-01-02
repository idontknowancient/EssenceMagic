package com.idk.essence.utils.placeholders;

import com.idk.essence.items.Item;
import com.idk.essence.items.SystemItem;
import com.idk.essence.magics.Magic;
import com.idk.essence.players.ManaHandler;
import com.idk.essence.players.PlayerData;
import com.idk.essence.skills.Skill;
import com.idk.essence.items.wands.Wand;
import com.idk.essence.items.wands.WandHandler;
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
        else if(info instanceof SystemItem s)
            string = handleSystemItem(string, s);
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

    private static String handleSystemItem(String string, SystemItem info) {
        if(string.contains(InternalPlaceholder.ITEM_NAME.name))
            string = string.replaceAll(InternalPlaceholder.ITEM_NAME.name, info.getName());
        if(string.contains(InternalPlaceholder.ITEM_DISPLAY_NAME.name))
            string = string.replaceAll(InternalPlaceholder.ITEM_DISPLAY_NAME.name, info.getDisplayName());
        if(string.contains(InternalPlaceholder.ITEM_TYPE.name))
            string = string.replaceAll(InternalPlaceholder.ITEM_TYPE.name, info.getMaterial().name());
        return string;
    }

    private static String handleSkill(String string, Skill info) {
        if(string.contains(InternalPlaceholder.SKILL_DISPLAY_NAME.name))
            string = string.replaceAll(InternalPlaceholder.SKILL_DISPLAY_NAME.name, String.valueOf(info.getDisplayName()));
        return string;
    }

    // use when initializing
    private static String handleWand(String string, Wand info) {
        String[] containerMagic = info.getDefaultMagic().toString().split(";");

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
            StringBuilder loreMagic = new StringBuilder();
            for(int i = 0; i < info.getSlot(); i++) {
                // using "\n" in lore, and ";" in container
                if(Magic.magics.containsKey(containerMagic[i]))
                    loreMagic.append(Magic.magics.get(containerMagic[i]).getDisplayName()).append("\n");
                else
                    loreMagic.append(Wand.getEmptyString()).append("\n");
            }
            string = string.replaceAll(InternalPlaceholder.WAND_MAGIC.name, loreMagic.toString());
        }
        if(string.contains(InternalPlaceholder.WAND_MAGIC_USING.name)) {
            // include indicating number`
            int usingNum = 0;
            try {
                usingNum = Integer.parseInt(containerMagic[containerMagic.length - 1]);
            } catch (NumberFormatException ignored) {

            }
            String using = containerMagic[usingNum];
            if(Magic.magics.containsKey(using))
                string = string.replaceAll(InternalPlaceholder.WAND_MAGIC_USING.name,
                        Magic.magics.get(using).getDisplayName());
            else
                string = string.replaceAll(InternalPlaceholder.WAND_MAGIC_USING.name, "");
        }
        return string;
    }

    // use when updating
    private static String handleWand(String string, ItemStack info) {
        // checked before
        assert info.getItemMeta() != null;
        PersistentDataContainer container = info.getItemMeta().getPersistentDataContainer();
        Integer Slot = container.get(Wand.getSlotKey(), PersistentDataType.INTEGER);
        int slot = Slot != null ? Slot : 1;
        String ContainerMagic = container.get(Wand.getWandMagicKey(), PersistentDataType.STRING);
        String[] containerMagic = ContainerMagic != null ? ContainerMagic.split(";") : new String[0];

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
            string = string.replaceAll(InternalPlaceholder.WAND_SLOT.name, slot+"");
        }
        if(string.contains(InternalPlaceholder.WAND_MAGIC.name)) {
            StringBuilder loreMagic = new StringBuilder();
            for(int i = 0; i < slot; i++) {
                // using "\n" in lore, and ";" in container
                if(Magic.magics.containsKey(containerMagic[i]))
                    loreMagic.append(Magic.magics.get(containerMagic[i]).getDisplayName()).append("\n");
                else
                    loreMagic.append(Wand.getEmptyString()).append("\n");
            }
            string = string.replaceAll(InternalPlaceholder.WAND_MAGIC.name, loreMagic.toString());
        }
        if(string.contains(InternalPlaceholder.WAND_MAGIC_USING.name)) {
            // include indicating number`
            int index = 0;
            try {
                index = Integer.parseInt(containerMagic[containerMagic.length - 1]);
            } catch (NumberFormatException ignored) {
            }
            String using = containerMagic[index];
            if(Magic.magics.containsKey(using))
                string = string.replaceAll(InternalPlaceholder.WAND_MAGIC_USING.name,
                        Magic.magics.get(using).getDisplayName());
            else
                string = string.replaceAll(InternalPlaceholder.WAND_MAGIC_USING.name, "");
        }
        if(string.contains(InternalPlaceholder.WAND_MANA_INJECTION.name))
            string = string.replaceAll(InternalPlaceholder.WAND_MANA_INJECTION.name, WandHandler.getInjection(info)+"");
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
