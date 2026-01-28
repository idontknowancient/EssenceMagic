package com.idk.essence.utils.nodes;

import com.idk.essence.utils.Key;
import com.idk.essence.utils.particles.ParticleManager;
import com.idk.essence.utils.particles.ParticleRegistry;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class BaseNode {

    @Getter @Setter private UUID selfUUID;
    /**
     * To have entities able to reconnect to nodes
     */
    @Nullable @Getter @Setter private UUID ownerUUID;
    /**
     * To make nodes connect to another one
     */
    @Nullable@Getter @Setter private UUID correlationUUID;
    /**
     * To identify the node as an attachment to another node.
     * Will not spawn particle.
     */
    @Setter @Getter private boolean attachment = false;
    @Getter private final Location location;
    @Getter @Setter private Entity displayEntity;
    /**
     * In ticks, -1 means perpetually exist
     */
    @Getter @Setter private int lifeTime = -1;
    private boolean visible = true;
    private Color color = Color.YELLOW;

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
            Key.Type.NODE_TYPE.set(container, getNodeType().getName());
            Key.Type.NODE_SELF.set(container, selfUUID);
            Key.Type.NODE_ATTACHMENT.set(container, attachment);
            Key.Type.PARTICLE.set(container, !attachment);
            Key.Type.NODE_OWNER.set(container, ownerUUID);

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
        selfUUID = Optional.ofNullable(Key.Type.NODE_SELF.getContent(entity)).orElse(selfUUID);
        ownerUUID = Optional.ofNullable(Key.Type.NODE_OWNER.getContent(entity)).orElse(ownerUUID);
        attachment = Optional.ofNullable(Key.Type.NODE_ATTACHMENT.getContent(entity)).orElse(false);
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
        correlationUUID = Optional.ofNullable(Key.Type.NODE_CORRELATION.getContent(getDisplayEntity())).orElse(correlationUUID);
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

    public void setParticleAttribute(boolean visible, Color color) {
        this.visible = visible;
        this.color = color;
    }

    public void generateParticle() {
        if(attachment || displayEntity == null || !visible || ParticleManager.hasKey(selfUUID)) return;
        ParticleManager.generate(displayEntity, ParticleRegistry.POINT, new Particle.DustOptions(color, 1));
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
