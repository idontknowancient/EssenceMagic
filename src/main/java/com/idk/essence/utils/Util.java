package com.idk.essence.utils;

import com.idk.essence.Essence;
import com.idk.essence.utils.placeholders.PlaceholderManager;
import com.jeff_media.customblockdata.CustomBlockData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})", Pattern.CASE_INSENSITIVE);

    /**
     * Create map with respect to legacy code and minimessage tags.
     */
    private static final Map<String, String> LEGACY_MAP = new HashMap<>();

    static {
        LEGACY_MAP.put("&0", "<black>");     LEGACY_MAP.put("&1", "<dark_blue>");
        LEGACY_MAP.put("&2", "<dark_green>"); LEGACY_MAP.put("&3", "<dark_aqua>");
        LEGACY_MAP.put("&4", "<dark_red>");   LEGACY_MAP.put("&5", "<dark_purple>");
        LEGACY_MAP.put("&6", "<gold>");        LEGACY_MAP.put("&7", "<gray>");
        LEGACY_MAP.put("&8", "<dark_gray>");  LEGACY_MAP.put("&9", "<blue>");
        LEGACY_MAP.put("&a", "<green>");       LEGACY_MAP.put("&b", "<aqua>");
        LEGACY_MAP.put("&c", "<red>");         LEGACY_MAP.put("&d", "<light_purple>");
        LEGACY_MAP.put("&e", "<yellow>");      LEGACY_MAP.put("&f", "<white>");
        LEGACY_MAP.put("&l", "<bold>");        LEGACY_MAP.put("&m", "<strikethrough>");
        LEGACY_MAP.put("&n", "<underlined>");  LEGACY_MAP.put("&o", "<italic>");
        LEGACY_MAP.put("&k", "<obfuscated>");  LEGACY_MAP.put("&r", "<reset>");
    }

    public static class MathTool {

        /*
             if looking downward, z-x plane is "x-y" plane we used to (z is "x" and x is "y")
             start from south (+z, yaw = 0), and keep rotating counterclockwise (yaw keeps declining), so we add "-"
             after north (-z, yaw = -180), yaw drops from 180 to 0, so we use 360 - yaw to get the accurate math angle
             */
        public static double yawToMathDegree(double yaw) {
            return 0 < yaw && yaw <= 180 ? 360 - yaw : -yaw;
        }

        public static double round(double value, int places) {
            double scale = Math.pow(10, places);
            return Math.round(value * scale) / scale;
        }

        /**
         * Read offset: \x:? \y:? \z:? vector.
         */
        public static Vector getVectorFromSection(ConfigurationSection vectorSection) {
            if(vectorSection == null)
                return new Vector(0, 0, 0);
            double x = vectorSection.getDouble("x", 0);
            double y = vectorSection.getDouble("y", 0);
            double z = vectorSection.getDouble("z", 0);

            return new Vector(x, y, z);
        }

        /**
         * Read [x, y, z] vector.
         */
        public static Vector getVectorFromList(List<Double> doubleList) {
            if(doubleList == null || doubleList.size() < 3)
                return new Vector(0, 0, 0);
            return new Vector(doubleList.getFirst(), doubleList.get(1), doubleList.get(2));
        }
    }

    public static class System {

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

        /**
         * Convert placeholders, legacy color code (&), hex color code, and minimessage to component.
         */
        @NotNull public static Component parseMessage(String input, Object info) {
            return parseMessage(PlaceholderManager.translate(input, info));
        }

        /**
         * Convert legacy color code (&), hex color code, and minimessage to component.
         */
        @NotNull public static Component parseMessage(String input) {
            if(input == null) return Component.empty();

            // Handle hex color code (e.g. #FFFFFF -> <#FFFFFF>)
            Matcher matcher = hexPattern.matcher(input);
            StringBuilder builder = new StringBuilder();
            while(matcher.find()) {
                matcher.appendReplacement(builder, "<#" + matcher.group(1) + ">");
            }
            matcher.appendTail(builder);
            input = builder.toString();

            // Handle legacy color code (e.g. &6 -> <gold>)
            for(Map.Entry<String, String> entry : LEGACY_MAP.entrySet()) {
                input = input.replace(entry.getKey(), entry.getValue());
                // Handle uppercase (&A)
                input = input.replace(entry.getKey().toUpperCase(), entry.getValue());
            }

            return MiniMessage.miniMessage().deserialize(input).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        }

        /**
         * Format: <message>
         */
        public static void consoleLog(String message) {
            Essence.getPlugin().getComponentLogger().info(message);
        }

        /**
         * Format: <message>: <object>
         */
        public static void consoleLog(String message, Object info) {
            Essence.getPlugin().getComponentLogger().info("{}: {}", message, info);
        }
    }
}
