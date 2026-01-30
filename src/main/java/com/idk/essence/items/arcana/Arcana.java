package com.idk.essence.items.arcana;

import com.idk.essence.items.items.ItemBuilder;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.configs.EssenceConfig;
import com.idk.essence.utils.placeholders.PlaceholderProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public abstract class Arcana implements PlaceholderProvider {

    protected final ItemBuilder itemBuilder;

    protected Arcana(Builder<?> builder) {
        this.itemBuilder = builder.itemBuilder;
    }

    public ItemStack create() {
        return itemBuilder.build();
    }

    @Override
    public Map<String, String> getPlaceholders() {
        return itemBuilder.getPlaceholders();
    }

    /**
     * Parent return child.
     */
    public abstract static class Builder<T extends Builder<T>> {

        protected String internalName;
        protected final ItemBuilder itemBuilder;

        public Builder(String materialString) {
            itemBuilder = new ItemBuilder(materialString);
        }

        /**
         * Apply both item key and arcana key.
         */
        public T internalName(@NotNull String internalName) {
            this.internalName = internalName;
            itemBuilder.internalName(internalName);
            itemBuilder.container(Key.Type.ARCANA, internalName);
            return self();
        }

        public T displayName(Component displayName) {
            itemBuilder.displayName(displayName);
            return self();
        }

        public T lore(List<String> lore) {
            itemBuilder.lore(lore);
            return self();
        }

        public T glowing(boolean glowing) {
            itemBuilder.glowing(glowing);
            return self();
        }

        public T placeable(boolean placeable) {
            itemBuilder.placeable(placeable);
            return self();
        }

        public T usable(boolean usable) {
            itemBuilder.usable(usable);
            return self();
        }

        public T flag(ConfigurationSection flagSection) {
            itemBuilder.flag(flagSection);
            return self();
        }

        public T recipe(ConfigurationSection recipeSection) {
            itemBuilder.recipe(recipeSection);
            return self();
        }

        protected T fromConfig(EssenceConfig config) {
             return displayName(config.outString(internalName + ".display-name", ""))
                     .lore(config.getStringList(internalName + ".lore"))
                     .glowing(config.getBoolean(internalName + ".glowing", false))
                     .placeable(config.getBoolean(internalName + ".placeable", true))
                     .usable(config.getBoolean(internalName + ".usable", true))
                     .flag(config.getConfigurationSection(internalName + ".options"))
                     .recipe(config.getConfigurationSection(internalName + ".recipe"));
        }

        /**
         * Make parents know who is the child.
         */
        protected abstract T self();
        protected abstract Arcana build();
    }
}
