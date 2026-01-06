package com.idk.essence.mobs;

import org.bukkit.entity.EntityType;

public class MobTemplate {

    private final Mob mob;

    public MobTemplate(String internalName) {
        mob = new Mob(internalName);
    }

    public MobTemplate displayName(String displayName) {
        mob.setDisplayName(displayName);
        return this;
    }

    public MobTemplate type(EntityType type) {
//        mob.setType(type);
        return this;
    }

}
