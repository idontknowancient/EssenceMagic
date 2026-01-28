package com.idk.essence.items.artifacts.behaviors;

import com.idk.essence.items.artifacts.ArtifactBehavior;
import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.items.artifacts.ArtifactRegistry;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.nodes.NodeFeature;
import com.idk.essence.utils.particles.ParticleFeature;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

public class WandWorkTableBehavior implements ArtifactBehavior, ParticleFeature, NodeFeature {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        generateParticle(event.getBlockPlaced().getLocation());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        stopParticle(block.getLocation());
        removeNode(block);
    }

    @Override
    public void onBlockInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if(Key.Type.NODE_SELF.getContent(block) == null)
            spawnNode(block, event.getPlayer().getLocation().getYaw(), 3);
        else
            removeNode(block);
    }

    @Override
    public void onItemInteract(PlayerInteractEvent event) {

    }

    @Override
    @Nullable
    public ConfigurationSection getParticleSection() {
        return ArtifactFactory.getParticleSection(ArtifactRegistry.WAND_WORK_TABLE.getInternalName());
    }

    @Override
    @Nullable
    public ConfigurationSection getNodeSection() {
        return ArtifactFactory.getNodeSection(ArtifactRegistry.WAND_WORK_TABLE.getInternalName());
    }
}
