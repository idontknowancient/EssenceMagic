package com.idk.essencemagic.utils.messages;

import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.placeholders.InternalPlaceholderHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Message {

    static void send(CommandSender sender, String path) {
        sender.sendMessage(colorize(getPrefix() + path));
    }

    static void send(CommandSender sender, String path, Object info) {
        sender.sendMessage(colorize(
                translatePlaceholder(sender, getPrefix() + path, info)));
    }

    static String out(String path) {
        return ConfigFile.ConfigName.MESSAGES.outString(path);
    }

    static String out(String path, Player player) {
        return ConfigFile.ConfigName.MESSAGES.outString(player, path, player);
    }

    static String getPrefix() {
        return SystemMessage.PREFIX.out();
    }

    static String translatePlaceholder(CommandSender sender, String string, Object info) {
        if(string != null) {
            return InternalPlaceholderHandler.translatePlaceholders(sender, string, info);
        }
        return null;
    }

    static String colorize(String string) {
        Pattern hexCode = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher match = hexCode.matcher(string); //find regex in a string
        while(match.find()) {
            String hexStr = string.substring(match.start(), match.end());
            string = string.replace(hexStr, ChatColor.of(hexStr)+"");
            match = hexCode.matcher(string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
