package com.idk.essence.items.systemItems.features;

import com.idk.essence.Essence;
import com.idk.essence.items.SystemItem;
import com.idk.essence.utils.DisplayHandler;
import com.idk.essence.utils.Registry;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.particles.CustomParticle;
import com.idk.essence.utils.particles.ParticleHandler;
import com.jeff_media.customblockdata.CustomBlockData;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class SIParticleBlock extends SystemItem implements Placeable, WithParticle {

    @Getter private static final Essence plugin = Essence.getPlugin();

    boolean displayParticle = ConfigManager.ConfigDefaultFile.SYSTEM_ITEMS.getBoolean(getName() + ".particle.display");

    protected SIParticleBlock(String itemName) {
        super(itemName);
    }

    protected abstract NamespacedKey getCustomKey();

    @Override
    public void onItemRightClick(PlayerInteractEvent event) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        // import external repository to store block data
        PersistentDataContainer container = new CustomBlockData(block, plugin);
        container.set(getSystemItemKey(), PersistentDataType.STRING, getName());

        if(isDisplayParticle())
            generateParticle(block.getLocation());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        PersistentDataContainer container = new CustomBlockData(block, plugin);
        container.remove(getSystemItemKey());
        container.remove(getCustomKey());

        ItemStack getBack = DisplayHandler.removeItemDisplayFromContainer(container);
        DisplayHandler.removeTextDisplayFromContainer(container);

        // get the item back from the item display
        if(getBack != null)
            block.getWorld().dropItemNaturally(block.getLocation(), getBack);
        // stop the particle if activated
        if(isDisplayParticle())
            ParticleHandler.stopParticle(block.getLocation());
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
        ConfigurationSection section = ConfigManager.ConfigDefaultFile.SYSTEM_ITEMS.getConfigurationSection(getName() + ".particle");
        Registry.CustomParticle particle;
        try {
            particle = Registry.CustomParticle.valueOf(section.getString("shape", "circle").toUpperCase());
        } catch (IllegalArgumentException e) {
            particle = Registry.CustomParticle.CIRCLE;
        }

        // create custom particle
        CustomParticle activating = particle.constructor.apply(section);
        activating.generate(location);
    }
}
