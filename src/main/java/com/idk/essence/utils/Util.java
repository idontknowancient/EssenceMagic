package com.idk.essence.utils;

import com.idk.essence.Essence;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final Essence plugin = Essence.getPlugin();
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

    public static void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    // used to handle "\n" in a line
    // e.g. "[a]\n[b]\n[c]\n[d]\n" should be split into four parts
    public static List<String> splitLore(List<String> old) {
        List<String> new_ = new ArrayList<>();
        for(String string : old) {
            if(string.contains("\n")) {
                String[] multiple = string.split("\n");
                new_.addAll(List.of(multiple));
            } else
                new_.add(string);
        }

        return new_;
    }

    public static Color stringToColor(String string) {
        return switch (string.toUpperCase()) {
            case "AQUA" -> Color.AQUA;
            case "BLACK" -> Color.BLACK;
            case "BLUE" -> Color.BLUE;
            case "FUCHSIA" -> Color.FUCHSIA;
            case "GRAY" -> Color.GRAY;
            case "GREEN" -> Color.GREEN;
            case "LIME" -> Color.LIME;
            case "MAROON" -> Color.MAROON;
            case "NAVY" -> Color.NAVY;
            case "OLIVE" -> Color.OLIVE;
            case "ORANGE" -> Color.ORANGE;
            case "PURPLE" -> Color.PURPLE;
            case "RED" -> Color.RED;
            case "SILVER" -> Color.SILVER;
            case "TEAL" -> Color.TEAL;
            case "WHITE" -> Color.WHITE;
            case "YELLOW" -> Color.YELLOW;
            default -> Color.WHITE; // fallback
        };
    }

    /*
     if looking downward, z-x plane is "x-y" plane we used to (z is "x" and x is "y")
     start from south (+z, yaw = 0), and keep rotating counterclockwise (yaw keeps declining), so we add "-"
     after north (-z, yaw = -180), yaw drops from 180 to 0, so we use 360 - yaw to get the accurate math angle
     */
    public static double yawToMathDegree(double yaw) {
        return 0 < yaw && yaw <= 180 ? 360 - yaw : -yaw;
    }

    public static void consoleOuts(String s) {
        plugin.getLogger().log(Level.INFO, s);
    }

    public static double stringExpressionConverter(String s) {
        return 0;
    }
}
