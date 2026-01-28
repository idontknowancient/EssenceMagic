package com.idk.essence.items.artifacts;

import com.idk.essence.items.items.ItemBuilder;
import com.idk.essence.utils.Key;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArtifactBuilder {

    private final ItemBuilder itemBuilder;
    @Nullable @Getter private ConfigurationSection particleSection;
    @Nullable @Getter private ConfigurationSection nodeSection;

    public ArtifactBuilder(String materialString) {
        itemBuilder = new ItemBuilder(materialString);
    }

    /**
     * Apply both item key and artifact key.
     */
    public ArtifactBuilder internalName(String internalName) {
        itemBuilder.container(Key.Class.ITEM.get(), internalName);
        itemBuilder.container(Key.Class.ARTIFACT.get(), internalName);
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
        itemBuilder.container(Key.Feature.PLACEABLE.get(), placeable);
        return this;
    }

    /**
     * Whether an item stack can be used. Only effective for interactable items.
     */
    public ArtifactBuilder usable(boolean usable) {
        itemBuilder.container(Key.Feature.USABLE.get(), usable);
        return this;
    }

    public ArtifactBuilder particle(ConfigurationSection particle) {
        this.particleSection = particle;
        if(particle != null)
            itemBuilder.container(Key.Class.PARTICLE.get(), true);
        return this;
    }

    /**
     * Node data are stored in the entity, so no key needed.
     */
    public ArtifactBuilder node(ConfigurationSection node) {
        this.nodeSection = node;
        return this;
    }

    /**
     * Build an artifact from the builder.
     * Complete with deep copy.
     * @return the brand new item stack
     */
    public ItemStack build() {
        return itemBuilder.build();
    }
}
