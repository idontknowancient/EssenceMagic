package com.idk.essence.utils.particles;

import com.idk.essence.Essence;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public abstract class CustomParticle {

    public static final Map<Location, CustomParticle> activatingParticles = new LinkedHashMap<>();

    BukkitTask task;
    ConfigurationSection section;
    Location center;
    Location offset;
    Particle particle;
    final int tickInterval = 1;

    // get the custom particle part
    public CustomParticle(ConfigurationSection section) {
        this.section = section;
    }

    public void generate(Location location) {
        // initialize common attributes
        offset = new Location(location.getWorld(), 0.5, section.getDouble("y-offset", 0.1), 0.5);
        center = location.clone().add(offset); // move above based on the block
        try {
            particle = Particle.valueOf(section.getString("type", "CLOUD").toUpperCase());
        } catch(IllegalArgumentException e) {
            particle = Particle.CLOUD;
        }

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                repeat();
            }
        }.runTaskTimer(Essence.getPlugin(), 0L, tickInterval);

        activatingParticles.put(location, this);
    }

    public abstract void repeat();

    public void stop() {
        if(task != null)
            task.cancel();
    }

}
