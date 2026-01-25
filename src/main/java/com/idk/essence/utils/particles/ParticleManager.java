package com.idk.essence.utils.particles;

import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.utils.CustomKey;
import com.jeff_media.customblockdata.CustomBlockData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.idk.essence.players.ManaManager.plugin;

public class ParticleManager implements Listener {

    /**
     * Used to register listener
     */
    @Getter private static final ParticleManager instance = new ParticleManager();

    /**
     * Store all custom particles according to UUID (entity)
     */
    private static final Map<UUID, ParticleEffect> entityParticles = new HashMap<>();

    /**
     * Store all custom particles according to Location
     */
    private static final Map<Location, ParticleEffect> locationParticles = new HashMap<>();

    private ParticleManager() {}

    public static void initialize() {
        shutdown();
        activateParticles();
    }

    public static void shutdown() {
        entityParticles.values().forEach(ParticleEffect::stop);
        locationParticles.values().forEach(ParticleEffect::stop);
        entityParticles.clear();
        locationParticles.clear();
    }

    /**
     * Find all tile states and generate particles.
     */
    private static void activateParticles() {
        for(World world : Bukkit.getWorlds()) {
            for(Chunk chunk : world.getLoadedChunks()) {
                activateChunkParticles(chunk);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        activateChunkParticles(event.getChunk());
    }

    private static void activateChunkParticles(Chunk chunk) {
        for(Block block : CustomBlockData.getBlocksWithCustomData(plugin, chunk)) {
            if(!hasParticle(block)) return;
            if(ArtifactFactory.getBehavior(block) instanceof ParticleFeature particleFeature &&
                    !locationParticles.containsKey(block.getLocation())) {
                particleFeature.generateParticle(block.getLocation());
            }
        }
    }

    public static boolean hasParticle(Block block) {
        CustomBlockData data = new CustomBlockData(block, plugin);
        return Optional.ofNullable(data.get(CustomKey.getParticleKey(), PersistentDataType.BOOLEAN)).orElse(false);
    }

    public static boolean hasKey(Location location) {
        return locationParticles.containsKey(location);
    }

    public static void add(UUID uuid, ParticleEffect particle) {
        entityParticles.put(uuid, particle);
    }

    public static void add(Location location, ParticleEffect particle) {
        locationParticles.put(location, particle);
    }

    /**
     * Stop a particle effect by uuid.
     */
    public static void stop(UUID uuid) {
        Optional.ofNullable(entityParticles.get(uuid)).ifPresent(ParticleEffect::stop);
        entityParticles.remove(uuid);
    }

    /**
     * Stop a particle effect by location.
     */
    public static void stop(Location location) {
        Optional.ofNullable(locationParticles.get(location)).ifPresent(ParticleEffect::stop);
        locationParticles.remove(location);
    }
}
