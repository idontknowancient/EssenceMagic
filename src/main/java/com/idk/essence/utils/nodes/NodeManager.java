package com.idk.essence.utils.nodes;

import com.idk.essence.Essence;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.nodes.types.ActionNode;
import com.idk.essence.utils.nodes.types.ItemNode;
import com.idk.essence.utils.nodes.types.TextNode;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class NodeManager implements Listener {

    /**
     * Used to register listener
     */
    @Getter private static final NodeManager instance = new NodeManager();

    /**
     * Store all active nodes. Use selfUUID as key.
     */
    private static final Map<UUID, BaseNode> activeNodes = new LinkedHashMap<>();
    private static final List<BukkitTask> tasks = new ArrayList<>();


    private NodeManager() {}

    public static void initialize() {
        shutdown();
        activateNodes();
        lifetimeManager();
    }

    public static void shutdown() {
        unloadAll(node -> true);
        activeNodes.clear();
        tasks.stream().filter(task -> !task.isCancelled()).forEach(BukkitTask::cancel);
        tasks.clear();
    }

    private static void activateNodes() {
        for(World world : Bukkit.getWorlds()) {
            for(Entity entity : world.getEntitiesByClasses(NodeRegistry.getEntityClasses())) {
                activateNode(entity);
            }
        }
        correlateNodes();
    }

    private static void lifetimeManager() {
        int frequency = 5;
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Essence.getPlugin(), () -> removeAll(node -> {
            node.updateLifetime(frequency);
            return node.isExpired();
        }), 0L, frequency);
        tasks.add(task);
    }

    /**
     * Recreate nodes by the entity.
     */
    @EventHandler
    public void onEntityLoad(EntitiesLoadEvent event) {
        for(Entity entity : event.getEntities()) {
            activateNode(entity);
        }
        correlateNodes();
    }

    /**
     * Unload nodes when entities are unloaded to save resources.
     */
    @EventHandler
    public void onEntityUnload(EntitiesUnloadEvent event) {
        for(Entity entity : event.getEntities()) {
            if(!hasNode(entity)) continue;
            unload(entity);
        }
    }

    private static void activateNode(Entity entity) {
        if(!hasNode(entity) || hasKey(Key.Type.NODE_SELF.getContent(entity))) return;
        BaseNode node = recreate(entity);
        if(node == null) return;
        add(node);
    }

    /**
     * Link nodes to its correlation.
     */
    private static void correlateNodes() {
        activeNodes.values().forEach(BaseNode::correlate);
    }

    /**
     * Handle the logic of nodes.
     */
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if(!hasNode(entity)) return;
        Optional.ofNullable(activeNodes.get(Key.Type.NODE_SELF.getContent(entity)))
                .ifPresent(node -> node.perform(event));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasNode(Entity entity) {
        return Key.Type.NODE_TYPE.check(entity);
    }

    public static boolean hasKey(UUID uuid) {
        return activeNodes.containsKey(uuid);
    }

    public static void add(BaseNode node) {
        activeNodes.put(node.getSelfUUID(), node);
    }

    @Nullable
    public static BaseNode get(UUID uuid) {
        return activeNodes.get(uuid);
    }

    /**
     * Remove a node. Automatically call BaseNode::remove.
     * Entity will also be removed.
     * @param uuid of this node
     */
    public static void remove(UUID uuid) {
        Optional.ofNullable(activeNodes.get(uuid)).ifPresent(BaseNode::remove);
        activeNodes.remove(uuid);
    }

    /**
     * Remove a node by the self uuid. Automatically call BaseNode::remove.
     * Entity will also be removed.
     */
    public static void remove(Entity entity) {
        Optional.ofNullable(Key.Type.NODE_SELF.getContent(entity)).ifPresent(NodeManager::remove);
    }

    /**
     * Remove a node. Automatically call BaseNode::remove.
     * Entity will also be removed.
     */
    public static void remove(BaseNode node) {
        remove(node.getSelfUUID());
    }

    /**
     * Unload a node. Automatically call BaseNode::unload.
     * Entity will not be removed.
     */
    public static void unload(UUID uuid) {
        Optional.ofNullable(activeNodes.get(uuid)).ifPresent(BaseNode::unload);
        activeNodes.remove(uuid);
    }

    /**
     * Unload a node by the entity's pdc. Automatically call BaseNode::unload.
     * Entity will not be removed.
     */
    public static void unload(Entity entity) {
        Optional.ofNullable(Key.Type.NODE_SELF.getContent(entity)).ifPresent(NodeManager::unload);
    }

    /**
     * Unload a node. Automatically call BaseNode::unload.
     * Entity will not be removed.
     */
    public static void unload(BaseNode node) {
        unload(node.getSelfUUID());
    }

    /**
     * Remove all nodes with specific owner (like an artifact).
     * @param ownerUUID of the owner
     */
    public static void removeAll(UUID ownerUUID) {
        removeAll(node -> Optional.ofNullable(node.getOwnerUUID()).map(uuid ->
                uuid.equals(ownerUUID)).orElse(false));
    }

    /**
     * Safely remove all nodes with filter using iterator.
     * @param filter judgement to remove
     */
    private static void removeAll(Predicate<BaseNode> filter) {
        Iterator<Map.Entry<UUID, BaseNode>> iterator = activeNodes.entrySet().iterator();
        // The iterator will clear the map, so we can't call remove() in this class
        while(iterator.hasNext()) {
            BaseNode node = iterator.next().getValue();
            if(filter.test(node)) {
                node.remove();
                iterator.remove();
            }
        }
    }

    /**
     * Safely unload all nodes with filter using iterator.
     * @param filter judgement to remove
     */
    private static void unloadAll(Predicate<BaseNode> filter) {
        Iterator<Map.Entry<UUID, BaseNode>> iterator = activeNodes.entrySet().iterator();
        // The iterator will clear the map, so we can't call remove() in this class
        while(iterator.hasNext()) {
            BaseNode node = iterator.next().getValue();
            if(filter.test(node)) {
                node.unload();
                iterator.remove();
            }
        }
    }

    /**
     * Recreate a node based on an entity.
     * Automatically node.load().
     */
    @Nullable
    public static BaseNode recreate(Entity entity) {
        String type = Key.Type.NODE_TYPE.getContent(entity);
        if(type ==  null) return null;

        if(NodeRegistry.ITEM.getName().equalsIgnoreCase(type) && entity instanceof ItemDisplay display) {
            ItemNode node = new ItemNode(entity.getLocation());
            node.load(display);
            return node;
        } else if(NodeRegistry.ACTION.getName().equalsIgnoreCase(type) && entity instanceof Interaction interaction) {
            ActionNode node = new ActionNode(entity.getLocation());
            node.load(interaction);
            return node;
        } else if(NodeRegistry.TEXT.getName().equalsIgnoreCase(type) && entity instanceof TextDisplay display) {
            TextNode node = new TextNode(entity.getLocation());
            node.load(display);
            return node;
        }

        return null;
    }
}
