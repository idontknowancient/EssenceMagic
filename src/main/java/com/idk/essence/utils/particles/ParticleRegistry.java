package com.idk.essence.utils.particles;

import com.idk.essence.utils.particles.shapes.CircleEffect;
import com.idk.essence.utils.particles.shapes.PointEffect;
import com.idk.essence.utils.particles.shapes.RotatingSquareEffect;
import com.idk.essence.utils.particles.shapes.SpiralEffect;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum ParticleRegistry {

    CIRCLE("circle", CircleEffect::new, CircleEffect::new),
    POINT("point", PointEffect::new, PointEffect::new),
    ROTATING_SQUARE("rotating_square", RotatingSquareEffect::new, RotatingSquareEffect::new),
    SPIRAL("spiral", SpiralEffect::new, SpiralEffect::new),
    ;

    @Getter private final String name;
    @Getter private final Function<ConfigurationSection, ParticleEffect> sectionConstructor;
    @Getter private final Function<Entity, ParticleEffect> entityConstructor;

    ParticleRegistry(String name, Function<ConfigurationSection, ParticleEffect> sectionConstructor,
                     Function<Entity, ParticleEffect> entityConstructor) {
        this.name = name;
        this.sectionConstructor = sectionConstructor;
        this.entityConstructor = entityConstructor;
    }

    /**
     * Get ParticleEffect by section. e.g. shape: circle
     */
    @Nullable
    public static ParticleEffect get(ConfigurationSection section) {
        if(section == null) return null;
        String name = section.getString("shape",  "circle");
        try {
            return ParticleRegistry.valueOf(name.toUpperCase()).sectionConstructor.apply(section);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get ParticleRegistry by a string.
     */
    @Nullable
    public static ParticleRegistry get(String name) {
        if(name == null) return null;
        try {
            return ParticleRegistry.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
