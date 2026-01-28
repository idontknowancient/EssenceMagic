package com.idk.essence.utils;

import com.idk.essence.Essence;
import org.bukkit.NamespacedKey;

public class Key {

    private static final Essence plugin = Essence.getPlugin();

    public enum Class {
        /**
         * Custom item identifier. Type: string. Content: internalName.
         */
        ITEM("item-key"),

        /**
         * Artifact identifier. Type: string. Content: internalName.
         */
        ARTIFACT("artifact-key"),

        /**
         * Element identifier. Type: string. Content: internalName.
         */
        ELEMENT("element-key"),

        /**
         * Mob identifier. Type: string. Content: internalName.
         */
        MOB("mob-key"),

        /**
         * Skill identifier. Type: string. Content: internalName.
         */
        SKILL("skill-key"),

        /**
         * Particle identifier. Type: boolean. Content: has particle.
         */
        PARTICLE("particle-key"),

        /**
         * Node identifier. Type: string. Content: nodeType.
         */
        NODE_TYPE("node-type-key"),

        /**
         * UUID in pdc. Type: string. Content: self UUID.
         */
        NODE_SELF("node-self-key"),

        /**
         * Node owner identifier. Type: string. Content: owner UUID.
         */
        NODE_OWNER("node-owner-key"),

        /**
         * Node correlation identifier. Type: string. Content: correlation node UUID.
         */
        NODE_CORRELATION("node-correlation-key"),

        /**
         * Node attachment identifier. Type: boolean. Content: true/false
         */
        NODE_ATTACHMENT("node-attachment-key"),
        ;

        private final NamespacedKey key;

        Class(String keyName) {
            key = new NamespacedKey(plugin, keyName);
        }

        public NamespacedKey get() {
            return key;
        }
    }

    public enum Feature {

        /**
         * Block placeable identifier. Type: boolean. Content: placeable.
         */
        PLACEABLE("placeable-key"),

        /**
         * Item usable identifier. Type: boolean. Content: usable.
         */
        USABLE("usable-key"),
        ;

        private final NamespacedKey key;

        Feature(String keyName) {
            key = new NamespacedKey(plugin, keyName);
        }

        public NamespacedKey get() {
            return key;
        }
    }
}
