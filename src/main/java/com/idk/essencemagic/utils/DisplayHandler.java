package com.idk.essencemagic.utils;

import com.idk.essencemagic.EssenceMagic;
import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DisplayHandler {

    @Getter private static final NamespacedKey itemDisplayKey = new NamespacedKey(EssenceMagic.getPlugin(), "item-display-key");
    @Getter private static final NamespacedKey textDisplayKey = new NamespacedKey(EssenceMagic.getPlugin(), "text-display-key");

    @Nullable
    public static UUID createItemDisplayFromHand(ItemStack item, Location originalLocation, Player player, double yOffset, double scale) {
        if(originalLocation.getWorld() == null) return null;
        ItemDisplay display = (ItemDisplay) originalLocation.getWorld().spawnEntity(originalLocation.clone().add(0.5, yOffset, 0.5), EntityType.ITEM_DISPLAY);
        display.setItemStack(item);
        player.getInventory().setItemInMainHand(null);
        display.setRotation(0, 0);

        Transformation transformation = display.getTransformation();
        transformation.getScale().set(scale);
        display.setTransformation(transformation);
        display.setBillboard(Display.Billboard.CENTER);

        return display.getUniqueId();
    }

    @Nullable
    public static UUID createItemDisplayFromHand(ItemStack item, Location originalLocation, Player player, double yOffset) {
        return createItemDisplayFromHand(item, originalLocation, player, yOffset, 0.5);
    }

    @Nullable
    public static UUID createTextDisplayFromItem(ItemStack item, Location originalLocation, double yOffset, double scale) {
        if(originalLocation.getWorld() == null || item.getItemMeta() == null) return null;
        TextDisplay display = (TextDisplay) originalLocation.getWorld().spawnEntity(originalLocation.clone().add(0.5, yOffset, 0.5), EntityType.TEXT_DISPLAY);
        display.setText(item.getItemMeta().getDisplayName());

        Transformation transformation = display.getTransformation();
        transformation.getScale().set(scale);
        display.setTransformation(transformation);
        display.setBillboard(Display.Billboard.CENTER);

        return display.getUniqueId();
    }

    @Nullable
    public static UUID createTextDisplayFromItem(ItemStack item, Location originalLocation, double yOffset) {
        return createTextDisplayFromItem(item, originalLocation, yOffset, 1);
    }

    @Nullable
    public static ItemStack removeItemDisplayFromContainer(PersistentDataContainer container) {
        if(!(getDisplay(container, itemDisplayKey) instanceof ItemDisplay display)) return null;
        display.remove();
        container.remove(itemDisplayKey);

        return display.getItemStack();
    }

    @Nullable
    public static String removeTextDisplayFromContainer(PersistentDataContainer container) {
        if(!(getDisplay(container, textDisplayKey) instanceof TextDisplay display)) return null;
        display.remove();
        container.remove(textDisplayKey);

        return display.getText();
    }

    @Nullable
    public static Display getDisplay(PersistentDataContainer container, NamespacedKey key) {
        UUID displayID = container.get(key, DataType.UUID);
        if(displayID == null) return null;
        Entity entity = Bukkit.getEntity(displayID);
        return entity instanceof Display display ? display : null;
    }

}
