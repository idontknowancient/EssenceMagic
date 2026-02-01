package com.idk.essence.skills;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

public enum Target {

    ENTITY("ENTITY"),
    MOB("MOB"),
    PLAYER("PLAYER"),
    SELF("SELF"),
    ;

    public final String name;

    Target(String name) {
        this.name = name;
    }

    /**
     * @return corresponding target, default is self
     */
    public static Target get(@Nullable String name) {
        if(name == null) return SELF;
        try {
            return Target.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SELF;
        }
    }

    private Set<LivingEntity> getTargets(LivingEntity caster, LivingEntity target) {
        Set<LivingEntity> targets = new LinkedHashSet<>();
        if(this == SELF)
            targets.add(caster);
        if(this == ENTITY)
            targets.add(target);
        if(this == MOB && !(target instanceof Player))
            targets.add(target);
        if(this == PLAYER && target instanceof Player)
            targets.add(target);

        return targets;
    }

    public Set<LivingEntity> getTargets(LivingEntity caster, Event event) {
        Set<LivingEntity> targets = new LinkedHashSet<>();
        if(event == null || event instanceof PlayerInteractEvent e) {
            targets.addAll(getTargets(caster, caster));
        } else if(event instanceof EntityDamageByEntityEvent e) {
            if(!(e.getDamager() instanceof LivingEntity attacker) || !(e.getEntity() instanceof LivingEntity target)) return targets;
            targets.addAll(getTargets(attacker, target));
        }

        return targets;
    }
}
