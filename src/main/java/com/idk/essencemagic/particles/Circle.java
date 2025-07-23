package com.idk.essencemagic.particles;

import com.idk.essencemagic.EssenceMagic;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class Circle extends CustomParticle {

    private final Location center;
    private final Particle particle;
    private final double radius;

    private double baseAngle = 0;

    public Circle(Location center, Particle particle, double radius, int pointCount, double bounceAmplitude, double rotationSpeedDeg, long tickInterval) {
        this.center = center.add(0.5, 0.5, 0.5); // move above based on the block
        this.particle = particle;
        this.radius = radius;

        this.task = new BukkitRunnable() {
            private int tick = 0;

            @Override
            public void run() {
                for (int i = 0; i < pointCount; i++) {
                    double angleDeg = baseAngle + ((double)i / pointCount) * 360.0;
                    double rad = Math.toRadians(angleDeg);
                    double x = radius * Math.cos(rad);
                    double z = radius * Math.sin(rad);

                    // 閃爍跳動 Y 軸（類似 bounce 效果）
                    double y = bounceAmplitude * Math.sin(Math.toRadians(tick * 10 + i * 20));

                    Location loc = center.clone().add(x, y, z);
                    assert center.getWorld() != null;
                    center.getWorld().spawnParticle(particle, loc, 1, 0, 0, 0, 0);
                }

                baseAngle += rotationSpeedDeg;
                if (baseAngle >= 360) baseAngle -= 360;
                tick++;
            }
        }.runTaskTimer(EssenceMagic.getPlugin(), 0L, tickInterval);
    }
}
