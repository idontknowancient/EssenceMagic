package com.idk.essence.utils;

import com.idk.essence.players.PlayerDataManager;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.placeholders.PlaceholderManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConditionManager {

    public enum Operator {
        GREATER_EQUAL(">=", (a, b) -> compare(a, b) >= 0),
        LESS_EQUAL("<=", (a, b) -> compare(a, b) <= 0),
        EQUAL("==", (a, b) -> compare(a, b) == 0),
        NOT_EQUAL("!=", (a, b) -> compare(a, b) != 0),
        GREATER(">", (a, b) -> compare(a, b) > 0),
        LESS("<", (a, b) -> compare(a, b) < 0);

        private final String symbol;
        private final BiPredicate<Object, Object> function;

        Operator(String symbol, BiPredicate<Object, Object> function) {
            this.symbol = symbol;
            this.function = function;
        }

        private static int compare(Object a, Object b) {
            if(a == null || b == null) return 0;

            // Try to handle placeholder number
            Double d1 = tryParseDouble(a);
            Double d2 = tryParseDouble(b);
            if(d1 != null && d2 != null) {
                return Double.compare(d1, d2);
            }

            // Use string to compare if numbers not available
            return a.toString().compareTo(b.toString());
        }

        private static Double tryParseDouble(Object object) {
            if(object instanceof Number n) return n.doubleValue();
            try {
                return Double.parseDouble(object.toString());
            } catch(NumberFormatException e) {
                return null;
            }
        }

        public boolean apply(Object left, Object right) {
            return function.test(left, right);
        }

        // Get all operators and sort (longer -> former) to generate regex
        public static String getRegexPattern() {
            return Arrays.stream(values())
                    .map(op -> Pattern.quote(op.symbol))
                    .sorted((a, b) -> Integer.compare(b.length(), a.length()))
                    .collect(Collectors.joining("|"));
        }
    }

    /**
     * (.+?) for first part e.g. %mana%
     * (pattern) for operators e.g. >=
     * (.+) for last part e.g. 20
     * \\s* for any space
     */
    private static final Pattern PATTERN = Pattern.compile(
            "(.+?)\\s*(" + Operator.getRegexPattern() + ")\\s*(.+)"
    );

    /**
     * Check if the caster satisfies the requirement. Automatically handle placeholders.
     * @param requirement string
     * @param player caster
     */
    public static boolean checkRequirement(String requirement, Player player) {
        Matcher matcher = PATTERN.matcher(requirement);
        if(matcher.find()) {
            // e.g. %player_mana%
            String left = matcher.group(1).trim();
            left = PlaceholderManager.translate(left, PlayerDataManager.get(player));
            // Handle if the placeholder is not a custom one
            if(left.contains("%"))
                left = PlaceholderAPI.setPlaceholders(player, left);

            // e.g. >=
            String symbol = matcher.group(2);

            // e.g. 20
            String right = matcher.group(3).trim();
            right = PlaceholderManager.translate(right, PlayerDataManager.get(player));
            if(left.contains("%"))
                left = PlaceholderAPI.setPlaceholders(player, left);

            // Find corresponding operator
            Operator operator = Arrays.stream(Operator.values())
                    .filter(op -> op.symbol.equals(symbol))
                    .findFirst().orElse(Operator.EQUAL);

            return operator.apply(left, right);
        }
        return true;
    }

    public static void applyCost(String cost, Player player, boolean success) {
        int colonIndex = cost.indexOf(":");
        String type = cost.substring(0, colonIndex).trim();
        double amount = 0;
        try {
            amount = Double.parseDouble(cost.substring(colonIndex + 1).trim());
        } catch (NumberFormatException ignored) {
        }

        if(type.equalsIgnoreCase("mana"))
            // Consume mana depending on the settings
            if(success || ConfigManager.DefaultFile.MANA.getBoolean("consume-while-skill-fail"))
                PlayerDataManager.get(player).deductMana(amount);
    }
}
