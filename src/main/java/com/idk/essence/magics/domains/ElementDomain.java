package com.idk.essence.magics.domains;

import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.magics.DomainAccordance;
import com.idk.essence.magics.MagicDomain;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ElementDomain extends MagicDomain {

    @Getter private static int leastAptitude;

    public ElementDomain(String internalName) {
        super(internalName);
        Optional<@Nullable Element> element = Optional.ofNullable(ElementFactory.get(getInternalName()));

        // Override original settings
        setDisplayName(element.map(Element::getDisplayName).orElse(Component.text(getInternalName())));
        setAptitudeChance(element.map(Element::getAptitudeChance).orElse(1d));
        setupItemBuilder();
    }

    public static void initialize() {
        leastAptitude = config.getInteger("domain." + DomainAccordance.ELEMENT.getName() + ".least-aptitude", 1);
    }

    @Override
    protected DomainAccordance getAccordance() {
        return DomainAccordance.ELEMENT;
    }

    @Override
    public String toString() {
        return "Element Domain " + getInternalName();
    }
}
