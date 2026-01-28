package com.idk.essence.utils.particles;

import com.idk.essence.Essence;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.UUID;

@Getter
public abstract class ParticleEffect {

    protected BukkitTask task;
    protected ConfigurationSection section;
    protected boolean display = true;
    protected Particle particle = Particle.DUST;
    protected Vector offset;
    protected Location center;
    protected final int tickInterval = 2;

    // Get the particle section and set the particle type
    protected ParticleEffect(ConfigurationSection section) {
        this.section = section;
        display = section.getBoolean("display", true);
        try {
            particle = Particle.valueOf(section.getString("type", "CLOUD").toUpperCase());
        } catch(IllegalArgumentException ignored) {
        }
    }

    protected ParticleEffect(Entity owner) {

    }

    /**
     * Generate the particle effect by the location and offset.
     * Automatically add to map.
     */
    public void generate(Location location) {
        offset = Util.getVectorFromSection(section.getConfigurationSection("offset"));
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

    /**
     * Generate the particle effect moving with the entity.
     * Automatically add to map and set particle key.
     */
    public void generate(Entity entity) {
        UUID uuid = Util.getUUIDFromContainer(entity);
        if(uuid == null || ParticleManager.hasKey(uuid)) return;
        entity.getPersistentDataContainer().set(Key.Class.PARTICLE.get(), PersistentDataType.BOOLEAN, true);

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                center = entity.getLocation().clone();
                repeat();
            }
        }.runTaskTimer(Essence.getPlugin(), 0L, tickInterval);

        ParticleManager.add(uuid, this);
    }

    public abstract void repeat();

    public void stop() {
        if(task != null && !task.isCancelled())
            task.cancel();
    }

}
