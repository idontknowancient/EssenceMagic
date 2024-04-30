package com.idk.essencemagic.utils.messages.placeholders;

import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.player.ManaHandler;
import com.idk.essencemagic.player.PlayerData;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class InternalPlaceholderHandler {

    public static String translatePlaceholders(String string, Object info) {
        if(info instanceof String s)
            string = handleString(string, s);
        if(info instanceof Item i)
            string = handleItem(string, i);
        if(info instanceof PlayerData d)
            string = handlePlayerData(string, d);
        if(info instanceof Double d)
            string = handleDouble(string, d);
        if(info instanceof Player p)
            string = handlePlaceholderAPI(string, p);
        return string;
    }

    private static String handleString(String string, String info) {
        if(string.contains(InternalPlaceholder.PLAYER_NAME.name))
            string = string.replaceAll(InternalPlaceholder.PLAYER_NAME.name, info);
        if(string.contains(InternalPlaceholder.USAGE.name))
            string = string.replaceAll(InternalPlaceholder.USAGE.name, info);
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

    private static String handlePlayerData(String string, PlayerData info) {
        if(string.contains(InternalPlaceholder.MANA_LEVEL.name))
            string = string.replaceAll(InternalPlaceholder.MANA_LEVEL.name, String.valueOf(info.getManaLevel()));
        if(string.contains(InternalPlaceholder.MANA.name))
            string = string.replaceAll(InternalPlaceholder.MANA.name, String.valueOf(info.getMana()));
        if(string.contains(InternalPlaceholder.DEFAULT_MANA.name))
            string = string.replaceAll(InternalPlaceholder.DEFAULT_MANA.name, String.valueOf(ManaHandler.getDefaultMana()));
        if(string.contains(InternalPlaceholder.MAX_MANA.name))
            string = string.replaceAll(InternalPlaceholder.MAX_MANA.name, String.valueOf(info.getMaxMana()));
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
