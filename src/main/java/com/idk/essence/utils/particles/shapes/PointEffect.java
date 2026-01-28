package com.idk.essence.utils.particles.shapes;

import com.idk.essence.utils.particles.ParticleEffect;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import java.util.Optional;

public class PointEffect extends ParticleEffect {

    public PointEffect(ConfigurationSection section) {
        super(section);
    }

    public PointEffect(Entity owner) {
        super(owner);
    }

    @Override
    public void repeat() {
        spawn(getCenter());
    }
}
