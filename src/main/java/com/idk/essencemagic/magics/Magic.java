package com.idk.essencemagic.magics;

import com.idk.essencemagic.EssenceMagic;

import java.util.HashMap;
import java.util.Map;

public abstract class Magic {

    static final EssenceMagic plugin = EssenceMagic.getPlugin();

    public static final Map<String, Magic> magics = new HashMap<>();

    abstract String getId();

    public enum MagicType {

        ELEMENT("element"),
        RECONCILE("reconcile"),
        MANIPULATE("manipulate"),
        MIXED("mixed"),
        ;

        private final String id;

        MagicType(String id) {
            this.id = id;
        }
    }
}
