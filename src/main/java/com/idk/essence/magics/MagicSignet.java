package com.idk.essence.magics;

import com.idk.essence.items.items.ItemBuilder;
import com.idk.essence.magics.domains.IntensityDomain;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public class MagicSignet {

    private final String internalName;
    private final ItemBuilder itemBuilder;
    private Component displayName;
    private final List<MagicDomain> domains;
    private final RoalkomEngine.AvailableRange availableRange;

    /**
     * Record domains by accordance
     */
    private final Map<DomainAccordance, Set<MagicDomain>> classifications = new EnumMap<>(DomainAccordance.class);

    public MagicSignet(Builder builder) {
        this.internalName = builder.internalName;
        this.itemBuilder = builder.itemBuilder;
        this.displayName = builder.displayName;
        this.domains = builder.domains;
        this.availableRange = builder.availableRange;
    }

    public void addDomain(@Nullable MagicDomain domain) {
        if(domain == null) return;
        classifications.computeIfAbsent(domain.getAccordance(), acc -> new LinkedHashSet<>()).add(domain);
    }

    public boolean isDomain(@Nullable MagicDomain domain) {
        if(domain == null) return false;
        return classifications.values().stream().anyMatch(set -> set.contains(domain));
    }

    @Nullable
    public Set<MagicDomain> getDomainsIn(@Nullable DomainAccordance accordance) {
        if(accordance == null) return null;
        return classifications.get(accordance);
    }

    public static class Builder {

        private final String internalName;
        private final ItemBuilder itemBuilder;
        private Component displayName;
        private final List<MagicDomain> domains = new ArrayList<>();
        private RoalkomEngine.AvailableRange availableRange = RoalkomEngine.AvailableRange.getDefault();

        public Builder(@NotNull String internalName) {
            this.internalName = internalName;
            itemBuilder = new ItemBuilder(ConfigManager.DefaultFile.MENUS.getString("magic-signet.item"));
            itemBuilder.internalName(internalName);
            itemBuilder.container(Key.Type.MAGIC_SIGNET,  internalName);
        }

        public Builder displayName(Component displayName) {
            this.displayName = displayName;
            itemBuilder.displayName(displayName);
            return this;
        }

        public Builder domain(@Nullable List<String> domains) {
            if(domains == null) return this;
            domains.stream().map(MagicManager::getDomain).filter(Objects::nonNull).forEach(this::domain);
            return this;
        }

        public void domain(@NotNull MagicDomain domain) {
            domains.add(domain);
            itemBuilder.addLore(domain.getFullDisplay());
        }

        /**
         * E.g. lower-limit: E;0 / upper-limit: S;9.
         * Automatically set corresponding intensity domains according to the range.
         */
        public Builder range(@Nullable ConfigurationSection rangeSection) {
            if(rangeSection == null) return this;
            availableRange = RoalkomEngine.parseSignet(rangeSection);
            IntensityDomain.getDomains().stream()
                    .filter(domain -> domain.getAvailableRange().intersects(availableRange))
                    .forEach(this::domain);
            itemBuilder.addLore(this.availableRange.toString());
            return this;
        }

        public Builder fromConfig(EssenceConfig config) {
            return displayName(config.outString(internalName + ".display-name", ""))
                    .domain(config.getStringList(internalName + ".domain"))
                    .range(config.getConfigurationSection(internalName + ".available-range"));
        }

        public MagicSignet build() {
            return new MagicSignet(this);
        }
    }
}
