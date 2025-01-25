package com.idk.essencemagic.magics;

import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;

@Getter
public class MagicElement extends Magic {

    private static final ConfigFile.ConfigName cm = ConfigFile.ConfigName.MAGICS_ELEMENT;

    private final MagicType magicType = MagicType.ELEMENT;

    private final String id;

    private final String displayName;

    private final Element element;

    

    public MagicElement(String id) {
        this.id = id;
        displayName = cm.getString(id+".name");
        element = Element.elements.get(cm.getString(id+".element"));
    }

    @Override
    String getId() {
        return id;
    }
}
