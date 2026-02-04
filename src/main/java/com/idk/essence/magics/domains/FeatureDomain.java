package com.idk.essence.magics.domains;

import com.idk.essence.magics.DomainAccordance;
import com.idk.essence.magics.MagicDomain;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;

public class FeatureDomain extends MagicDomain {

    @Getter private static int leastAptitude;

    public FeatureDomain(String internalName) {
        super(internalName);
        setupItemBuilder();
    }

    public static void initialize() {
        leastAptitude = config.getInteger("domain." + DomainAccordance.FEATURE.getName() + ".least-aptitude", 1);
    }

    @Override
    protected DomainAccordance getAccordance() {
        return DomainAccordance.FEATURE;
    }

    @Override
    public String toString() {
        return "Feature Domain " + getInternalName();
    }
}
