package com.idk.essence.items.artifacts;

import com.idk.essence.items.items.ItemBuilder;
import com.idk.essence.utils.CustomKey;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArtifactBuilder {

    private final ItemBuilder itemBuilder;
    private ItemStack item;
    @Nullable private ConfigurationSection particle;
    @Nullable private ConfigurationSection node;

    public ArtifactBuilder(String materialString) {
        itemBuilder = new ItemBuilder(materialString);
    }

    /**
     * Apply both item key and artifact key.
     */
    public ArtifactBuilder internalName(String internalName) {
        itemBuilder.container(CustomKey.getItemKey(), internalName);
        itemBuilder.container(CustomKey.getArtifactKey(), internalName);
        return this;
    }

    public ArtifactBuilder displayName(Component displayName) {
        itemBuilder.displayName(displayName);
        return this;
    }

    public ArtifactBuilder lore(List<String> lore) {
        itemBuilder.lore(lore);
        return this;
    }

    public ArtifactBuilder glowing(boolean glowing) {
        itemBuilder.glowing(glowing);
        return this;
    }

    /**
     * Whether an item stack can be placed. Only effective for blocks.
     */
    public ArtifactBuilder placeable(boolean placeable) {
        itemBuilder.container(CustomKey.getPlaceableKey(), placeable);
        return this;
    }

    /**
     * Whether an item stack can be used. Only effective for interactable items.
     */
    public ArtifactBuilder usable(boolean usable) {
        itemBuilder.container(CustomKey.getUsableKey(), usable);
        return this;
    }

    /**
     * Build an artifact from the builder.
     * Complete with deep copy.
     * @return the brand new item stack
     */
    public ItemStack build() {
        if(item == null) item = itemBuilder.build();
        return item.clone();
    }
}
