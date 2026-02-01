package com.idk.essence.utils.nodes;

import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.nodes.types.ItemNode;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public interface NodeFeature {

    @Nullable ConfigurationSection getNodeSection();

    /**
     * Spawn one node in the center by the config section.
     */
    default void spawnNode(UUID ownerUUID, Location center) {
        spawnNode(ownerUUID, center, 0, 1);
    }

    /**
     * Spawn one node in the center by the config section.
     */
    default void spawnNode(UUID ownerUUID, Location center, double startYaw) {
        spawnNode(ownerUUID, center, startYaw, 1);
    }

    /**
     * Spawn specific amount of nodes surrounding the center by the config section.
     * Automatically set UUID for this block to identify the owner.
     */
    default void spawnNode(Block block, double startYaw, int amount) {
        UUID ownerUUID = UUID.randomUUID();
        Key.Type.NODE_SELF.set(block, ownerUUID);
        spawnNode(ownerUUID, block.getLocation(), startYaw, amount);
    }

    default void spawnItemNode(@Nullable UUID ownerUUID, @Nullable ItemStack item, @Nullable Location location,
                               boolean particleVisible, Color particleColor, boolean interactable, boolean textDisplayable) {
        if(ownerUUID == null || item == null || location == null) return;
        ItemNode node = (ItemNode) NodeRegistry.ITEM.getConstructor().apply(location);
        node.setOwnerUUID(ownerUUID);
        node.setItem(item);
        node.setParticleAttribute(particleVisible, particleColor);
        node.setInteractable(interactable);
        node.setTextDisplayable(textDisplayable);
        node.spawn();
    }

    /**
     * Spawn specific amount of nodes surrounding the center by the config section.
     */
    default void spawnNode(UUID ownerUUID, Location center, double startYaw, int amount) {
        if(getNodeSection() == null) return;
        Set<String> keys = getNodeSection().getKeys(false);
        // Can be multiple nodes
        for(String key : keys) {
            boolean enabled = getNodeSection().getBoolean(key + ".enabled", true);
            NodeRegistry type = NodeRegistry.get(key);
            if(type == null || !enabled) continue;

            boolean visible = getNodeSection().getBoolean(key + ".visible", true);
            Color color = Util.System.stringToColor(getNodeSection().getString(key + ".color", "yellow"));
            int radius = getNodeSection().getInt(key + ".radius", 1);
            double startRadian = Math.toRadians(Util.Tool.yawToMathDegree(startYaw));
            Vector offset = Util.Tool.getVectorFromList(getNodeSection().getDoubleList(key + ".offset"));

            // Scatter in the arc
            for(int i = 0; i < amount; i++) {
                double radian = 2 * Math.PI * i / amount + startRadian;
                Location location = center.clone().add(offset)
                        .add(radius * Math.sin(radian), 0, radius * Math.cos(radian));

                BaseNode node = type.getConstructor().apply(location);
                // To have entities able to reconnect to nodes
                node.setOwnerUUID(ownerUUID);
                node.setParticleAttribute(visible, color);
                node.spawn();
            }
        }
    }

    /**
     * Remove all nodes created by its owner.
     * Automatically get UUID in the block.
     */
    default void removeNode(Block block) {
        removeNode(Key.Type.NODE_SELF.getContent(block));
        Key.Type.NODE_SELF.remove(block);
    }

    /**
     * Remove all nodes created by its owner.
     */
    default void removeNode(UUID ownerUUID) {
        NodeManager.removeAll(ownerUUID);
    }
}
