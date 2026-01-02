package com.idk.essence.magics;

public enum MagicType {
    ;

    public enum Feature {
        ATTACK("attack"),
        DEFENSE("defense"),
        SUPPORT("support"),
        CONTROL("control"),
        FEATURE("feature"),
        ;

        public final String name;

        Feature(String name) {
            this.name = name;
        }
    }

    public enum Attribute {

        ELEMENT("element"),
        RECONCILE("reconcile"),
        CREATION("creation"),
        ;

        public final String name;

        Attribute(String name) {
            this.name = name;
        }
    }
}
