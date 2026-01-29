package com.idk.essence.utils.nodes;

import com.idk.essence.utils.nodes.types.ActionNode;
import com.idk.essence.utils.nodes.types.ItemNode;
import com.idk.essence.utils.nodes.types.TextNode;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Function;

public enum NodeRegistry {

    ITEM("item", ItemDisplay.class, ItemNode::new),
    ACTION("action", Interaction.class, ActionNode::new),
    TEXT("text", TextDisplay.class, TextNode::new),
    ;

    @Getter private final String name;
    @Getter private final Class<? extends Entity> entityClass;
    @Getter private final Function<Location, BaseNode> constructor;

    NodeRegistry(String name, Class<? extends Entity> entityClass,  Function<Location, BaseNode> constructor) {
        this.name = name;
        this.entityClass = entityClass;
        this.constructor = constructor;
    }

    @Nullable
    public static NodeRegistry get(String name) {
        if(name == null) return null;
        try {
            return NodeRegistry.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get all entity classes that will be used as the entity of the node.
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends Entity>[] getEntityClasses() {
        return Arrays.stream(NodeRegistry.values())
                .map(NodeRegistry::getEntityClass)
                .toArray(Class[]::new);
    }
}
