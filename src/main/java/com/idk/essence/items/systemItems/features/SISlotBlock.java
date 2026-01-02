package com.idk.essence.items.systemItems.features;

import com.idk.essence.items.wands.Wand;
import com.idk.essence.items.wands.WandHandler;
import com.idk.essence.utils.DisplayHandler;
import com.idk.essence.utils.configs.ConfigFile;
import com.idk.essence.utils.interactiveSlots.InteractiveSlot;
import com.idk.essence.utils.interactiveSlots.InteractiveSlotHandler;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class SISlotBlock extends SIParticleBlock implements WithInteractiveSlot {

    protected SISlotBlock(String itemName) {
        super(itemName);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        super.onBlockBreak(event);

        // stop all interactive slots
        InteractiveSlotHandler.stopSlotsAround(event.getBlock());
    }

    @Override
    public void onBlockRightClick(PlayerInteractEvent event) {
        event.setCancelled(true);

        Block block = event.getClickedBlock();
        if(block == null) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Wand wand = WandHandler.getWand(item);
        PersistentDataContainer container = new CustomBlockData(block, getPlugin());

        // show item display
        if(!container.has(getCustomKey()) && wand != null) {
            // create and set container
            DisplayHandler.createItemDisplayFromHand(item, block, container, player, 1.7);
            DisplayHandler.createTextDisplayFromItem(item, block, container, 2.2);

            container.set(getCustomKey(), PersistentDataType.STRING, getName());

            // generate interactive slots and set container
            generateSlotsAround(
                    block.getLocation(), player.getLocation().getYaw(),
                    WandHandler.getSlot(item));
        } else if(container.has(getCustomKey()) && item == null) {
            // remove and remove container
            ItemStack getBack = DisplayHandler.removeItemDisplayFromContainer(container);
            DisplayHandler.removeTextDisplayFromContainer(container);

            player.getInventory().setItemInMainHand(getBack);

            InteractiveSlotHandler.stopSlotsAround(block);

            container.remove(getCustomKey());
            container.remove(InteractiveSlot.getInteractiveSlotKey());
        }
    }

    protected abstract NamespacedKey getCustomKey();

    // also set container
    @Override
    public void generateSlotsAround(Location center, float startYaw, int count) {
        ConfigurationSection section = ConfigFile.ConfigName.SYSTEM_ITEMS.getConfigurationSection(getName() + ".slot");
        PersistentDataContainer container = new CustomBlockData(center.getBlock(), getPlugin());
        InteractiveSlotHandler.setSlotsAround(center, startYaw, count, section, container);
    }

    @Override
    public void rebuildSlotsAround(Location center) {
        ConfigurationSection section = ConfigFile.ConfigName.SYSTEM_ITEMS.getConfigurationSection(getName() + ".slot");
        PersistentDataContainer container = new CustomBlockData(center.getBlock(), getPlugin());
        InteractiveSlotHandler.rebuildSlotsAround(center, section, container);
    }
}
