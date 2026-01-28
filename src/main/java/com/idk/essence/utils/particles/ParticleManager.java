package com.idk.essence.utils.particles;

import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import com.jeff_media.customblockdata.CustomBlockData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
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
     * Store all custom particles according to UUID (entity owner)
     */
    private static final Map<UUID, ParticleEffect> uuidParticles = new HashMap<>();

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
        uuidParticles.values().forEach(ParticleEffect::stop);
        locationParticles.values().forEach(ParticleEffect::stop);
        uuidParticles.clear();
        locationParticles.clear();
    }

    /**
     * Find all blocks with custom data and generate particles.
     */
    private static void activateParticles() {
        for(World world : Bukkit.getWorlds()) {
            for(Chunk chunk : world.getLoadedChunks()) {
                activateLocationParticles(chunk);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        activateLocationParticles(event.getChunk());
    }

    @EventHandler
    public void onEntityLoad(EntitiesLoadEvent  event) {

    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        deactivateLocationParticles(event.getChunk());
    }

    @EventHandler
    public void onEntityUnload(EntitiesUnloadEvent event) {

    }

    private static void activateLocationParticles(Chunk chunk) {
        for(Block block : CustomBlockData.getBlocksWithCustomData(plugin, chunk)) {
            if(!hasParticle(block)) return;
            if(ArtifactFactory.getBehavior(block) instanceof ParticleFeature particleFeature &&
                    !locationParticles.containsKey(block.getLocation())) {
                particleFeature.generateParticle(block.getLocation());
            }
        }
    }

    /**
     * Save resources.
     */
    private static void deactivateLocationParticles(Chunk chunk) {
        for(Block block : CustomBlockData.getBlocksWithCustomData(plugin, chunk)) {
            if(!hasParticle(block)) return;
            stop(block.getLocation());
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasParticle(Block block) {
        CustomBlockData data = new CustomBlockData(block, plugin);
        return Optional.ofNullable(data.get(Key.Class.PARTICLE.get(), PersistentDataType.BOOLEAN)).orElse(false);
    }

    /**
     * Check if the entity has the particle key in its container.
     */
    public static boolean hasParticle(Entity entity) {
        return Optional.ofNullable(entity.getPersistentDataContainer()
                .get(Key.Class.PARTICLE.get(), PersistentDataType.BOOLEAN)).orElse(false);
    }

    public static boolean hasKey(Location location) {
        return locationParticles.containsKey(location);
    }

    public static boolean hasKey(UUID uuid) {
        return uuidParticles.containsKey(uuid);
    }

    public static void add(UUID uuid, ParticleEffect particle) {
        uuidParticles.put(uuid, particle);
    }

    public static void add(Location location, ParticleEffect particle) {
        locationParticles.put(location, particle);
    }

    public static void generate(Entity owner, ParticleRegistry registry) {
        Util.consoleLog("has particle", hasParticle(owner));
        Util.consoleLog("has key", hasKey(Util.getUUIDFromContainer(owner)));
        if(owner == null || registry == null || !hasParticle(owner) || hasKey(Util.getUUIDFromContainer(owner))) return;
        registry.getEntityConstructor().apply(owner).generate(owner);
    }

    public static void generate(Entity owner, String particleName) {
        Optional.ofNullable(ParticleRegistry.get(particleName)).ifPresent(registry ->
                generate(owner, registry));
    }

    /**
     * Stop a particle effect by uuid.
     */
    public static void stop(UUID uuid) {
        Optional.ofNullable(uuidParticles.get(uuid)).ifPresent(ParticleEffect::stop);
        uuidParticles.remove(uuid);
    }

    /**
     * Stop a particle effect by location.
     */
    public static void stop(Location location) {
        Optional.ofNullable(locationParticles.get(location)).ifPresent(ParticleEffect::stop);
        locationParticles.remove(location);
    }
}
