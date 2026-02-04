package com.idk.essence.utils;

import com.idk.essence.Essence;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Util {

    public static class Tool {

        /**
         * If looking downward, z-x plane is "x-y" plane we used to (z is "x" and x is "y").
         * Start from south (+z, yaw = 0), and keep rotating counterclockwise (yaw keeps declining), so we add "-"
         * after north (-z, yaw = -180), yaw drops from 180 to 0, so we use 360 - yaw to get the accurate math angle.
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

        /**
         * Capitalize the first char.
         */
        public static String capitalize(String input) {
            if(input == null || input.isEmpty()) {
                return input;
            }
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }

        /**
         * Get an online player, or an offline player having played before, otherwise null.
         */
        @Nullable
        public static OfflinePlayer getPlayerByName(String playerName) {
            if(Bukkit.getPlayerExact(playerName) != null)
                return Bukkit.getPlayerExact(playerName);
            else
                return Arrays.stream(Bukkit.getOfflinePlayers())
                    .filter(p -> playerName.equalsIgnoreCase(p.getName()))
                    .findFirst().orElse(null);
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
                case "YELLOW" -> Color.YELLOW;
                default -> Color.WHITE;
            };
        }

        /**
         * Format: [message]
         */
        public static void info(String message) {
            Essence.getPlugin().getComponentLogger().info(message);
        }

        /**
         * Format: [message: object]
         */
        public static void info(String message, Object info) {
            Essence.getPlugin().getComponentLogger().info("{}: {}", message, info);
        }

        /**
         * Format: [message: object]
         */
        public static void info(Component message, Object info) {
            Essence.getPlugin().getComponentLogger().info("{}: {}", message, info);
        }
    }
}
