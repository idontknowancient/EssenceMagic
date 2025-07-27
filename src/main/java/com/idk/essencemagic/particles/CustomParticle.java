package com.idk.essencemagic.particles;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public abstract class CustomParticle {

    public static final Map<Location, CustomParticle> activatingParticles = new LinkedHashMap<>();

    BukkitTask task;
    Location center;
    Location offset;
    Particle particle;

    public void stop() {
        task.cancel();
    }

}
