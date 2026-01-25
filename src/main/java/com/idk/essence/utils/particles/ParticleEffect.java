package com.idk.essence.utils.particles;

import com.idk.essence.Essence;
import com.idk.essence.utils.Util;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

@Getter
public abstract class ParticleEffect {

    protected BukkitTask task;
    protected ConfigurationSection section;
    protected boolean display;
    protected Particle particle;
    protected Vector offset;
    protected Location center;
    protected final int tickInterval = 1;

    // Get the particle section and set the particle type
    protected ParticleEffect(ConfigurationSection section) {
        this.section = section;
        display = section.getBoolean("display", true);
        try {
            particle = Particle.valueOf(section.getString("type", "CLOUD").toUpperCase());
        } catch(IllegalArgumentException e) {
            particle = Particle.CLOUD;
        }
    }

    public void generate(Location location) {
        offset = Util.getVector(section.getConfigurationSection("offset"));
        center = location.clone().add(offset);
        if(!display || ParticleManager.hasKey(location.clone())) return;

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                repeat();
            }
        }.runTaskTimer(Essence.getPlugin(), 0L, tickInterval);

        // Remember to clone
        ParticleManager.add(location.clone(), this);
    }

    public abstract void repeat();

    public void stop() {
        if(task != null)
            task.cancel();
    }

}
