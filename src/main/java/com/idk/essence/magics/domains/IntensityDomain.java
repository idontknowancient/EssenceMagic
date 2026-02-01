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

    private static final List<String> tiers = new ArrayList<>();
    private static final Map<String, IntensityDomain> domains = new LinkedHashMap<>();

    @Getter private final RoalkomEngine.AvailableRange availableRange;

    public IntensityDomain(String internalName, @Nullable String rangeString) {
        super(internalName);
        availableRange = RoalkomEngine.parseDomain(rangeString);
        tiers.add(internalName);
        domains.put(internalName, this);
    }

    @Override
    public void initialize() {
        tiers.clear();
        domains.clear();
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
    protected Component getDisplayName() {
        return ConfigManager.DefaultFile.CONFIG.outString("domain." + getAccordance().getName() +
                "." + getInternalName() + ".display-name",  Component.text(getInternalName()));
    }

    @Override
    public String toString() {
        return "Intensity Domain " + getInternalName() + ": " + availableRange.toString();
    }
}
