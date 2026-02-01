package com.idk.essence.magics.domains;

import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.magics.DomainAccordance;
import com.idk.essence.magics.MagicDomain;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class ElementDomain extends MagicDomain {

    public ElementDomain(String internalName) {
        super(internalName);
    }

    @Override
    public void initialize() {

    }

    @Override
    protected DomainAccordance getAccordance() {
        return DomainAccordance.ELEMENT;
    }

    @Override
    protected Component getDisplayName() {
        return Optional.ofNullable(ElementFactory.get(getInternalName())).map(Element::getDisplayName)
                .orElse(Component.text(getInternalName()));
    }

    @Override
    public String toString() {
        return "Element Domain " + getInternalName();
    }
}
