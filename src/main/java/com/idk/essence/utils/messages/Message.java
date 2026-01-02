package com.idk.essence.utils.messages;

import com.idk.essence.utils.configs.ConfigFile;
import com.idk.essence.utils.placeholders.InternalPlaceholderHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Message {

    static void send(CommandSender sender, String path) {
        sender.sendMessage(colorize(getPrefix() + path));
    }

    static void send(CommandSender sender, String path, Object info) {
        sender.sendMessage(colorize(
                translatePlaceholder(getPrefix() + path, info)));
    }

    static String out(String path) {
        return ConfigFile.ConfigName.MESSAGES.outString(path);
    }

    static String out(String path, Object info) {
        return ConfigFile.ConfigName.MESSAGES.outString(path, info);
    }

    static String getPrefix() {
        return SystemMessage.PREFIX.out();
    }

    static String translatePlaceholder(String string, Object info) {
        if(string != null) {
            return InternalPlaceholderHandler.translatePlaceholders(string, info);
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
