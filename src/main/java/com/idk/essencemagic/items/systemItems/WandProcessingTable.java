package com.idk.essencemagic.items.systemItems;

import com.idk.essencemagic.items.systemItems.features.SIParticleBlock;
import com.idk.essencemagic.utils.DisplayHandler;
import com.idk.essencemagic.wands.WandHandler;
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

        if(event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        PersistentDataContainer container = new CustomBlockData(event.getClickedBlock(), getPlugin());

        // show item display
        if(!container.has(wandProcessingTableKey) && WandHandler.getWand(item) != null) {
            // create and set container
            DisplayHandler.createItemDisplayFromHand(item, block, player, 1.7);
            DisplayHandler.createTextDisplayFromItem(item, block, 2.2);

            container.set(wandProcessingTableKey, PersistentDataType.STRING, getName());
        } else if(container.has(wandProcessingTableKey) && item == null) {
            // remove and remove container
            ItemStack getBack = DisplayHandler.removeItemDisplayFromContainer(container);
            DisplayHandler.removeTextDisplayFromContainer(container);

            player.getInventory().setItemInMainHand(getBack);
            container.remove(wandProcessingTableKey);
        }
    }
}
