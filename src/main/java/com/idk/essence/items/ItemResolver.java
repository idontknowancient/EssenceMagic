package com.idk.essence.items;

import com.idk.essence.items.arcana.ArcanaFactory;
import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.items.items.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class ItemResolver {

    /**
     * Get specific item among arcana, artifacts, and items.
     */
    @Nullable
    public static ItemStack get(String internalName) {
        ItemStack item = ArcanaFactory.getItem(internalName);
        if(item == null) item = ArtifactFactory.getArtifact(internalName);
        if(item == null) item = ItemFactory.get(internalName);
        return item;
    }

    /**
     * Get all keys among arcana, artifacts, and items.
     */
    public static Collection<String> getAllKeys() {
        Collection<String> keys = new ArrayList<>();
        keys.addAll(ArcanaFactory.getAllKeys());
        keys.addAll(ArtifactFactory.getAllActivateKeys());
        keys.addAll(ItemFactory.getAllKeys());
        return keys;
    }

    /**
     * Get all items among arcana, artifacts, and items.
     */
    public static Collection<ItemStack> getAllItems() {
        Collection<ItemStack> items = new ArrayList<>();
        items.addAll(ArcanaFactory.getAllItems());
        items.addAll(ArtifactFactory.getAllActivateItems());
        items.addAll(ItemFactory.getAll());
        return items;
    }
}
