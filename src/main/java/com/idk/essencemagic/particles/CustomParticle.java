package com.idk.essencemagic.particles;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CustomParticle {

    public static final Map<Location, CustomParticle> activatingParticles = new LinkedHashMap<>();

    BukkitTask task;

    public void stop() {
        task.cancel();
    }

}
