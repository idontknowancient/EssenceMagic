package com.idk.essencemagic.items.systemItems;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.items.SystemItem;
import com.idk.essencemagic.utils.particles.Circle;
import com.idk.essencemagic.utils.particles.CustomParticle;
import com.idk.essencemagic.utils.particles.ParticleHandler;
import com.idk.essencemagic.utils.DisplayHandler;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.wands.WandHandler;
import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class WandProcessingTable extends SystemItem implements Placeable, WithParticle {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    @Getter private static final NamespacedKey wandProcessingTableKey = new NamespacedKey(plugin, "wand-processing-table-key");

    boolean displayParticle = ConfigFile.ConfigName.SYSTEM_ITEMS.getBoolean(getName() + ".display-particle");

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
        PersistentDataContainer container = new CustomBlockData(table, plugin);
        container.set(getSystemItemKey(), PersistentDataType.STRING, getName());

        if(isDisplayParticle())
            generateParticle(table.getLocation());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        ParticleHandler.stopParticle(block.getLocation());
        PersistentDataContainer container = new CustomBlockData(event.getBlock(), plugin);

        ItemStack getBack = DisplayHandler.removeItemDisplayFromContainer(container);
        DisplayHandler.removeTextDisplayFromContainer(container);
        if(getBack != null) {
            block.getWorld().dropItemNaturally(block.getLocation(), getBack);
            container.remove(wandProcessingTableKey);
        }
    }

    @Override
    public void onBlockRightClick(PlayerInteractEvent event) {
        event.setCancelled(true);

        if(event.getClickedBlock() == null) return;
        Location location = event.getClickedBlock().getLocation();
        assert location.getWorld() != null;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        PersistentDataContainer container = new CustomBlockData(event.getClickedBlock(), plugin);

        // show item display
        if(!container.has(wandProcessingTableKey) && WandHandler.getWand(item) != null) {
            UUID itemUUID = DisplayHandler.createItemDisplayFromHand(item, location, player, 1.7);
            UUID textUUID = DisplayHandler.createTextDisplayFromItem(item, location, 2.2);
            if(itemUUID == null || textUUID == null) return;

            // set corresponding item and display uuid
            container.set(DisplayHandler.getItemDisplayKey(), DataType.UUID, itemUUID);
            container.set(DisplayHandler.getTextDisplayKey(), DataType.UUID, textUUID);
            container.set(wandProcessingTableKey, PersistentDataType.STRING, getName());
        } else if(container.has(wandProcessingTableKey) && item == null) {
            ItemStack getBack = DisplayHandler.removeItemDisplayFromContainer(container);
            DisplayHandler.removeTextDisplayFromContainer(container);

            player.getInventory().setItemInMainHand(getBack);
            container.remove(wandProcessingTableKey);
        }
    }
    @Override
    public boolean isDisplayParticle() {
        return displayParticle;
    }

    @Override
    public void setDisplayParticle(boolean displayParticle) {
        this.displayParticle = displayParticle;
    }

    @Override
    public void generateParticle(Location location) {
        // create custom particle
        Circle particle = new Circle(location, new Location(location.getWorld(), 0.5, 0.1, 0.5),
                Particle.GLOW, 3,
                8, 0.2, 10, 2);
        CustomParticle.activatingParticles.put(location, particle);
    }
}
