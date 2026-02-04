package com.idk.essence.magics;

import com.idk.essence.elements.ElementFactory;
import com.idk.essence.magics.domains.ElementDomain;
import com.idk.essence.magics.domains.FeatureDomain;
import com.idk.essence.magics.domains.IntensityDomain;
import com.idk.essence.magics.domains.OriginDomain;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;

public enum DomainAccordance {

    FEATURE("feature"),
    ELEMENT("element"),
    INTENSITY("intensity"),
    ORIGIN("origin"),
    ;

    @Getter private final String name;
    @Getter private Component displayName;

    DomainAccordance(String name) {
        this.name = name;
    }

    /**
     * Register all domains defined in config.yml.
     * Case-sensitive.
     */
    public static void registerDomains() {
        // Set display name
        for(DomainAccordance accordance : DomainAccordance.values()) {
            accordance.displayName = ConfigManager.DefaultFile.CONFIG.outString("domain." + accordance.name + ".display-name",
                    Component.text(Util.Tool.capitalize(accordance.name) + " Domain"));
        }

        ConfigurationSection section = ConfigManager.DefaultFile.CONFIG.getConfigurationSection("domain");
        if(section == null) return;
        for(String key : section.getKeys(false)) {
            // Accordance: feature.  e.g. attack
            if(key.equals(DomainAccordance.FEATURE.name)) {
                ConfigurationSection featureSection = section.getConfigurationSection(DomainAccordance.FEATURE.name);
                if(featureSection == null) continue;
                featureSection.getKeys(false).stream()
                        .filter(name -> !name.equals("display-name") && !name.equals("least-aptitude"))
                        .forEach(name -> MagicManager.addDomain(name, new FeatureDomain(name)));
            }

            // Accordance: element. e.g. flame, water
            else if(key.equals(DomainAccordance.ELEMENT.name)) {
                ConfigurationSection elementSection = section.getConfigurationSection(DomainAccordance.ELEMENT.name);
                if(elementSection == null) continue;
                // Take all elements in element folder as domains
                if(elementSection.getBoolean("all-in-folder", true)) {
                    ElementFactory.getAllKeys().forEach(name ->
                            MagicManager.addDomain(name, new ElementDomain(name)));
                } else {
                    elementSection.getStringList("otherwise").stream().filter(ElementFactory::has)
                            .filter(name -> !name.equals("display-name") && !name.equals("least-aptitude"))
                            .forEach(name -> MagicManager.addDomain(name, new ElementDomain(name)));
                }
            }

            // Accordance: intensity. e.g. F: "[0, 1)"
            else if(key.equals(DomainAccordance.INTENSITY.name)) {
                ConfigurationSection intensitySection = section.getConfigurationSection(DomainAccordance.INTENSITY.name);
                if(intensitySection == null) continue;
                intensitySection.getKeys(false).stream()
                        .filter(name -> !name.equals("display-name") && !name.equals("least-aptitude"))
                        .forEach(name -> MagicManager.addDomain(name, new IntensityDomain(name)));
            }

            // Accordance: origin. e.g. scroll
            else if(key.equals(DomainAccordance.ORIGIN.name)) {
                ConfigurationSection originSection = section.getConfigurationSection(DomainAccordance.ORIGIN.name);
                if(originSection == null) continue;
                originSection.getKeys(false).stream()
                        .filter(name -> !name.equals("display-name") && !name.equals("least-aptitude"))
                        .forEach(name -> MagicManager.addDomain(name, new OriginDomain(name)));
            }
        }
    }
}
