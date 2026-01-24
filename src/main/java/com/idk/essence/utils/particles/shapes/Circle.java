package com.idk.essence.utils.particles.shapes;

import com.idk.essence.utils.particles.CustomParticle;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class Circle extends CustomParticle {

    private double baseAngle = 0;
    private final double radius;
    private final int pointCount;
    private final double bounceAmplitude;
    private final double rotationSpeed;
    private int tick = 0;

    public Circle(ConfigurationSection section) {
        super(section);
        radius = getSection().getDouble("radius", 3);
        pointCount = getSection().getInt("point-count", 8);
        bounceAmplitude = getSection().getDouble("bounce-amplitude", 0.2);
        rotationSpeed = getSection().getDouble("rotation-speed", 20);
    }

    @Override
    public void repeat() {
        for (int i = 0; i < pointCount; i++) {
            double angleDeg = baseAngle + ((double)i / pointCount) * 360.0;
            double rad = Math.toRadians(angleDeg);
            double x = radius * Math.cos(rad);
            double z = radius * Math.sin(rad);

            // flittering y-axis (similar to bounce)
            double y = bounceAmplitude * Math.sin(Math.toRadians(tick * 10 + i * 20));

            Location location = getCenter().clone().add(x, y, z);
            assert getCenter().getWorld() != null;
            getCenter().getWorld().spawnParticle(getParticle(), location, 1, 0, 0, 0, 0);
        }

        baseAngle = (baseAngle + rotationSpeed) % 360;
        tick++;
    }
}
