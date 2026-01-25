package com.idk.essence.utils.particles.shapes;

import com.idk.essence.utils.particles.ParticleEffect;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

@Getter
public class CircleEffect extends ParticleEffect {

    private double baseAngle = 0;
    private final double radius;
    private final int pointCount;
    private final double bounceAmplitude;
    private final double rotationSpeed;
    private int tick = 0;

    public CircleEffect(ConfigurationSection section) {
        super(section);
        ConfigurationSection settings = section.getConfigurationSection("settings");
        radius = Optional.ofNullable(settings).map(s -> s.getDouble("radius", 3)).orElse(3d);
        pointCount = Optional.ofNullable(settings).map(s -> s.getInt("point-count", 8)).orElse(8);
        bounceAmplitude = Optional.ofNullable(settings).map(s -> s.getDouble("bounce-amplitude", 0)).orElse(0d);
        rotationSpeed = Optional.ofNullable(settings).map(s -> s.getDouble("rotation-speed", 2)).orElse(2d);
    }

    @Override
    public void repeat() {
        for(int i = 0; i < pointCount; i++) {
            double angleDeg = baseAngle + ((double)i / pointCount) * 360.0;
            double rad = Math.toRadians(angleDeg);
            double x = radius * Math.cos(rad);
            double z = radius * Math.sin(rad);

            // Flittering y-axis (similar to bounce)
            double y = bounceAmplitude * Math.sin(Math.toRadians(tick * 10 + i * 20));

            Location location = getCenter().clone().add(x, y, z);
            assert getCenter().getWorld() != null;
            Optional.ofNullable(getCenter().getWorld()).ifPresent(world ->
                            world.spawnParticle(getParticle(), location, 1, 0, 0, 0, 0));
        }

        baseAngle = (baseAngle + rotationSpeed) % 360;
        tick++;
    }
}
