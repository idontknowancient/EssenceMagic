package com.idk.essence.items.systemItems;

import com.idk.essence.items.systemItems.features.SIParticleBlock;
import com.idk.essence.utils.DisplayHandler;
import com.idk.essence.utils.configs.ConfigFile;
import com.idk.essence.utils.interactiveSlots.InteractiveSlot;
import com.idk.essence.utils.interactiveSlots.InteractiveSlotHandler;
import com.idk.essence.items.wands.Wand;
import com.idk.essence.items.wands.WandHandler;
import com.jeff_media.customblockdata.CustomBlockData;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class WandProcessingTable extends SIParticleBlock {

    @Getter private static final NamespacedKey wandProcessingTableKey = new NamespacedKey(getPlugin(), "wand-processing-table-key");

    public WandProcessingTable(String name) {
        super(name);
    }

    @Override
    protected NamespacedKey getCustomKey() {
        return wandProcessingTableKey;
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
        if(!container.has(wandProcessingTableKey) && wand != null) {
            // create and set container
            DisplayHandler.createItemDisplayFromHand(item, block, container, player, 1.7);
            DisplayHandler.createTextDisplayFromItem(item, block, container, 2.2);

            container.set(wandProcessingTableKey, PersistentDataType.STRING, getName());

            InteractiveSlot[] slots = InteractiveSlotHandler.setSlotsAround(
                    block.getLocation(), player.getLocation().getYaw(), WandHandler.getSlot(item),
                    ConfigFile.ConfigName.SYSTEM_ITEMS.getConfigurationSection(getName() + ".slot"));
            InteractiveSlotHandler.setContainer(container, slots);
        } else if(container.has(wandProcessingTableKey) && item == null) {
            // remove and remove container
            ItemStack getBack = DisplayHandler.removeItemDisplayFromContainer(container);
            DisplayHandler.removeTextDisplayFromContainer(container);

            player.getInventory().setItemInMainHand(getBack);

            InteractiveSlot[] slots = InteractiveSlotHandler.getStoredSlots(block);
            if(slots != null) {
                for(InteractiveSlot slot : slots) {
                    InteractiveSlotHandler.stopInteractiveSlot(slot.getLocation());
                }
            }

            container.remove(wandProcessingTableKey);
            container.remove(InteractiveSlot.getInteractiveSlotKey());
        }
    }
}