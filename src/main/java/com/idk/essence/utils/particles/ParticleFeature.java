package com.idk.essence.utils.particles;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ParticleFeature {

    @Nullable ConfigurationSection getParticleSection();

    /**
     * Complete with null handle. Automatically prevent repetition.
     */
    default void generateParticle(Location location) {
        if(ParticleManager.hasKey(location)) return;
        Optional.ofNullable(ParticleRegistry.get(getParticleSection())).ifPresent(particle ->
                particle.generate(location));
    }

    default void stopParticle(Location location) {
        ParticleManager.stop(location);
    }
}
