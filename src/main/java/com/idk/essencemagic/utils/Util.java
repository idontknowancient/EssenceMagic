package com.idk.essencemagic.utils;

import com.idk.essencemagic.EssenceMagic;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();
    private static final Pattern hexCode = Pattern.compile("#[a-fA-F0-9]{6}"); //regex

    public static String colorize(String string) { //hex support
        Matcher match = hexCode.matcher(string); //find regex in a string
        while(match.find()) {
            String hexStr = string.substring(match.start(), match.end());
            string = string.replace(hexStr, ChatColor.of(hexStr)+"");
            match = hexCode.matcher(string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void consoleOuts(String s) {
        plugin.getLogger().log(Level.INFO, s);
    }

    public static double stringExpressionConverter(String s) {
        return 0;
    }
}
