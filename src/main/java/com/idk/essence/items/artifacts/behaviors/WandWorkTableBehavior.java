package com.idk.essence.items.artifacts.behaviors;

import com.idk.essence.items.artifacts.ArtifactBehavior;
import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.utils.particles.ParticleFeature;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

public class WandWorkTableBehavior implements ArtifactBehavior, ParticleFeature {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        generateParticle(event.getBlock().getLocation());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        stopParticle(event.getBlock().getLocation());
    }

    @Override
    public void onBlockInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onItemInteract(PlayerInteractEvent event) {

    }

    @Override
    @Nullable
    public ConfigurationSection getParticleSection() {
        return ArtifactFactory.getParticleSection("wand_work_table");
    }
}
