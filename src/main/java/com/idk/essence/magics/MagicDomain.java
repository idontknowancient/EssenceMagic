package com.idk.essence.magics;

import com.idk.essence.items.items.ItemBuilder;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class MagicDomain {

    protected static final ConfigManager.DefaultFile config = ConfigManager.DefaultFile.CONFIG;
    private final String internalName;
    protected final ItemBuilder itemBuilder;
    protected final String pathPrefix;
    @Setter private Component displayName;
    /**
     * Chance that decides whether the player can get the aptitude of this domain.
     */
    @Setter private double aptitudeChance;

    public MagicDomain(String internalName) {
        this.internalName = internalName;
        itemBuilder = new ItemBuilder(ConfigManager.DefaultFile.MENUS.getString("magic-domain.item"));
        pathPrefix = "domain." + getAccordance().getName() + "." + internalName;

        displayName = config.outString(pathPrefix + ".display-name",  Component.text(getInternalName()));
        aptitudeChance = config.getDouble(pathPrefix + ".aptitude-chance", 1);
    }

    /**
     * Should be called by subclass.
     */
    protected void setupItemBuilder() {
        itemBuilder.displayName(getFullDisplay());
        itemBuilder.addLore("&fAptitude chance: " + aptitudeChance);
    }

    protected abstract DomainAccordance getAccordance();

    public boolean isAccordance(@NotNull DomainAccordance accordance) {
        return getAccordance().equals(accordance);
    }

    /**
     * Get full display of a domain.
     * E.g. "&a功能 &#c1d8ac攻擊型".
     */
    public Component getFullDisplay() {
        return getAccordance().getDisplayName().append(Component.text(" ")).append(getDisplayName());
    }
}
