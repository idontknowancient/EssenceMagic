package com.idk.essence.utils.nodes;

import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.particles.ParticleManager;
import com.idk.essence.utils.particles.ParticleRegistry;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.UUID;

public abstract class BaseNode {

    @Getter @Setter private UUID selfUUID;
    /**
     * To have entities able to reconnect to nodes
     */
    @Getter @Setter private UUID ownerUUID;
    /**
     * To make nodes connect to another one
     */
    @Getter @Setter private UUID correlationUUID;
    /**
     * To identify the node as an attachment to another node.
     * Will not spawn particle.
     * -- SETTER --
     *  Set whether the node is an attachment to another node.
     *  Automatically add to pdc.

     */
    @Setter
    @Getter private boolean attachment = false;
    @Getter private final Location location;
    @Getter @Setter private Entity displayEntity;
    /**
     * In ticks, -1 means perpetually exist
     */
    @Getter @Setter private int lifeTime = -1;
    private boolean visible = true;
    private String color = "yellow";

    public BaseNode(Location location) {
        this.selfUUID = UUID.randomUUID();
        this.location = location;
    }

    /**
     * Spawn from scratch.
     * Automatically add to map.
     */
    public void spawn() {
        Class<? extends Entity> entityClass = getNodeType().getEntityClass();

        location.getWorld().spawn(location, entityClass, entity -> {
            PersistentDataContainer container = entity.getPersistentDataContainer();
            container.set(Key.Class.NODE_TYPE.get(), PersistentDataType.STRING, getNodeType().getName());
            container.set(Key.Class.NODE_SELF.get(), PersistentDataType.STRING, selfUUID.toString());
            container.set(Key.Class.NODE_ATTACHMENT.get(),  PersistentDataType.BOOLEAN, attachment);
            container.set(Key.Class.PARTICLE.get(),  PersistentDataType.BOOLEAN, !attachment);
            if(ownerUUID != null) {
                container.set(Key.Class.NODE_OWNER.get(), PersistentDataType.STRING, ownerUUID.toString());
            }

            this.displayEntity = entity;
            // Initialization of subclass
            onSpawn(entity);
            generateParticle();
        });

        NodeManager.add(this);
    }

    /**
     * Load from an entity. Get and set selfUUID, ownerUUID, and attachment from the pdc.
     * Subclass should invoke generateParticle() if wanted.
     */
    public void load(Entity entity) {
        selfUUID = Optional.ofNullable(Util.getUUIDFromContainer(entity)).orElse(selfUUID);
        Optional.ofNullable(entity.getPersistentDataContainer()
                .get(Key.Class.NODE_OWNER.get(), PersistentDataType.STRING)).ifPresent(ownerUUID ->
                setOwnerUUID(UUID.fromString(ownerUUID)));
        attachment = Optional.ofNullable(entity.getPersistentDataContainer()
                .get(Key.Class.NODE_ATTACHMENT.get(), PersistentDataType.BOOLEAN)).orElse(false);
        onLoad(entity);
    }

    /**
     * Fully remove, including the entity.
     */
    public void remove() {
        // Clear logic of subclass
        onRemove();
        if(displayEntity != null)
            displayEntity.remove();
        stopParticle();
    }

    /**
     * Stop task or some resource-consuming tasks.
     */
    public void unload() {
        onUnload();
        stopParticle();
    }

    /**
     * Get and set correlationUUID from the pdc.
     * Subclass should implement onCorrelate() to actually link the node.
     */
    public void correlate() {
        Optional.ofNullable(displayEntity.getPersistentDataContainer().get(
                Key.Class.NODE_CORRELATION.get(), PersistentDataType.STRING)).ifPresent(uuidString ->
                setCorrelationUUID(UUID.fromString(uuidString)));
        onCorrelate();
    }

    /**
     * Perform what the node should do when interacted.
     */
    public void perform(PlayerInteractEntityEvent event) {
        onPerform(event);
    }

    public boolean isExpired() {
        return lifeTime != -1 && lifeTime <= 0;
    }

    /**
     * @param frequency e.g. 5 ticks per update
     */
    public void updateLifetime(int frequency) {
        if(lifeTime != -1)
            lifeTime = Math.max(0, lifeTime - frequency);
    }

    public void setParticleAttribute(boolean visible, String color) {
        this.visible = visible;
        this.color = color;
    }

    public void generateParticle() {
        if(attachment || displayEntity == null || !visible || ParticleManager.hasKey(selfUUID)) return;
        ParticleManager.generate(displayEntity, ParticleRegistry.POINT);
    }

    public void stopParticle() {
        if(visible)
            ParticleManager.stop(selfUUID);
    }

    protected abstract void onSpawn(Entity entity);
    protected abstract void onLoad(Entity entity);
    protected abstract void onRemove();
    protected abstract void onUnload();
    protected abstract void onCorrelate();
    protected abstract void onPerform(PlayerInteractEntityEvent event);
    public abstract NodeRegistry getNodeType();
}
