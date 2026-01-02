package com.idk.essence.utils;

import com.idk.essence.Essence;
import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DisplayHandler {

    private static final Essence plugin = Essence.getPlugin();

    @Getter private static final NamespacedKey itemDisplayKey = new NamespacedKey(Essence.getPlugin(), "item-display-key");
    @Getter private static final NamespacedKey textDisplayKey = new NamespacedKey(Essence.getPlugin(), "text-display-key");

    public static void createItemDisplayFromHand(ItemStack item, Block block, PersistentDataContainer container, Player player, double yOffset, double scale) {
        Location location = block.getLocation();
        if(location.getWorld() == null) return;

        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5, yOffset, 0.5), EntityType.ITEM_DISPLAY);
        display.setItemStack(item);
        player.getInventory().setItemInMainHand(null);
        display.setRotation(0, 0);

        Transformation transformation = display.getTransformation();
        transformation.getScale().set(scale);
        display.setTransformation(transformation);
        display.setBillboard(Display.Billboard.CENTER);

        container.set(itemDisplayKey, DataType.UUID, display.getUniqueId());
    }

    public static void createItemDisplayFromHand(ItemStack item, Block block, PersistentDataContainer container, Player player, double yOffset) {
        createItemDisplayFromHand(item, block, container, player, yOffset, 0.5);
    }

    public static void createTextDisplayFromItem(ItemStack item, Block block, PersistentDataContainer container, double yOffset, double scale) {
        Location location = block.getLocation();
        if(location.getWorld() == null || item.getItemMeta() == null) return;

        TextDisplay display = (TextDisplay) location.getWorld().spawnEntity(location.clone().add(0.5, yOffset, 0.5), EntityType.TEXT_DISPLAY);
        display.setText(item.getItemMeta().getDisplayName());

        Transformation transformation = display.getTransformation();
        transformation.getScale().set(scale);
        display.setTransformation(transformation);
        display.setBillboard(Display.Billboard.CENTER);

        container.set(textDisplayKey, DataType.UUID, display.getUniqueId());
    }

    public static void createTextDisplayFromItem(ItemStack item, Block block, PersistentDataContainer container, double yOffset) {
        createTextDisplayFromItem(item, block, container, yOffset, 1);
    }

    @Nullable
    public static ItemStack removeItemDisplayFromContainer(PersistentDataContainer container) {
        if(!(getDisplay(container, itemDisplayKey) instanceof ItemDisplay display)) return null;
        display.remove();
        container.remove(itemDisplayKey);

        return display.getItemStack();
    }

    public static void removeTextDisplayFromContainer(PersistentDataContainer container) {
        if(!(getDisplay(container, textDisplayKey) instanceof TextDisplay display)) return;
        display.remove();
        container.remove(textDisplayKey);
    }

    @Nullable
    public static Display getDisplay(PersistentDataContainer container, NamespacedKey key) {
        UUID displayID = container.get(key, DataType.UUID);
        if(displayID == null) return null;
        Entity entity = Bukkit.getEntity(displayID);
        return entity instanceof Display display ? display : null;
    }

}
