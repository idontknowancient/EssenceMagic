package com.idk.essencemagic.items.systemItems.features;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.items.SystemItem;
import com.idk.essencemagic.utils.DisplayHandler;
import com.idk.essencemagic.utils.Registry;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.particles.CustomParticle;
import com.idk.essencemagic.utils.particles.ParticleHandler;
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

    @Getter private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    boolean displayParticle = ConfigFile.ConfigName.SYSTEM_ITEMS.getBoolean(getName() + ".particle.display");

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

        ItemStack getBack = DisplayHandler.removeItemDisplayFromContainer(container);
        DisplayHandler.removeTextDisplayFromContainer(container);
        if(getBack != null) {
            block.getWorld().dropItemNaturally(block.getLocation(), getBack);
            container.remove(getCustomKey());
        }
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
        ConfigurationSection section = ConfigFile.ConfigName.SYSTEM_ITEMS.getConfigurationSection(getName() + ".particle");
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
