package com.idk.essence.utils.interactiveSlots;

import com.idk.essence.Essence;
import com.idk.essence.utils.Util;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

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
            InteractiveSlot[] slots = getStoredSlots(block);
            if(slots == null) return;
            for(InteractiveSlot slot : slots) {
                if(!InteractiveSlot.activatingSlots.containsValue(slot))
                    slot.generate(slot.getLocation());
            }
        }
    }

    public static void stopInteractiveSlot(Location location) {
        InteractiveSlot slot = InteractiveSlot.activatingSlots.get(location);
        if(slot != null) {
            slot.remove();
            InteractiveSlot.activatingSlots.remove(location);
        }
    }

    @Nullable
    public static InteractiveSlot[] getStoredSlots(Block block) {
        CustomBlockData data = new CustomBlockData(block, plugin);
        return data.has(InteractiveSlot.getInteractiveSlotKey()) ?
                data.get(InteractiveSlot.getInteractiveSlotKey(), InteractiveSlot.getDataType()) :
                null;
    }

    public static InteractiveSlot[] setSlotsAround(Location center, float startYaw, int count, ConfigurationSection section) {
        InteractiveSlot[] slots = new InteractiveSlot[count];
        // turns Minecraft yaw to a regular math angle
        double startYawRadian = Math.toRadians(Util.MathTool.yawToMathDegree(startYaw));
        for(int i = 0; i < count; i++) {
            double radian = 2 * Math.PI * i / count + startYawRadian;
            InteractiveSlot slot = new InteractiveSlot(section);
            slot.generate(center, radian);
            InteractiveSlot.activatingSlots.put(slot.getLocation(), slot);
            slots[i] = slot;
        }
        return slots;
    }

    public static void setContainer(PersistentDataContainer container, InteractiveSlot[] slots) {
        container.set(InteractiveSlot.getInteractiveSlotKey(), InteractiveSlot.getDataType(), slots);
    }

    @EventHandler
    public void onChunkLoaded(ChunkLoadEvent event) {
        setChunkInteractiveSlot(event.getChunk());
    }

}