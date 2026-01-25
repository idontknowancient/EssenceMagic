package com.idk.essence.items;

import com.idk.essence.Essence;
import com.idk.essence.items.systemItems.features.Placeable;
import com.idk.essence.utils.ClickHandler;
import com.idk.essence.utils.Registry;
import com.idk.essence.utils.configs.ConfigManager;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class SystemItemHandler implements Listener {

    public static void initialize() {
        SystemItem.systemItems.clear();
        setSystemItems();
    }

    private static void setSystemItems() {
        Set<String> systemItemSet = ConfigManager.DefaultFile.ARTIFACTS.getConfig().getKeys(false);
        for(Registry.SystemItem registry : Registry.SystemItem.values()) {
            String name = registry.name().toLowerCase();
            if(systemItemSet.contains(name)) {
                // use Function to automatically generate SystemItem from a string
                SystemItem.systemItems.put(name, registry.constructor.apply(name));
            }
        }
    }

    @Nullable
    public static SystemItem getSystemItem(ItemStack item) {
        if(!isSystemItem(item)) return null;
        assert item.getItemMeta() != null;
        String name = item.getItemMeta().getPersistentDataContainer().get(SystemItem.getSystemItemKey(), PersistentDataType.STRING);
        return getSystemItem(name);
    }

    @Nullable
    public static SystemItem getSystemItem(String name) {
        return SystemItem.systemItems.get(name);
    }

    public static boolean isSystemItem(ItemStack item) {
        return item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(SystemItem.getSystemItemKey());
    }

    // handle system item click
    @EventHandler
    public void onItemRightClick(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Action action = event.getAction();
        SystemItem systemItem = getSystemItem(item);
        if(systemItem == null) return;

        if(!systemItem.isUsable()) event.setCancelled(true);
        if(action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR))
            systemItem.onItemRightClick(event);
    }

    // handle system item place
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        SystemItem systemItem = getSystemItem(item);
        if(!(systemItem instanceof Placeable block)) return;

        if(!systemItem.isPlaceable()) event.setCancelled(true);
        block.onBlockPlace(event);
    }

    // handle system item break
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        // cancel multiple conditions like holding a wand
        if(ClickHandler.shouldCancelLeft(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        PersistentDataContainer container = new CustomBlockData(block, Essence.getPlugin());
        String name = container.get(SystemItem.getSystemItemKey(), PersistentDataType.STRING);
        if(name == null) return;

        SystemItem systemItem = getSystemItem(name);
        if(!(systemItem instanceof Placeable block_)) return;

        event.setDropItems(false);
        block.getWorld().dropItemNaturally(block.getLocation(), systemItem.getItemStack());
        block_.onBlockBreak(event);
    }

    // handle right click after a system item was placed
    @EventHandler
    public void onBlockRightClick(PlayerInteractEvent event) {
        Block table = event.getClickedBlock();
        Action action = event.getAction();
        if(table == null) return;
        if(action != Action.RIGHT_CLICK_BLOCK) return;

        PersistentDataContainer container = new CustomBlockData(table, Essence.getPlugin());
        SystemItem systemItem = getSystemItem(container.get(SystemItem.getSystemItemKey(), PersistentDataType.STRING));
        if(systemItem instanceof Placeable block)
            block.onBlockRightClick(event);
    }

}
