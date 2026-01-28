package com.idk.essence.skills.types;

import com.idk.essence.items.items.ItemBuilder;
import com.idk.essence.skills.SkillTemplate;
import com.idk.essence.skills.SkillType;
import com.idk.essence.utils.Util;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.function.BiConsumer;

public class ProjectileType implements SkillType {

    /**
     * Initialize depending on the type
     */
    private enum ProjectileBehavior {

        ARROW(Arrow.class, (projectile, settings) -> {
            Arrow arrow = (Arrow) projectile;
            arrow.setDamage(Math.max(settings.getDouble("damage", 2.0), 0));
        }),

        FIREBALL(Fireball.class, (projectile, settings) -> {
            Fireball fireball = (Fireball) projectile;
            fireball.setIsIncendiary(settings.getBoolean("incendiary", false));
            fireball.setYield((float) Math.max(settings.getDouble("power", 1.0), 0));
        }),

        SNOWBALL(Snowball.class, (projectile, settings) -> {
            Snowball snowball = (Snowball) projectile;
            snowball.setItem(new ItemBuilder(settings.getString("item")).build());
        }),
        ;

        @Getter private final Class<? extends Projectile> entityClass;
        private final BiConsumer<Projectile, ConfigurationSection> initializer;

        ProjectileBehavior(Class<? extends Projectile> entityClass, BiConsumer<Projectile, ConfigurationSection> initializer) {
            this.entityClass = entityClass;
            this.initializer = initializer;
        }

        /**
         * Overall settings and custom settings
         */
        public void init(LivingEntity target, Projectile projectile, ConfigurationSection settings) {
            if(settings.contains("display-name"))
                projectile.customName(Util.System.parseMessage(settings.getString("display-name", "")));
            if(settings.contains("name-visible"))
                projectile.setCustomNameVisible(settings.getBoolean("name-visible", true));
            if(settings.contains("glowing"))
                projectile.setGlowing(settings.getBoolean("glowing", false));
            if(settings.contains("gravity"))
                projectile.setGravity(settings.getBoolean("gravity", true));
            if(settings.contains("velocity")) {
                final Vector velocity = target.getEyeLocation().getDirection().normalize()
                        .multiply(settings.getDouble("velocity", 1.0));
                projectile.setVelocity(velocity);
            }
            initializer.accept(projectile, settings);
        }
    }

    @Override
    public String name() {
        return "projectile";
    }

    @Override
    public void execute(LivingEntity target, SkillTemplate template) {
        ConfigurationSection settings = template.getSettings();
        ProjectileBehavior behavior;
        try {
            behavior = ProjectileBehavior.valueOf(settings.getString("projectile", "snowball").toUpperCase());
        } catch (IllegalArgumentException e) {
            behavior = ProjectileBehavior.SNOWBALL;
        }

        Projectile projectile = target.launchProjectile(behavior.getEntityClass());
        behavior.init(target, projectile, settings);
    }
}
