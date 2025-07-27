package com.idk.essencemagic.items.systemItems;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.items.SystemItem;
import com.idk.essencemagic.particles.Circle;
import com.idk.essencemagic.particles.CustomParticle;
import com.idk.essencemagic.particles.ParticleHandler;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.wands.WandHandler;
import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import org.bukkit.Bukkit;
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
import org.bukkit.util.Transformation;

import java.util.Optional;
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
        ParticleHandler.stopParticle(event.getBlock().getLocation());
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
            ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
            display.setItemStack(item);
            player.getInventory().setItemInMainHand(null);
            display.setRotation(0, 0);

            Transformation transformation = display.getTransformation();
            transformation.getTranslation().set(0.5, 1.7, 0.5);
            transformation.getScale().set(0.5, 0.5, 0.5);
            display.setTransformation(transformation);
            display.setBillboard(Display.Billboard.FIXED);

            // set corresponding item and display uuid
            assert item != null;
            container.set(wandProcessingTableKey, DataType.ITEM_STACK, item);
            container.set(wandProcessingTableKey, DataType.UUID, display.getUniqueId());
        } else if(container.has(wandProcessingTableKey) && item == null) {
//            player.getInventory().setItemInMainHand(container.get(wandProcessingTableKey, DataType.ITEM_STACK));
            UUID displayID = container.get(wandProcessingTableKey, DataType.UUID);
            if(displayID == null) return;

//            Optional.ofNullable(Bukkit.getEntity(displayID)).ifPresent(Entity::remove);
            Entity entity = Bukkit.getEntity(displayID);
            if(!(entity instanceof ItemDisplay display)) return;
            entity.remove();
            player.getInventory().setItemInMainHand(display.getItemStack());
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
