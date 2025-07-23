package com.idk.essencemagic.items.systemItems;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.items.SystemItem;
import com.idk.essencemagic.particles.Circle;
import com.idk.essencemagic.particles.CustomParticle;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class WandProcessingTable extends SystemItem implements Placeable {

    public WandProcessingTable(String name) {
        super(name);
    }

    @Override
    public void onItemRightClick(PlayerInteractEvent event) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        Block table = event.getBlockPlaced();
        // import external repository to store block data
        PersistentDataContainer container = new CustomBlockData(table, EssenceMagic.getPlugin());
        container.set(getSystemItemKey(), PersistentDataType.STRING, getName());

        // create custom particle
        Circle particle = new Circle(table.getLocation(), Particle.ENCHANTMENT_TABLE, 3,
                16, 0.15, 5, 2);
        CustomParticle.activatingParticles.put(table.getLocation(), particle);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        // stop particle
        CustomParticle.activatingParticles.get(event.getBlock().getLocation()).stop();
        CustomParticle.activatingParticles.remove(event.getBlock().getLocation());
    }

    @Override
    public void onBlockRightClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        event.getPlayer().sendMessage("666");
    }
}
