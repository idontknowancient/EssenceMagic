package com.idk.essence.magics.domains;

import com.idk.essence.magics.DomainAccordance;
import com.idk.essence.magics.MagicDomain;
import com.idk.essence.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

public class OriginDomain extends MagicDomain {

    public OriginDomain(String internalName) {
        super(internalName);
    }

    @Override
    public void initialize() {

    }

    @Override
    protected DomainAccordance getAccordance() {
        return DomainAccordance.ORIGIN;
    }

    @Override
    protected Component getDisplayName() {
        return ConfigManager.DefaultFile.CONFIG.outString("domain." + getAccordance().getName() + "." + getInternalName(),
                Component.text(getInternalName()));
    }

    @Override
    public String toString() {
        return "Origin Domain " + getInternalName();
    }
}
