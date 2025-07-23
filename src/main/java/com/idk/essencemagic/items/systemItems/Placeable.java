package com.idk.essencemagic.items.systemItems;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface Placeable {

    void onBlockPlace(BlockPlaceEvent event);

    void onBlockBreak(BlockBreakEvent event);

    void onBlockRightClick(PlayerInteractEvent event);

}
