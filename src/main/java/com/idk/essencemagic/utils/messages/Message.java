package com.idk.essencemagic.utils.messages;

import com.idk.essencemagic.utils.configs.ConfigFile;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Message {

    static void send(CommandSender sender, String string) {
        sender.sendMessage(translatePlaceholder(sender, getPrefix() + string));
    }

    static String getPrefix() {
        return ConfigFile.ConfigName.MESSAGES.outString("prefix");
    }

    static String translatePlaceholder(CommandSender sender, String string) {
        if(string != null) {
            if (sender instanceof Player p)
                string = PlaceholderAPI.setPlaceholders(p, string);
            return colorize(string);
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
