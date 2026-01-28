package com.idk.essence.utils.particles.shapes;

import com.idk.essence.utils.particles.ParticleEffect;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import java.util.Optional;

@Getter
public class RotatingSquareEffect extends ParticleEffect {

    private double baseAngle = 0;
    private double radius = 3;
    private int pointCount = 8;
    private double bounceAmplitude = 0.2;
    private double rotationSpeed = 2;
    private int tick = 0;

    public RotatingSquareEffect(ConfigurationSection section) {
        super(section);
        radius = getSection().getDouble("radius", 3);
        pointCount = getSection().getInt("point-count", 8);
        bounceAmplitude = getSection().getDouble("bounce-amplitude", 0.2);
        rotationSpeed = getSection().getDouble("rotation-speed", 2);
    }

    public RotatingSquareEffect(Entity owner) {
        super(owner);
    }

    @Override
    public void repeat() {
        // four angles(before rotation)
        double[][] corners = {
                { -radius, -radius },
                {  radius, -radius },
                {  radius,  radius },
                { -radius,  radius }
        };

        for (int i = 0; i < 4; i++) {
            double[] from = corners[i];
            double[] to = corners[(i + 1) % 4];

            for (int j = 0; j <= pointCount; j++) {
                double x = from[0] + (to[0] - from[0]) * j / pointCount;
                double z = from[1] + (to[1] - from[1]) * j / pointCount;

                // rotating coordination
                double angleRad = Math.toRadians(baseAngle);
                double rotatedX = x * Math.cos(angleRad) - z * Math.sin(angleRad);
                double rotatedZ = x * Math.sin(angleRad) + z * Math.cos(angleRad);

                // flittering y-axis (similar to bounce)
                double y = bounceAmplitude * Math.sin(Math.toRadians(tick * 10 + j * 15));

                Location location = getCenter().clone().add(rotatedX, y, rotatedZ);
                Optional.ofNullable(getCenter().getWorld()).ifPresent(world ->
                        world.spawnParticle(getParticle(), getCenter().clone(), 1, 0, 0, 0, 0, getOptions()));
            }
        }

        baseAngle = (baseAngle + rotationSpeed) % 360;
        tick++;
    }
}
