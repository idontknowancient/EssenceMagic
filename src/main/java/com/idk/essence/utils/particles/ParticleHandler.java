package com.idk.essence.utils.particles;

import com.idk.essence.Essence;
import com.idk.essence.items.SystemItem;
import com.idk.essence.items.SystemItemHandler;
import com.idk.essence.items.systemItems.features.WithParticle;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataType;

public class ParticleHandler implements Listener {

    private static final Essence plugin = Essence.getPlugin();

    public static void initialize() {
        for(CustomParticle particle : CustomParticle.activatingParticles.values())
            particle.stop();
        CustomParticle.activatingParticles.clear();
        setParticles();
    }

    // find all custom block data and generate particles
    private static void setParticles() {
        for(World world : Bukkit.getWorlds()) {
            for(Chunk chunk : world.getLoadedChunks()) {
                setChunkParticle(chunk);
            }
        }
    }

    private static void setChunkParticle(Chunk chunk) {
        for(Block block : CustomBlockData.getBlocksWithCustomData(plugin, chunk)) {
            CustomBlockData data = new CustomBlockData(block, plugin);

            // handle particles by SystemItem
            String name = data.get(SystemItem.getSystemItemKey(), PersistentDataType.STRING);
            SystemItem systemItem = SystemItemHandler.getSystemItem(name);
            if(systemItem instanceof WithParticle block_ && block_.isDisplayParticle()
                    && !CustomParticle.activatingParticles.containsKey(block.getLocation())) {
                block_.generateParticle(block.getLocation());
            }
        }
    }

    public static void stopParticle(Location location) {
        CustomParticle particle = CustomParticle.activatingParticles.get(location);
        if(particle != null) {
            particle.stop();
            CustomParticle.activatingParticles.remove(location);
        }
    }

    @EventHandler
    public void onChunkLoaded(ChunkLoadEvent event) {
        setChunkParticle(event.getChunk());
    }

}
