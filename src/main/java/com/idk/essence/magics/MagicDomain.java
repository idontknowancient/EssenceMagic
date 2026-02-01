package com.idk.essence.magics;

import lombok.Getter;
import net.kyori.adventure.text.Component;

@Getter
public abstract class MagicDomain {

    private final String internalName;

    public MagicDomain(String internalName) {
        this.internalName = internalName;
    }

    public abstract void initialize();
    protected abstract DomainAccordance getAccordance();
    protected abstract Component getDisplayName();

    /**
     * Get full display of a domain.
     * E.g. "&a功能 &#c1d8ac攻擊型".
     */
    public Component getFullDisplay() {
        return getAccordance().getDisplayName().append(Component.text(" ")).append(getDisplayName());
    }
}
