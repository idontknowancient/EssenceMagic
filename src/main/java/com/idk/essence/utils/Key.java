package com.idk.essence.utils;

import com.idk.essence.Essence;
import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * One key, one value.
 */
public class Key {

    private static final Essence plugin = Essence.getPlugin();

    private interface KeyComponent<T> {

        NamespacedKey getKey();
        PersistentDataType<?, T> getDataType();

        default void set(@Nullable Entity entity, @Nullable T value) {
            if(entity == null || value == null) return;
            entity.getPersistentDataContainer().set(getKey(), getDataType(), value);
        }

        default void set(@Nullable ItemStack item, @Nullable T value) {
            if(item == null || item.getItemMeta() == null || value == null) return;
            item.getItemMeta().getPersistentDataContainer().set(getKey(), getDataType(), value);
        }

        default void set(@Nullable Block block, @Nullable T value) {
            if(block == null || value == null) return;
            new CustomBlockData(block, plugin).set(getKey(), getDataType(), value);
        }

        default void set(@Nullable PersistentDataContainer container, @Nullable T value) {
            if(container == null || value == null) return;
            container.set(getKey(), getDataType(), value);
        }

        default void remove(@Nullable Entity entity) {
            if(entity == null) return;
            entity.getPersistentDataContainer().remove(getKey());
        }

        default void remove(@Nullable ItemStack item) {
            if(item == null || item.getItemMeta() == null) return;
            item.getItemMeta().getPersistentDataContainer().remove(getKey());
        }

        default void remove(@Nullable Block block) {
            if(block == null) return;
            new CustomBlockData(block, plugin).remove(getKey());
        }

        default void remove(@Nullable PersistentDataContainer container) {
            if(container == null) return;
            container.remove(getKey());
        }

        default boolean check(@Nullable Entity entity) {
            if(entity == null) return false;
            return entity.getPersistentDataContainer().has(getKey(), getDataType());
        }

        default boolean check(@Nullable ItemStack item) {
            if(item == null || item.getItemMeta() == null) return false;
            return item.getItemMeta().getPersistentDataContainer().has(getKey(), getDataType());
        }

        default boolean check(@Nullable Block block) {
            if(block == null) return false;
            return new CustomBlockData(block, plugin).has(getKey(), getDataType());
        }

        default boolean check(@Nullable PersistentDataContainer container) {
            if(container == null) return false;
            return container.has(getKey(), getDataType());
        }

        @Nullable
        default T getContent(@Nullable Entity entity) {
            if(entity == null) return null;
            return entity.getPersistentDataContainer().get(getKey(), getDataType());
        }

        @Nullable
        default T getContent(@Nullable ItemStack item) {
            if(item == null || item.getItemMeta() == null) return null;
            return item.getItemMeta().getPersistentDataContainer().get(getKey(), getDataType());
        }

        @Nullable
        default T getContent(@Nullable Block block) {
            if(block == null) return null;
            return new CustomBlockData(block, plugin).get(getKey(), getDataType());
        }

        @Nullable
        default T getContent(@Nullable PersistentDataContainer container) {
            if(container == null) return null;
            return container.get(getKey(), getDataType());
        }

        @NotNull
        default T getContentOrDefault(@Nullable Entity entity, @NotNull T defaultValue) {
            return Optional.ofNullable(getContent(entity)).orElse(defaultValue);
        }

        @NotNull
        default T getContentOrDefault(@Nullable ItemStack item, @NotNull T defaultValue) {
            return Optional.ofNullable(getContent(item)).orElse(defaultValue);
        }

        @NotNull
        default T getContentOrDefault(@Nullable Block block, @NotNull T defaultValue) {
            return Optional.ofNullable(getContent(block)).orElse(defaultValue);
        }

        @NotNull
        default T getContentOrDefault(@Nullable PersistentDataContainer container, @NotNull T defaultValue) {
            return Optional.ofNullable(getContent(container)).orElse(defaultValue);
        }
    }

