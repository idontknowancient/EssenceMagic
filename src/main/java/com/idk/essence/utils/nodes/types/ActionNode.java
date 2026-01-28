package com.idk.essence.utils.nodes.types;

import com.idk.essence.utils.nodes.BaseNode;
import com.idk.essence.utils.nodes.NodeRegistry;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.function.Consumer;

public class ActionNode extends BaseNode {

    private float width = 0.4f;
    private float height = 0.4f;
    @Setter private Consumer<PlayerInteractEntityEvent> action;

    public ActionNode(Location location) {
        super(location.clone().add(0, -0.2, 0));
    }

    @Override
    public NodeRegistry getNodeType() {
        return NodeRegistry.ACTION;
    }

    @Override
    public void onSpawn(Entity entity) {
        if(!(entity instanceof Interaction interaction)) return;

        // Set the size for collision box (in blocks)
        interaction.setInteractionWidth(width);
        interaction.setInteractionHeight(height);

        interaction.setResponsive(true);
    }

    @Override
    protected void onLoad(Entity entity) {
        if(!(entity instanceof Interaction interaction)) return;
        setDisplayEntity(interaction);
        generateParticle();
    }

    @Override
    public void onRemove() {

    }

    @Override
    protected void onUnload() {

    }

    @Override
    protected void onCorrelate() {

    }

    @Override
    protected void onPerform(PlayerInteractEntityEvent event) {
        if(action != null) {
            action.accept(event);
        }
    }

    /**
     * Set the size of collision box.
     */
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        if(getDisplayEntity() instanceof Interaction interaction) {
            interaction.setInteractionWidth(width);
            interaction.setInteractionHeight(height);
        }
    }
}
