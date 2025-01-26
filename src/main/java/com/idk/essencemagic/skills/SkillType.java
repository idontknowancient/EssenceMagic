package com.idk.essencemagic.skills;

public enum SkillType {

    POTION("potion"),
    SHOOT("shoot"),
    CUSTOM("custom"),
    ;

    public final String name;

    SkillType(String name) {
        this.name = name;
    }
}
