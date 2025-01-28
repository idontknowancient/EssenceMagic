package com.idk.essencemagic.skills;

public enum SkillType {

    CUSTOM("custom"),
    POTION("potion"),
    SHOOT("shoot"),
    WAIT("wait"),
    ;

    public final String name;

    SkillType(String name) {
        this.name = name;
    }
}
