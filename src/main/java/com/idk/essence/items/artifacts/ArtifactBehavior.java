package com.idk.essence.items.artifacts;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface ArtifactBehavior {

    void onBlockPlace(BlockPlaceEvent event);

    void onBlockBreak(BlockBreakEvent event);

    void onBlockInteract(PlayerInteractEvent event);

    void onItemInteract(PlayerInteractEvent event);
}