    public static class Type<T> extends Key implements KeyComponent<T> {

        /**
         * Custom item identifier. Type: string. Content: internalName.
         */
        public static final Type<String> ITEM = new Type<>("item-key", PersistentDataType.STRING);

        /**
         * Artifact identifier. Type: string. Content: internalName.
         */
        public static final Type<String> ARTIFACT = new Type<>("artifact-key", PersistentDataType.STRING);

        /**
         * Element identifier. Type: string. Content: internalName.
         */
        public static final Type<String> ELEMENT = new Type<>("element-key", PersistentDataType.STRING);

        /**
         * Mob identifier. Type: string. Content: internalName.
         */
        public static final Type<String> MOB = new Type<>("mob-key", PersistentDataType.STRING);

        /**
         * Skill identifier. Type: string. Content: internalName.
         */
        public static final Type<String> SKILL = new Type<>("skill-key", PersistentDataType.STRING);

        /**
         * Particle identifier. Type: boolean. Content: has particle.
         */
        public static final Type<Boolean> PARTICLE = new Type<>("particle-key", PersistentDataType.BOOLEAN);

        /**
         * Node identifier. Type: string. Content: nodeType.
         */
        public static final Type<String> NODE_TYPE = new Type<>("node-type-key", PersistentDataType.STRING);

        /**
         * Node self UUID in pdc. Type: UUID. Content: self UUID.
         */
        public static final Type<UUID> NODE_SELF = new Type<>("node-self-key", DataType.UUID);

        /**
         * Node owner identifier. Type: UUID. Content: owner UUID.
         */
        public static final Type<UUID> NODE_OWNER = new Type<>("node-owner-key", DataType.UUID);

        /**
         * Node correlation identifier. Type: UUID. Content: correlation node UUID.
         */
        public static final Type<UUID> NODE_CORRELATION = new Type<>("node-correlation-key", DataType.UUID);

        /**
         * Node attachment identifier. Type: boolean. Content: is attachment.
         */
        public static final Type<Boolean> NODE_ATTACHMENT = new Type<>("node-attachment-key", PersistentDataType.BOOLEAN);

        @Getter private final NamespacedKey key;
        @Getter private final PersistentDataType<?, T> dataType;

        private Type(String key, PersistentDataType<?, T> dataType) {
            this.key = new NamespacedKey(plugin, key);
            this.dataType = dataType;
        }
    }

    public static class Feature<T> extends Key implements KeyComponent<T> {

        /**
         * Block placeable identifier. Type: boolean. Content: placeable.
         */
        public static final Type<Boolean> PLACEABLE = new Type<>("placeable-key", PersistentDataType.BOOLEAN);

        /**
         * Item usable identifier. Type: boolean. Content: usable.
         */
        public static final Type<Boolean> USABLE = new Type<>("usable-key", PersistentDataType.BOOLEAN);

        @Getter private final NamespacedKey key;
        @Getter private final PersistentDataType<?, T> dataType;

        private Feature(String key, PersistentDataType<?, T> dataType) {
            this.key = new NamespacedKey(plugin, key);
            this.dataType = dataType;
        }
    }

    /**
     * Copy container from source to destination.
     */
    public static void copy(PersistentDataContainerView src, PersistentDataContainer dest) {
        for(NamespacedKey key : src.getKeys()) {
            copyTag(src, dest, key, PersistentDataType.STRING);
            copyTag(src, dest, key, PersistentDataType.INTEGER);
            copyTag(src, dest, key, PersistentDataType.DOUBLE);
            copyTag(src, dest, key, PersistentDataType.BOOLEAN);
        }
    }

    private static <T, Z> void copyTag(PersistentDataContainerView src, PersistentDataContainer dest,
                                       NamespacedKey key, PersistentDataType<T, Z> type) {
        if(src.has(key, type)) {
            Z value = src.get(key, type);
            if(value != null) {
                dest.set(key, type, value);
            }
        }
    }
}
