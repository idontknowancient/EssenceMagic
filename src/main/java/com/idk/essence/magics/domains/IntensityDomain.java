package com.idk.essence.magics.domains;

import com.idk.essence.magics.DomainAccordance;
import com.idk.essence.magics.MagicDomain;
import com.idk.essence.magics.RoalkomEngine;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class IntensityDomain extends MagicDomain {

    @Getter private static int leastAptitude;
    private static final List<String> tiers = new ArrayList<>();
    private static final Map<String, IntensityDomain> domains = new LinkedHashMap<>();

    @Getter private final RoalkomEngine.AvailableRange availableRange;

    public IntensityDomain(String internalName) {
        super(internalName);
        availableRange = RoalkomEngine.parseDomain(config.getString(pathPrefix + ".range", ""));
        setupItemBuilder();

        tiers.add(internalName);
        domains.put(internalName, this);
    }

    public static void initialize() {
        leastAptitude = config.getInteger("domain." + DomainAccordance.INTENSITY.getName() + ".least-aptitude", 1);
    }

    @Override
    public void setupItemBuilder() {
        super.setupItemBuilder();
        itemBuilder.addLore("&fAvailable Range: " + availableRange.toString());
    }

    @Nullable
    public static IntensityDomain getDomain(@Nullable String internalName) {
        return domains.get(internalName);
    }

    @NotNull
    public static Collection<IntensityDomain> getDomains() {
        return domains.values();
    }

    @Override
    protected DomainAccordance getAccordance() {
        return DomainAccordance.INTENSITY;
    }

    @Override
    public String toString() {
        return "Intensity Domain " + getInternalName() + ": " + availableRange.toString();
    }
}
