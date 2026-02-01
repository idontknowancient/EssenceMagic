package com.idk.essence.magics;

import com.idk.essence.magics.domains.IntensityDomain;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RoalkomEngine {

    /**
     * In Roalkom's index
     */
    public static class AvailableRange {
        @Getter private final double lowerLimit;
        @Getter private final double upperLimit;
        private final boolean includeMin;
        private final boolean includeMax;

        public AvailableRange(double lowerLimit, double upperLimit, boolean includeMin, boolean includeMax) {
            this.lowerLimit = lowerLimit;
            this.upperLimit = Math.max(upperLimit, lowerLimit);
            this.includeMin = includeMin;
            this.includeMax = includeMax;
        }

        public boolean contains(double value) {
            return (includeMax ? value <= upperLimit : value < upperLimit) &&
                    (includeMin ? value >= lowerLimit : value > lowerLimit);
        }

        /**
         * Check if this range intersects the other range.
         */
        public boolean intersects(AvailableRange range) {
            return
            ((this.includeMax && range.includeMin) ? this.upperLimit >= range.lowerLimit : this.upperLimit > range.lowerLimit) &&
            ((this.includeMin && range.includeMax) ? this.lowerLimit <= range.upperLimit : this.lowerLimit < range.upperLimit);
        }

        public static AvailableRange getDefault() {
            return new AvailableRange(0.0, 0.0, true, false);
        }

        @Override
        public String toString() {
            return (includeMin ? "&f[" : "&f(") + lowerLimit + ", " +
                    (upperLimit == Double.MAX_VALUE ? "inf" : upperLimit) + (includeMax ? "]" : ")");
        }
    }

    private static final double LOG_BASE = ConfigManager.DefaultFile.CONFIG.getDouble("roalkom-log-base", 2.7);
    private static final double LN_BASE = Math.log(LOG_BASE);

    /**
     * Logarithm Roalkom Index
     * @param conversionRatio depending on the player and environment
     * @param infusedMana depending on the player
     */
    public static double calculateRoalkomIndex(double conversionRatio, double infusedMana) {
        double outputMana = conversionRatio * infusedMana;
        if(outputMana <= 0) return 0.0;
        // log_a(b) = ln(b) / ln(a)
        return Math.log(outputMana) / LN_BASE;
    }

    /**
     * @param roalkomIndex >= 0
     * @return e.g. F7 if index is in [0.7, 0.8). "" if index < 0.
     */
    @NotNull
    public static String format(double roalkomIndex) {
        if(roalkomIndex < 0) return "";

        StringBuilder format = new StringBuilder();
        for(IntensityDomain domain : IntensityDomain.getDomains()) {
            if(domain.getAvailableRange().contains(roalkomIndex)) {
                format.append(domain.getInternalName());
                break;
            }
        }

        // e.g. get 7 in index 3.72
        int level = (int) (Math.floor(roalkomIndex * 10) % 10);

        return format.append(level).toString();
    }

    /**
     * Parse a string in domain section to available range.
     * E.g. [0, 1) -> lower: 0, upper: 1, min: true, max: false.
     */
    @NotNull
    public static AvailableRange parseDomain(@Nullable String input) {
        if(input == null || input.isEmpty()) return AvailableRange.getDefault();
        boolean includeMin = input.startsWith("[");
        boolean includeMax = input.endsWith("]");

        String values = input.substring(1, input.length() - 1);
        String[] parts = values.split(",");

        double min = 0;
        double max = 0;
        try {
            min = Double.parseDouble(parts[0].trim());
            max = parts[1].trim().equalsIgnoreCase("inf")
                    ? Double.MAX_VALUE
                    : Double.parseDouble(parts[1].trim());
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ignored) {

        }

        return new AvailableRange(min, max, includeMin, includeMax);
    }

    /**
     * Parse range section to available range.
     * E.g. lower-limit: E;0 / upper-limit: S;9.
     * @return range from tier E level 0 to tire S level 9
     */
    @NotNull
    public static AvailableRange parseSignet(@Nullable ConfigurationSection section) {
        if(section == null) return AvailableRange.getDefault();
        String lowerStr = section.getString("lower-limit");
        String upperStr = section.getString("upper-limit");
        double min, max;
        boolean includeMin, includeMax;

        if(lowerStr != null) {
            String[] parts = lowerStr.split(";");
            String tier =  parts[0];
            AvailableRange range = Optional.ofNullable(IntensityDomain.getDomain(tier))
                    .map(IntensityDomain::getAvailableRange).orElse(AvailableRange.getDefault());

            min = range.lowerLimit;
            try {
                min += Double.parseDouble(parts[1].trim()) * 0.1;
            } catch(ArrayIndexOutOfBoundsException | NumberFormatException ignored) {

            }
            includeMin = range.includeMin;
        } else {
            min = AvailableRange.getDefault().lowerLimit;
            includeMin = AvailableRange.getDefault().includeMin;
        }

        if(upperStr != null) {
            String[] parts = upperStr.split(";");
            String tier =  parts[0];
            AvailableRange range = Optional.ofNullable(IntensityDomain.getDomain(tier))
                    .map(IntensityDomain::getAvailableRange).orElse(AvailableRange.getDefault());

            // Add from lower
            max = range.lowerLimit;
            try {
                max += Double.parseDouble(parts[1].trim()) * 0.1;
            } catch(ArrayIndexOutOfBoundsException | NumberFormatException ignored) {

            }
            includeMax = range.includeMax;
        } else {
            max = AvailableRange.getDefault().upperLimit;
            includeMax = AvailableRange.getDefault().includeMax;
        }

        return new AvailableRange(min, max, includeMin, includeMax);
    }
}
