package com.idk.essencemagic.skills;

public enum SkillType {

    SHOOT("shoot"),
    CUSTOM("custom"),
    ;

    public final String name;

    SkillType(String name) {
        this.name = name;
    }
}
