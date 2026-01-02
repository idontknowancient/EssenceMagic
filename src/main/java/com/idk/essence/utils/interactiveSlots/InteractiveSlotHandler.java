package com.idk.essence.utils.interactiveSlots;

import com.idk.essence.Essence;
import com.idk.essence.items.SystemItem;
import com.idk.essence.items.SystemItemHandler;
import com.idk.essence.items.systemItems.features.WithInteractiveSlot;
import com.idk.essence.utils.Util;
import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class InteractiveSlotHandler implements Listener {

    private static final Essence plugin = Essence.getPlugin();

    public static void initialize() {
        // cannot use stopInteractiveSlot (Concurrent??Exception)
        for(InteractiveSlot slot : InteractiveSlot.activatingSlots.values())
            slot.remove();
        InteractiveSlot.activatingSlots.clear();
        setInteractiveSlots();
    }

    // find all custom block data and generate particles
    private static void setInteractiveSlots() {
        for(World world : Bukkit.getWorlds()) {
            for(Chunk chunk : world.getLoadedChunks()) {
                setChunkInteractiveSlot(chunk);
            }
        }
    }

    private static void setChunkInteractiveSlot(Chunk chunk) {
        for(Block block : CustomBlockData.getBlocksWithCustomData(plugin, chunk)) {
            CustomBlockData data = new CustomBlockData(block, plugin);

            // handle slots by SystemItem
            String name = data.get(SystemItem.getSystemItemKey(), PersistentDataType.STRING);
            SystemItem systemItem = SystemItemHandler.getSystemItem(name);
            if(!(systemItem instanceof WithInteractiveSlot slot_)) return;

            Location location = block.getLocation();
            if(data.has(InteractiveSlot.getInteractiveSlotKey()))
                slot_.rebuildSlotsAround(location);
        }
    }

    public static void stopSlotsAround(Block block) {
        CustomBlockData data = new CustomBlockData(block, plugin);
        Location[] locations = data.get(InteractiveSlot.getInteractiveSlotKey(), DataType.LOCATION_ARRAY);
        if(locations == null) return;
        for(Location location : locations) {
            stopInteractiveSlot(location);
        }
    }

    public static void stopInteractiveSlot(Location location) {
        InteractiveSlot slot = InteractiveSlot.activatingSlots.get(location);
        if(slot != null) {
            slot.remove();
            InteractiveSlot.activatingSlots.remove(location);
        }
    }

    // also set container
    public static void setSlotsAround(Location center, double startYaw, int count, ConfigurationSection section, PersistentDataContainer container) {
        Location[] locations = new Location[count];
        // turns Minecraft yaw to a regular math angle
        double startYawRadian = Math.toRadians(Util.yawToMathDegree(startYaw));
        for(int i = 0; i < count; i++) {
            double radian = 2 * Math.PI * i / count + startYawRadian;
            InteractiveSlot slot = new InteractiveSlot(section);
            slot.generate(center, radian);
            locations[i] = slot.getLocation();
        }
        container.set(InteractiveSlot.getInteractiveSlotKey(), DataType.LOCATION_ARRAY, locations);
        container.set(InteractiveSlot.getInteractiveSlotKey(), PersistentDataType.DOUBLE, startYaw);
        container.set(InteractiveSlot.getInteractiveSlotKey(), PersistentDataType.INTEGER, count);
    }

    public static void rebuildSlotsAround(Location center, ConfigurationSection section, PersistentDataContainer container) {
        Location[] locations = container.get(InteractiveSlot.getInteractiveSlotKey(), DataType.LOCATION_ARRAY);
        Double startYaw = container.get(InteractiveSlot.getInteractiveSlotKey(), PersistentDataType.DOUBLE);
        Integer count = container.get(InteractiveSlot.getInteractiveSlotKey(), PersistentDataType.INTEGER);
        if(locations == null || startYaw == null || count == null) return;
        setSlotsAround(center, startYaw, count, section, container);
    }

    @EventHandler
    public void onChunkLoaded(ChunkLoadEvent event) {
        setChunkInteractiveSlot(event.getChunk());
    }

}
