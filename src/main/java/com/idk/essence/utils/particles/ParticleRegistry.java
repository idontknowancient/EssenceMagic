package com.idk.essence.utils.particles;

import com.idk.essence.utils.particles.shapes.CircleEffect;
import com.idk.essence.utils.particles.shapes.RotatingSquareEffect;
import com.idk.essence.utils.particles.shapes.SpiralEffect;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum ParticleRegistry {

    CIRCLE("circle", CircleEffect::new),
    ROTATING_SQUARE("rotating_square", RotatingSquareEffect::new),
    SPIRAL("spiral", SpiralEffect::new),
    ;

    @Getter private final String name;
    @Getter private final Function<ConfigurationSection, ParticleEffect> constructor;

    ParticleRegistry(String name, Function<ConfigurationSection, ParticleEffect> constructor) {
        this.name = name;
        this.constructor = constructor;
    }

    /**
     * Get ParticleEffect by section. e.g. shape: circle
     */
    @Nullable
    public static ParticleEffect get(ConfigurationSection section) {
        if(section == null) return null;
        String name = section.getString("shape",  "circle");
        try {
            return ParticleRegistry.valueOf(name.toUpperCase()).constructor.apply(section);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
