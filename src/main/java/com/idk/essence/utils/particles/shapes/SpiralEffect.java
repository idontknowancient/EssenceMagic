package com.idk.essence.utils.particles.shapes;

import com.idk.essence.utils.particles.ParticleEffect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

public class SpiralEffect extends ParticleEffect {

    public SpiralEffect(ConfigurationSection section) {
        super(section);
    }

    public SpiralEffect(Entity owner) {
        super(owner);
    }

    @Override
    public void repeat() {

    }
}
